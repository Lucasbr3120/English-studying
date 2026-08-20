package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.AiCorrectionResult
import com.example.data.model.CefrLevel
import com.example.data.model.ContractionPair
import com.example.data.model.Scene
import com.example.data.model.ScenePhrase
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiPart(val text: String? = null)

@JsonClass(generateAdapter = true)
data class GeminiContent(val parts: List<GeminiPart>, val role: String? = null)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(val content: GeminiContent?)

@JsonClass(generateAdapter = true)
data class GeminiResponse(val candidates: List<GeminiCandidate>?)

class GeminiAiService {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val apiKey: String
        get() = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

    private val isKeyValid: Boolean
        get() = apiKey.isNotBlank() && !apiKey.equals("MY_GEMINI_API_KEY", ignoreCase = true)

    suspend fun evaluateContractionWithAi(
        userInput: String,
        phrase: ScenePhrase
    ): Pair<AiCorrectionResult, String?> = withContext(Dispatchers.IO) {
        val (localResult, structureTag) = IntelligentCorrectionEngine.evaluateContractions(userInput, phrase)
        if (!isKeyValid) {
            return@withContext Pair(localResult, structureTag)
        }

        val prompt = """
            Você é um professor de inglês nativo e especialista no inglês falado de filmes e séries de Hollywood.
            Avalie a transformação para forma contraída/natural enviada pelo aluno.
            
            Frase Completa Original: "${phrase.fullForm}"
            Forma Natural Esperada: "${phrase.naturalForm}"
            Resposta do Aluno: "$userInput"
            
            Analise com atenção:
            1. Gramática (se o aluno omitiu o verbo ou auxiliar sem contrair, ex: 'I going' em vez de 'I'm going').
            2. Contrações (se usou o apóstrofo (') e as contrações corretas).
            3. Pontuação e ordem das palavras.
            4. Contexto da cena.
            
            Se houver erro, explique de forma curta, amigável e direta em português do Brasil para o nível do aluno (sem jargões acadêmicos pesados).
            
            Responda EXATAMENTE no seguinte formato de linhas:
            IS_CORRECT: [TRUE ou FALSE]
            TITLE: [Título curto e encorajador, ex: 'Perfeito! 🎬' ou 'Atenção ao verbo contraído']
            MESSAGE: [Explicação curta e didática do acerto ou erro]
            SUGGESTION: [Forma natural correta]
            GRAMMAR_TIP: [Dica rápida de pronúncia ou gramática falada]
        """.trimIndent()

        try {
            val responseText = callGemini(prompt)
            if (responseText.isNullOrBlank()) {
                return@withContext Pair(localResult, structureTag)
            }
            Pair(parseAiEvaluationResponse(responseText, phrase), structureTag)
        } catch (e: Exception) {
            Log.w("GeminiAiService", "Fallback to local evaluator: ${e.message}")
            Pair(localResult, structureTag)
        }
    }

    suspend fun evaluateTranslationWithAi(
        userInput: String,
        phrase: ScenePhrase
    ): AiCorrectionResult = withContext(Dispatchers.IO) {
        val localResult = IntelligentCorrectionEngine.evaluateTranslation(userInput, phrase)
        if (!isKeyValid) {
            return@withContext localResult
        }

        val prompt = """
            Você é um professor especialista em inglês de cinema e português do Brasil.
            Avalie a tradução do aluno.
            
            Frase em inglês falado: "${phrase.naturalForm}"
            Tradução de referência: "${phrase.portugueseTranslation}"
            Tradução do aluno: "$userInput"
            
            DIRETRIZES DE AVALIAÇÃO:
            - Avalie semanticamente (o sentido real), e NÃO correspondência palavra por palavra.
            - Aceite variações coloquiais naturais do Brasil (ex: 'Eu estou cansado', 'Estou cansado', 'Eu tô cansado', 'Tô cansado', 'A gente vai', 'Nós vamos').
            - Se a tradução estiver muito literal e alterar a naturalidade (ex: traduzir expressões fixas palavra por palavra), aponte isso amigavelmente na explicação.
            
            Responda EXATAMENTE nas seguintes linhas:
            IS_CORRECT: [TRUE ou FALSE]
            TITLE: [Título curto em português, ex: 'Excelente Tradução! 🇧🇷' ou 'Quase lá!']
            MESSAGE: [Explicação curta e encorajadora do sentido transmitido]
            SUGGESTION: [Sugestão de tradução mais cinematográfica/natural]
            GRAMMAR_TIP: [Dica rápida de vocabulário ou contexto]
        """.trimIndent()

        try {
            val responseText = callGemini(prompt)
            if (responseText.isNullOrBlank()) {
                return@withContext localResult
            }
            parseAiEvaluationResponse(responseText, phrase)
        } catch (e: Exception) {
            Log.w("GeminiAiService", "Fallback to local evaluator: ${e.message}")
            localResult
        }
    }

    suspend fun generateCustomScene(
        userPrompt: String,
        level: CefrLevel
    ): Scene = withContext(Dispatchers.IO) {
        if (!isKeyValid) {
            return@withContext generateFallbackScene(userPrompt, level)
        }

        val prompt = """
            Crie uma cena curta de diálogo no estilo de filme/série com 3 falas consecutivas para aprendizado de inglês falado.
            Nível CEFR: ${level.code} (${level.title})
            Tema solicitado: "$userPrompt"
            
            Retorne em formato de texto estruturado seguindo rigorosamente:
            TITLE: [Título da Cena em Inglês]
            GENRE: [Gênero, ex: Drama, Comédia, Suspense]
            CONTEXT: [Descrição do contexto em português]
            VOCAB: [palavra1, palavra2, palavra3]
            
            ---PHRASE 1---
            CHAR: [Nome do Personagem]
            FULL: [Frase sem contração em inglês]
            NATURAL: [Frase contraída e natural em inglês com contrações reais]
            TRANSLATION: [Tradução natural em português]
            CONTRACTIONS: [Full1 -> Short1 | Explicação em português; Full2 -> Short2 | Explicação]
            GRAMMAR: [Dica gramatical curta em português]
            EXAMPLE: [Frase de exemplo adicional em inglês]
            EXAMPLE_TRANS: [Tradução do exemplo adicional]
            BLANK_SENTENCE: [Frase com lacuna '______']
            BLANK_ANSWER: [Resposta correta da lacuna]
            BLANK_OPTIONS: [opt1, opt2, opt3, opt4]
            QUIZ_Q: [Pergunta sobre expressão ou significado]
            QUIZ_A: [Resposta correta]
            QUIZ_OPTIONS: [opt1, opt2, opt3, opt4]
            
            ---PHRASE 2---
            [Mesma estrutura para frase 2]
            
            ---PHRASE 3---
            [Mesma estrutura para frase 3]
        """.trimIndent()

        try {
            val responseText = callGemini(prompt)
            if (responseText.isNullOrBlank()) {
                return@withContext generateFallbackScene(userPrompt, level)
            }
            parseGeneratedScene(responseText, level, userPrompt)
        } catch (e: Exception) {
            Log.e("GeminiAiService", "Failed to generate scene with AI: ${e.message}")
            generateFallbackScene(userPrompt, level)
        }
    }

    private fun callGemini(prompt: String): String? {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
        val requestObj = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(text = prompt))
                )
            ),
            systemInstruction = GeminiContent(
                parts = listOf(GeminiPart(text = "Você é um professor de inglês nativo e fluente em português especializado no inglês natural e falado em filmes e séries de Hollywood. Seja encorajador, preciso e didático."))
            )
        )

        val adapter = moshi.adapter(GeminiRequest::class.java)
        val jsonBody = adapter.toJson(requestObj)
        val body = jsonBody.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.w("GeminiAiService", "HTTP error ${response.code}: ${response.body?.string()}")
                return null
            }
            val responseBody = response.body?.string() ?: return null
            val respAdapter = moshi.adapter(GeminiResponse::class.java)
            val parsed = respAdapter.fromJson(responseBody)
            return parsed?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        }
    }

    private fun parseAiEvaluationResponse(aiText: String, phrase: ScenePhrase): AiCorrectionResult {
        var isCorrect = true
        var title = "Avaliação Concluída"
        var message = "Sua resposta foi analisada."
        var suggestion = phrase.naturalForm
        var grammar = phrase.grammarTip

        aiText.lines().forEach { line ->
            val trimmed = line.trim()
            when {
                trimmed.startsWith("IS_CORRECT:", ignoreCase = true) -> {
                    isCorrect = trimmed.substringAfter(":").trim().equals("TRUE", ignoreCase = true)
                }
                trimmed.startsWith("TITLE:", ignoreCase = true) -> {
                    title = trimmed.substringAfter(":").trim()
                }
                trimmed.startsWith("MESSAGE:", ignoreCase = true) -> {
                    message = trimmed.substringAfter(":").trim()
                }
                trimmed.startsWith("SUGGESTION:", ignoreCase = true) -> {
                    suggestion = trimmed.substringAfter(":").trim()
                }
                trimmed.startsWith("GRAMMAR_TIP:", ignoreCase = true) -> {
                    grammar = trimmed.substringAfter(":").trim()
                }
            }
        }

        return AiCorrectionResult(
            isCorrect = isCorrect,
            feedbackTitle = title,
            feedbackMessage = message,
            suggestedImprovement = if (suggestion.isNotBlank()) "Forma esperada: \"$suggestion\"" else null,
            grammarExplanation = grammar,
            additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
            scorePercentage = if (isCorrect) 100 else 45
        )
    }

    private fun parseGeneratedScene(rawText: String, level: CefrLevel, fallbackTheme: String): Scene {
        var title = "Cena Personalizada: $fallbackTheme"
        var genre = "Diálogo de Filme"
        var context = "Cena gerada por IA baseada no seu pedido."
        var vocabList = listOf("natural speech", "contractions", "dialogue")
        val phrases = mutableListOf<ScenePhrase>()

        val rawBlocks = rawText.split(Regex("---PHRASE \\d+---"))
        if (rawBlocks.isNotEmpty()) {
            val headerLines = rawBlocks[0].lines()
            for (line in headerLines) {
                val t = line.trim()
                when {
                    t.startsWith("TITLE:", ignoreCase = true) -> title = t.substringAfter(":").trim()
                    t.startsWith("GENRE:", ignoreCase = true) -> genre = t.substringAfter(":").trim()
                    t.startsWith("CONTEXT:", ignoreCase = true) -> context = t.substringAfter(":").trim()
                    t.startsWith("VOCAB:", ignoreCase = true) -> {
                        vocabList = t.substringAfter(":").split(",").map { it.trim() }.filter { it.isNotBlank() }
                    }
                }
            }

            for (i in 1 until rawBlocks.size) {
                val phraseBlock = rawBlocks[i]
                var charName = "Character $i"
                var full = ""
                var natural = ""
                var trans = ""
                var grammar = "Atenção às contrações no diálogo falado."
                var example = "I'm sure you'll love it."
                var exTrans = "Tenho certeza de que você vai adorar."
                var blankSentence = "I ______ think this is right."
                var blankAnswer = "don't"
                var blankOptions = listOf("don't", "didn't", "won't", "wasn't")
                var quizQ = "O que a frase expressa?"
                var quizA = "Expressa concordância natural"
                var quizOptions = listOf("Expressa concordância natural", "Ordem agressiva", "Dúvida no passado", "Despedida")
                val contractionList = mutableListOf<ContractionPair>()

                for (line in phraseBlock.lines()) {
                    val t = line.trim()
                    when {
                        t.startsWith("CHAR:", ignoreCase = true) -> charName = t.substringAfter(":").trim()
                        t.startsWith("FULL:", ignoreCase = true) -> full = t.substringAfter(":").trim()
                        t.startsWith("NATURAL:", ignoreCase = true) -> natural = t.substringAfter(":").trim()
                        t.startsWith("TRANSLATION:", ignoreCase = true) -> trans = t.substringAfter(":").trim()
                        t.startsWith("GRAMMAR:", ignoreCase = true) -> grammar = t.substringAfter(":").trim()
                        t.startsWith("EXAMPLE:", ignoreCase = true) -> example = t.substringAfter(":").trim()
                        t.startsWith("EXAMPLE_TRANS:", ignoreCase = true) -> exTrans = t.substringAfter(":").trim()
                        t.startsWith("BLANK_SENTENCE:", ignoreCase = true) -> blankSentence = t.substringAfter(":").trim()
                        t.startsWith("BLANK_ANSWER:", ignoreCase = true) -> blankAnswer = t.substringAfter(":").trim()
                        t.startsWith("BLANK_OPTIONS:", ignoreCase = true) -> {
                            val opts = t.substringAfter(":").split(",").map { it.trim() }.filter { it.isNotBlank() }
                            if (opts.size >= 2) blankOptions = opts
                        }
                        t.startsWith("QUIZ_Q:", ignoreCase = true) -> quizQ = t.substringAfter(":").trim()
                        t.startsWith("QUIZ_A:", ignoreCase = true) -> quizA = t.substringAfter(":").trim()
                        t.startsWith("QUIZ_OPTIONS:", ignoreCase = true) -> {
                            val opts = t.substringAfter(":").split(",").map { it.trim() }.filter { it.isNotBlank() }
                            if (opts.size >= 2) quizOptions = opts
                        }
                        t.startsWith("CONTRACTIONS:", ignoreCase = true) -> {
                            val rawPairs = t.substringAfter(":").split(";")
                            for (p in rawPairs) {
                                val parts = p.split("|")
                                if (parts.isNotEmpty()) {
                                    val forms = parts[0].split("->")
                                    val f1 = forms.getOrNull(0)?.trim() ?: ""
                                    val f2 = forms.getOrNull(1)?.trim() ?: ""
                                    val expl = parts.getOrNull(1)?.trim() ?: "Contração natural no diálogo falado."
                                    if (f1.isNotBlank() && f2.isNotBlank()) {
                                        contractionList.add(ContractionPair(f1, f2, expl))
                                    }
                                }
                            }
                        }
                    }
                }

                if (natural.isNotBlank()) {
                    phrases.add(
                        ScenePhrase(
                            id = "ai_${UUID.randomUUID().toString().take(8)}",
                            characterName = charName,
                            fullForm = full.ifBlank { natural },
                            naturalForm = natural,
                            portugueseTranslation = trans.ifBlank { "Tradução contextualizada" },
                            contractionsUsed = contractionList,
                            vocabularyNotes = "Expressões geradas por IA para o nível ${level.code}.",
                            grammarTip = grammar,
                            additionalExample = example,
                            additionalExampleTranslation = exTrans,
                            blankSentence = blankSentence,
                            blankCorrectAnswer = blankAnswer,
                            blankOptions = blankOptions,
                            quizQuestion = quizQ,
                            quizCorrectAnswer = quizA,
                            quizOptions = quizOptions
                        )
                    )
                }
            }
        }

        if (phrases.isEmpty()) {
            return generateFallbackScene(fallbackTheme, level)
        }

        return Scene(
            id = "custom_ai_${UUID.randomUUID().toString().take(8)}",
            title = title,
            level = level,
            durationMinutes = 4,
            difficultyStars = when (level) {
                CefrLevel.A1 -> 1
                CefrLevel.A2 -> 2
                CefrLevel.B1 -> 3
                CefrLevel.B2 -> 4
                CefrLevel.C1 -> 5
            },
            contextDescription = context,
            genre = genre,
            imageResName = "scene_coffee_shop",
            mainVocabulary = vocabList,
            phrases = phrases
        )
    }

    private fun generateFallbackScene(theme: String, level: CefrLevel): Scene {
        return Scene(
            id = "fallback_scene_${System.currentTimeMillis()}",
            title = "Scene: $theme",
            level = level,
            durationMinutes = 4,
            difficultyStars = when (level) {
                CefrLevel.A1 -> 1
                CefrLevel.A2 -> 2
                CefrLevel.B1 -> 3
                CefrLevel.B2 -> 4
                CefrLevel.C1 -> 5
            },
            contextDescription = "Diálogo gerado com foco em contrações e expressões naturais para o tema \"$theme\".",
            genre = "Diálogo Cinematográfico",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("talk over", "decision", "take action", "agree"),
            phrases = listOf(
                ScenePhrase(
                    id = "fb_p1",
                    characterName = "Alex",
                    fullForm = "I cannot believe we are already talking about this again.",
                    naturalForm = "I can't believe we're already talkin' about this again.",
                    portugueseTranslation = "Não consigo acreditar que já estamos falando sobre isso de novo.",
                    acceptableTranslations = listOf(
                        "Não acredito que já estamos conversando sobre isso de novo.",
                        "Inacreditável que já estamos falando disso novamente."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("cannot", "can't", "'cannot' vira 'can't'."),
                        ContractionPair("we are", "we're", "'we are' vira 'we're'.")
                    ),
                    vocabularyNotes = "'talking about' = conversando a respeito.",
                    grammarTip = "'Can't believe' é comum para demonstrar surpresa.",
                    additionalExample = "I can't believe you're here.",
                    additionalExampleTranslation = "Não acredito que você está aqui.",
                    blankSentence = "I ______ believe we're here again.",
                    blankCorrectAnswer = "can't",
                    blankOptions = listOf("can't", "don't", "won't", "shouldn't"),
                    quizQuestion = "Qual a sensação expressa por 'can't believe'?",
                    quizCorrectAnswer = "Surpresa ou incredulidade",
                    quizOptions = listOf("Surpresa ou incredulidade", "Alegria extrema", "Dúvida formal", "Despedida")
                ),
                ScenePhrase(
                    id = "fb_p2",
                    characterName = "Jordan",
                    fullForm = "You do not have to worry, I will make sure it does not happen again.",
                    naturalForm = "You don't gotta worry, I'll make sure it doesn't happen again.",
                    portugueseTranslation = "Você não precisa se preocupar, eu vou garantir que isso não aconteça de novo.",
                    acceptableTranslations = listOf(
                        "Não precisa esquentar a cabeça, vou garantir que não ocorra novamente.",
                        "Você não tem que se preocupar, eu garanto que não vai acontecer de novo."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("do not", "don't", "'do not' vira 'don't'."),
                        ContractionPair("have to", "gotta", "'have to' vira 'gotta' na fala informal."),
                        ContractionPair("I will", "I'll", "'I will' vira 'I'll'."),
                        ContractionPair("does not", "doesn't", "'does not' vira 'doesn't'.")
                    ),
                    vocabularyNotes = "'make sure' = certificar-se, garantir.",
                    grammarTip = "'Make sure' é seguido de oração no presente simples.",
                    additionalExample = "I'll make sure she gets the message.",
                    additionalExampleTranslation = "Vou garantir que ela receba o recado.",
                    blankSentence = "I'll make sure it ______ happen again.",
                    blankCorrectAnswer = "doesn't",
                    blankOptions = listOf("doesn't", "don't", "won't", "didn't"),
                    quizQuestion = "O que significa 'make sure'?",
                    quizCorrectAnswer = "Garantir ou certificar-se de algo",
                    quizOptions = listOf("Garantir ou certificar-se de algo", "Ter certeza absoluta", "Construir algo sólido", "Duvidar")
                )
            )
        )
    }
}

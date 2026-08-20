package com.example.data.ai

import com.example.data.model.AiCorrectionResult
import com.example.data.model.ContractionPair
import com.example.data.model.ScenePhrase
import java.text.Normalizer
import java.util.Locale

data class ProgressiveHint(
    val level: Int, // 1 = Dica pequena, 2 = Dica específica, 3 = Parte da resposta, 4 = Resposta completa
    val hintTitle: String,
    val hintText: String,
    val partialAnswer: String? = null
)

object IntelligentCorrectionEngine {

    fun normalizeText(text: String): String {
        val trimmed = text.trim().lowercase(Locale.ROOT)
        val noAccents = Normalizer.normalize(trimmed, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        return noAccents.replace("[^a-z0-9'\\s]".toRegex(), " ")
            .replace("\\s+".toRegex(), " ")
            .trim()
    }

    private fun normalizePortugueseEquivalents(text: String): String {
        var norm = normalizeText(text)
        val replacements = listOf(
            "\\bto\\b" to "estou",
            "\\btou\\b" to "estou",
            "\\beu estou\\b" to "estou",
            "\\beu to\\b" to "estou",
            "\\beu tou\\b" to "estou",
            "\\bta\\b" to "esta",
            "\\btamem\\b" to "tambem",
            "\\btbm\\b" to "tambem",
            "\\beu vou\\b" to "vou",
            "\\beu nao\\b" to "nao",
            "\\ba gente ta\\b" to "estamos",
            "\\ba gente esta\\b" to "estamos",
            "\\ba gente vai\\b" to "vamos",
            "\\ba gente\\b" to "nos",
            "\\bnos\\b" to "nos",
            "\\bpra\\b" to "para",
            "\\bpro\\b" to "para o",
            "\\bpras\\b" to "para as",
            "\\bpros\\b" to "para os",
            "\\bpois\\b" to "porque",
            "\\bja que\\b" to "porque",
            "\\bvisto que\\b" to "porque",
            "\\bmercado\\b" to "supermercado",
            "\\bloja\\b" to "supermercado",
            "\\bcinema\\b" to "filme",
            "\\bfilmes\\b" to "filme"
        )
        for ((regex, replacement) in replacements) {
            norm = norm.replace(regex.toRegex(), replacement)
        }
        return norm.replace("\\s+".toRegex(), " ").trim()
    }

    /**
     * Intelligent Contraction Evaluation with Deep Grammatical & Diagnostic Analysis
     */
    fun evaluateContractions(
        userInput: String,
        phrase: ScenePhrase
    ): Pair<AiCorrectionResult, String?> {
        val userClean = userInput.trim().replace("\\s+".toRegex(), " ")
        val targetClean = phrase.naturalForm.trim().replace("\\s+".toRegex(), " ")
        val fullClean = phrase.fullForm.trim().replace("\\s+".toRegex(), " ")

        val userNorm = normalizeText(userClean)
        val targetNorm = normalizeText(targetClean)
        val fullNorm = normalizeText(fullClean)

        // Structure tag for adaptive learning tracking
        val primaryStructureTag = phrase.contractionsUsed.firstOrNull()?.let {
            "${it.fullForm} → ${it.contractedForm}"
        } ?: "Contração Geral"

        // 1. Exact Match (ignoring case)
        if (userClean.equals(targetClean, ignoreCase = true)) {
            return Pair(
                AiCorrectionResult(
                    isCorrect = true,
                    feedbackTitle = "Perfeito! 🎉",
                    feedbackMessage = "Você aplicou as contrações do inglês falado com total precisão.",
                    contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                    grammarExplanation = phrase.grammarTip,
                    additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                    scorePercentage = 100
                ),
                primaryStructureTag
            )
        }

        // 2. Diagnosing Common Grammatical & Contraction Mistakes:

        // A. Checking if user dropped auxiliary verb completely (e.g. "I going" instead of "I'm going")
        val droppedVerbFeedback = checkDroppedVerb(userClean, phrase)
        if (droppedVerbFeedback != null) {
            return Pair(
                AiCorrectionResult(
                    isCorrect = false,
                    feedbackTitle = "Atenção ao Verbo Contraído! ⚠️",
                    feedbackMessage = droppedVerbFeedback,
                    suggestedImprovement = "Forma correta falada: \"${phrase.naturalForm}\"",
                    contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                    grammarExplanation = phrase.grammarTip,
                    additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                    scorePercentage = 35
                ),
                primaryStructureTag
            )
        }

        // B. Checking missing apostrophes (e.g. "Im", "dont", "cant", "wont", "theyre")
        val missingApostrophe = checkMissingApostrophe(userClean, phrase)
        if (missingApostrophe != null) {
            return Pair(
                AiCorrectionResult(
                    isCorrect = false,
                    feedbackTitle = "Faltou o Apóstrofo (') ✍️",
                    feedbackMessage = missingApostrophe,
                    suggestedImprovement = "Com apóstrofo: \"${phrase.naturalForm}\"",
                    contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                    grammarExplanation = "No inglês escrito, o apóstrofo (') indica onde letras foram omitidas na contração falada.",
                    additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                    scorePercentage = 75
                ),
                primaryStructureTag
            )
        }

        // C. Checking if user left the sentence completely uncontracted
        if (userNorm == fullNorm || userClean.equals(fullClean, ignoreCase = true)) {
            val missingPairs = phrase.contractionsUsed.joinToString(", ") { "\"${it.fullForm}\" → \"${it.contractedForm}\"" }
            return Pair(
                AiCorrectionResult(
                    isCorrect = false,
                    feedbackTitle = "Frase não foi contraída! 🎬",
                    feedbackMessage = "Você manteve a frase na forma formal longa. Para soar natural no cinema, contraia: $missingPairs.",
                    suggestedImprovement = "Forma falada esperada: \"${phrase.naturalForm}\"",
                    contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                    grammarExplanation = phrase.grammarTip,
                    additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                    scorePercentage = 40
                ),
                primaryStructureTag
            )
        }

        // D. Checking uncontracted specific pairs
        val uncontractedItems = phrase.contractionsUsed.filter { pair ->
            userClean.contains(pair.fullForm, ignoreCase = true)
        }
        if (uncontractedItems.isNotEmpty()) {
            val list = uncontractedItems.joinToString(", ") { "\"${it.fullForm}\" → \"${it.contractedForm}\"" }
            val specificTag = "${uncontractedItems.first().fullForm} → ${uncontractedItems.first().contractedForm}"
            return Pair(
                AiCorrectionResult(
                    isCorrect = false,
                    feedbackTitle = "Quase lá! Faltou contrair 💡",
                    feedbackMessage = "Você deixou algumas palavras na forma formal completa. Transforme: $list.",
                    suggestedImprovement = "Resposta natural esperada: \"${phrase.naturalForm}\"",
                    contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                    grammarExplanation = phrase.grammarTip,
                    additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                    scorePercentage = 55
                ),
                specificTag
            )
        }

        // E. Checking Levenshtein / Minor Typos
        val distance = levenshteinDistance(userNorm, targetNorm)
        if (distance in 1..2) {
            return Pair(
                AiCorrectionResult(
                    isCorrect = true,
                    feedbackTitle = "Correto (com pequeno desvio) 👍",
                    feedbackMessage = "Você aplicou as contrações certas! Houve apenas um pequeno detalhe de digitação ou pontuação.",
                    suggestedImprovement = "Grafia exata: \"${phrase.naturalForm}\"",
                    contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                    grammarExplanation = phrase.grammarTip,
                    additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                    scorePercentage = 90
                ),
                primaryStructureTag
            )
        }

        // F. General Mismatch / Word Order Issue
        val userWords = userNorm.split(" ").filter { it.isNotBlank() }.toSet()
        val targetWords = targetNorm.split(" ").filter { it.isNotBlank() }.toSet()
        val commonWords = userWords.intersect(targetWords).size
        val similarity = if (targetWords.isNotEmpty()) commonWords.toDouble() / targetWords.size.toDouble() else 0.0

        if (similarity >= 0.6) {
            return Pair(
                AiCorrectionResult(
                    isCorrect = false,
                    feedbackTitle = "Atenção à ordem ou palavras!",
                    feedbackMessage = "Você capturou parte da ideia, mas algumas palavras ou a ordem na frase ficaram diferentes do diálogo original.",
                    suggestedImprovement = "Frase natural esperada: \"${phrase.naturalForm}\"",
                    contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                    grammarExplanation = phrase.grammarTip,
                    additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                    scorePercentage = (similarity * 100).toInt().coerceIn(30, 65)
                ),
                primaryStructureTag
            )
        }

        return Pair(
            AiCorrectionResult(
                isCorrect = false,
                feedbackTitle = "Vamos revisar esta frase! 🎯",
                feedbackMessage = "A resposta fornecida difere significativamente do diálogo esperado.",
                suggestedImprovement = "Resposta esperada: \"${phrase.naturalForm}\"",
                contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = 25
            ),
            primaryStructureTag
        )
    }

    private fun checkDroppedVerb(userInput: String, phrase: ScenePhrase): String? {
        val userTokens = userInput.lowercase(Locale.ROOT).split("[^a-z']+".toRegex()).filter { it.isNotBlank() }

        for (pair in phrase.contractionsUsed) {
            val fullParts = pair.fullForm.lowercase(Locale.ROOT).split(" ")
            if (fullParts.size >= 2) {
                val subject = fullParts[0] // e.g. "i", "you", "we", "he", "she", "do"
                val verb = fullParts[1]    // e.g. "am", "are", "is", "not", "have"

                val hasSubject = userTokens.contains(subject)
                val hasVerb = userTokens.contains(verb)
                val hasContracted = userTokens.any { it.contains(pair.contractedForm.lowercase(Locale.ROOT)) || it.contains(pair.contractedForm.replace("'", "").lowercase(Locale.ROOT)) }

                if (hasSubject && !hasVerb && !hasContracted) {
                    return "Você removeu o '${verb}', mas não substituiu por '${pair.contractedForm}'. No inglês falado, o verbo não desaparece: ele se funde com o pronome ('${pair.fullForm}' vira '${pair.contractedForm}')."
                }
            }
        }
        return null
    }

    private fun checkMissingApostrophe(userInput: String, phrase: ScenePhrase): String? {
        val userTokens = userInput.lowercase(Locale.ROOT).split("\\s+".toRegex())

        for (pair in phrase.contractionsUsed) {
            val contractedNoApostrophe = pair.contractedForm.replace("'", "").lowercase(Locale.ROOT)
            val contractedReal = pair.contractedForm.lowercase(Locale.ROOT)

            if (userTokens.any { it == contractedNoApostrophe } && !userInput.lowercase(Locale.ROOT).contains(contractedReal)) {
                return "Você escreveu '$contractedNoApostrophe' sem apóstrofo. A forma correta é '${pair.contractedForm}'."
            }
        }
        return null
    }

    /**
     * Intelligent Semantic Translation Evaluation for Brazilian Portuguese
     */
    fun evaluateTranslation(
        userInput: String,
        phrase: ScenePhrase
    ): AiCorrectionResult {
        val userNorm = normalizePortugueseEquivalents(userInput)
        val targetNorm = normalizePortugueseEquivalents(phrase.portugueseTranslation)
        val allAcceptable = (listOf(phrase.portugueseTranslation) + phrase.acceptableTranslations)
            .map { normalizePortugueseEquivalents(it) }

        // 1. Direct or Colloquial Match
        if (allAcceptable.contains(userNorm)) {
            return AiCorrectionResult(
                isCorrect = true,
                feedbackTitle = "Excelente Tradução! 🇧🇷",
                feedbackMessage = "Você capturou com precisão o sentido natural da cena, adaptando perfeitamente para o português falado no Brasil.",
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = 100
            )
        }

        // 2. Overly Literal Check
        val literalWarning = detectOverlyLiteralTranslation(userInput, phrase)

        // 3. Token Overlap & Similarity
        val userTokens = userNorm.split(" ").filter { it.isNotBlank() }.toSet()
        var maxMatchRatio = 0.0

        for (target in allAcceptable) {
            val targetTokens = target.split(" ").filter { it.isNotBlank() }.toSet()
            val intersection = userTokens.intersect(targetTokens).size
            val union = userTokens.union(targetTokens).size
            val ratio = if (union > 0) intersection.toDouble() / union.toDouble() else 0.0
            if (ratio > maxMatchRatio) maxMatchRatio = ratio
        }

        val bestTarget = allAcceptable.firstOrNull() ?: targetNorm
        val distance = levenshteinDistance(userNorm, bestTarget)
        val maxLen = maxOf(userNorm.length, bestTarget.length)
        val stringSimilarity = if (maxLen > 0) 1.0 - (distance.toDouble() / maxLen.toDouble()) else 0.0

        val overallSimilarity = maxOf(maxMatchRatio, stringSimilarity)

        if (overallSimilarity >= 0.62 || (userTokens.size >= 3 && maxMatchRatio >= 0.50)) {
            val msg = if (literalWarning != null) {
                "Tradução aceita pelo sentido geral, mas atenção: $literalWarning"
            } else {
                "Tradução natural aceita! O significado da cena foi transmitido perfeitamente."
            }

            return AiCorrectionResult(
                isCorrect = true,
                feedbackTitle = "Muito Bom! 👏",
                feedbackMessage = msg,
                suggestedImprovement = "Versão clássica dublada/legendada: \"${phrase.portugueseTranslation}\"",
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = (overallSimilarity * 100).toInt().coerceIn(75, 100)
            )
        } else {
            val msg = if (literalWarning != null) {
                "Sua tradução ficou muito literal: $literalWarning"
            } else {
                "Sua tradução expressou parte do sentido, mas não refletiu o significado real do diálogo na cena."
            }

            return AiCorrectionResult(
                isCorrect = false,
                feedbackTitle = "Quase lá! Vamos ajustar 🎯",
                feedbackMessage = msg,
                suggestedImprovement = "Tradução natural esperada: \"${phrase.portugueseTranslation}\"",
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = (overallSimilarity * 100).toInt().coerceIn(20, 55)
            )
        }
    }

    private fun detectOverlyLiteralTranslation(userInput: String, phrase: ScenePhrase): String? {
        val lower = userInput.lowercase(Locale.ROOT)
        if (phrase.naturalForm.contains("bad feeling", ignoreCase = true) && (lower.contains("mau sentimento") || lower.contains("ruim sentimento"))) {
            return "Em filmes, 'bad feeling' deve ser traduzido como 'mau pressentimento', não 'mau sentimento'."
        }
        if (phrase.naturalForm.contains("gonna", ignoreCase = true) && lower.contains("indo para ir")) {
            return "'gonna' é apenas 'vou' ou 'vai', sem redundância."
        }
        if (phrase.naturalForm.contains("make sure", ignoreCase = true) && (lower.contains("fazer certo") || lower.contains("fazer certeza"))) {
            return "'make sure' significa 'garantir' ou 'certificar-se', e não 'fazer certeza'."
        }
        if (phrase.naturalForm.contains("look out", ignoreCase = true) && lower.contains("olhar para fora")) {
            return "'look out' na cena é um aviso de perigo ('cuidado!'), e não literalmente 'olhar para fora'."
        }
        return null
    }

    /**
     * Progressive Hint Generator based on Attempt Level (1 to 4)
     */
    fun getProgressiveHint(
        phrase: ScenePhrase,
        attemptNumber: Int,
        isTranslationStep: Boolean
    ): ProgressiveHint {
        if (!isTranslationStep) {
            // Contraction Step Hints
            val primary = phrase.contractionsUsed.firstOrNull()
            return when (attemptNumber) {
                1 -> {
                    val clue = if (primary != null) "Olhe para a junção: '${primary.fullForm}'." else "Procure pronomes e verbos auxiliares que podem se juntar."
                    ProgressiveHint(
                        level = 1,
                        hintTitle = "Dica 1/3 (Pequena Dica 💡)",
                        hintText = "$clue Como essa expressão soa no inglês falado de cinema?"
                    )
                }
                2 -> {
                    val rule = if (primary != null) {
                        "Substitua '${primary.fullForm}' por '${primary.contractedForm}'."
                    } else {
                        "Lembre-se de adicionar o apóstrofo (') nas palavras encurtadas."
                    }
                    ProgressiveHint(
                        level = 2,
                        hintTitle = "Dica 2/3 (Dica Específica 🔍)",
                        hintText = "$rule O restante da frase continua com as mesmas palavras."
                    )
                }
                3 -> {
                    val words = phrase.naturalForm.split(" ")
                    val partial = words.take(maxOf(2, (words.size * 0.6).toInt())).joinToString(" ") + "..."
                    ProgressiveHint(
                        level = 3,
                        hintTitle = "Dica 3/3 (Parte da Resposta 🧩)",
                        hintText = "Comece assim: \"$partial\"",
                        partialAnswer = partial
                    )
                }
                else -> {
                    ProgressiveHint(
                        level = 4,
                        hintTitle = "Resposta Completa & Regra 🎓",
                        hintText = "Frase contraída: \"${phrase.naturalForm}\"\n${buildContractionExplanation(phrase.contractionsUsed)}"
                    )
                }
            }
        } else {
            // Translation Step Hints
            return when (attemptNumber) {
                1 -> {
                    ProgressiveHint(
                        level = 1,
                        hintTitle = "Dica 1/3 (Contexto 💡)",
                        hintText = "Pense no objetivo do personagem na cena. Qual ideia principal ele quer expressar?"
                    )
                }
                2 -> {
                    ProgressiveHint(
                        level = 2,
                        hintTitle = "Dica 2/3 (Vocabulário-Chave 🔍)",
                        hintText = "Dica de significado: ${phrase.vocabularyNotes.ifBlank { phrase.grammarTip }}"
                    )
                }
                3 -> {
                    val words = phrase.portugueseTranslation.split(" ")
                    val partial = words.take(maxOf(2, (words.size * 0.5).toInt())).joinToString(" ") + "..."
                    ProgressiveHint(
                        level = 3,
                        hintTitle = "Dica 3/3 (Início da Tradução 🧩)",
                        hintText = "Início em português: \"$partial\"",
                        partialAnswer = partial
                    )
                }
                else -> {
                    ProgressiveHint(
                        level = 4,
                        hintTitle = "Tradução Completa 🎓",
                        hintText = "Tradução natural: \"${phrase.portugueseTranslation}\""
                    )
                }
            }
        }
    }

    private fun buildContractionExplanation(pairs: List<ContractionPair>): String {
        if (pairs.isEmpty()) return "Nenhuma contração complexa nesta frase."
        return pairs.joinToString("\n• ") { "• ${it.fullForm} → ${it.contractedForm}: ${it.ruleExplanation}" }
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[s1.length][s2.length]
    }
}

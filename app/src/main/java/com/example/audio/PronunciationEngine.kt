package com.example.audio

import com.example.data.model.PronunciationAnalysisResult
import com.example.data.model.WordPronunciationDetail
import com.example.data.model.WordPronunciationStatus
import kotlin.math.max
import kotlin.math.min

object PronunciationEngine {

    /**
     * Common phonetic and natural spoken equivalences in English
     * to avoid penalizing standard connected speech or minor accent variations.
     */
    private val EQUIVALENT_MAPPINGS = mapOf(
        "gonna" to listOf("going", "to", "going to"),
        "wanna" to listOf("want", "to", "want to"),
        "gotta" to listOf("got", "to", "got to", "have to"),
        "kinda" to listOf("kind", "of", "kind of"),
        "sorta" to listOf("sort", "of", "sort of"),
        "outta" to listOf("out", "of", "out of"),
        "lemme" to listOf("let", "me", "let me"),
        "gimme" to listOf("give", "me", "give me"),
        "whatcha" to listOf("what", "are", "you", "what do you", "what are you"),
        "dunno" to listOf("do", "not", "know", "don't know", "dont know"),
        "ya" to listOf("you", "your"),
        "em" to listOf("them", "'em"),
        "cause" to listOf("because", "'cause", "cuz"),
        "cuz" to listOf("because", "'cause", "cause"),
        "yeah" to listOf("yes", "yep"),
        "nope" to listOf("no")
    )

    private val PHONETIC_SIMILARITIES = mapOf(
        "the" to listOf("da", "tha", "de", "di"),
        "to" to listOf("ta", "tu", "2", "too"),
        "for" to listOf("fer", "four", "4"),
        "and" to listOf("an", "en", "n", "&"),
        "are" to listOf("r", "er"),
        "you" to listOf("u", "yu"),
        "of" to listOf("ov", "uv", "a"),
        "have" to listOf("av", "haf"),
        "can" to listOf("ken", "kan")
    )

    fun analyzePronunciation(
        targetPhrase: String,
        spokenPhrase: String,
        recognitionConfidence: Float = 0.9f
    ): PronunciationAnalysisResult {
        val rawTargetTokens = tokenize(targetPhrase)
        val rawSpokenTokens = tokenize(spokenPhrase)

        if (rawSpokenTokens.isEmpty()) {
            return PronunciationAnalysisResult(
                targetPhrase = targetPhrase,
                spokenPhrase = spokenPhrase,
                scorePercentage = 0,
                fluencyScore = 0,
                accuracyScore = 0,
                words = rawTargetTokens.map {
                    WordPronunciationDetail(
                        word = it,
                        spokenWord = null,
                        status = WordPronunciationStatus.OMITTED,
                        feedback = "Palavra não pronunciada"
                    )
                },
                omittedWords = rawTargetTokens,
                mispronouncedWords = emptyList(),
                extraWords = emptyList(),
                feedbackMessage = "Não captamos sua fala. Toque no microfone e tente falar a frase completa.",
                rhythmAndIntonationTip = "Fale em tom normal, próximo ao microfone, mantendo um ritmo constante.",
                isUnderstood = false
            )
        }

        // Align target words with spoken words
        val wordDetails = mutableListOf<WordPronunciationDetail>()
        val omittedWords = mutableListOf<String>()
        val mispronouncedWords = mutableListOf<Pair<String, String>>()
        val extraWords = mutableListOf<String>()

        var spokenIndex = 0
        var correctMatchesCount = 0
        var closeMatchesCount = 0

        for (i in rawTargetTokens.indices) {
            val target = rawTargetTokens[i]
            val normTarget = normalizeToken(target)

            if (spokenIndex >= rawSpokenTokens.size) {
                // All remaining target words are omitted
                wordDetails.add(
                    WordPronunciationDetail(
                        word = target,
                        spokenWord = null,
                        status = WordPronunciationStatus.OMITTED,
                        feedback = if (i == rawTargetTokens.size - 1) "Você não pronunciou a última palavra." else "Palavra omitida."
                    )
                )
                omittedWords.add(target)
                continue
            }

            val spoken = rawSpokenTokens[spokenIndex]
            val normSpoken = normalizeToken(spoken)

            if (isDirectOrContractedMatch(normTarget, normSpoken)) {
                wordDetails.add(
                    WordPronunciationDetail(
                        word = target,
                        spokenWord = spoken,
                        status = WordPronunciationStatus.CORRECT,
                        feedback = "Pronúncia clara e correta"
                    )
                )
                correctMatchesCount++
                spokenIndex++
            } else if (isFuzzyOrAccentMatch(normTarget, normSpoken)) {
                wordDetails.add(
                    WordPronunciationDetail(
                        word = target,
                        spokenWord = spoken,
                        status = WordPronunciationStatus.CLOSE_ENOUGH,
                        feedback = "Pronúncia compreensível (aceitável para comunicação)"
                    )
                )
                closeMatchesCount++
                spokenIndex++
            } else {
                // Look ahead in spoken tokens to see if word was skipped or mispronounced
                val foundFurtherAhead = (spokenIndex + 1 until min(spokenIndex + 3, rawSpokenTokens.size)).firstOrNull { idx ->
                    isDirectOrContractedMatch(normTarget, normalizeToken(rawSpokenTokens[idx]))
                }

                if (foundFurtherAhead != null) {
                    // Words between spokenIndex and foundFurtherAhead are extra
                    for (k in spokenIndex until foundFurtherAhead) {
                        extraWords.add(rawSpokenTokens[k])
                    }
                    spokenIndex = foundFurtherAhead
                    wordDetails.add(
                        WordPronunciationDetail(
                            word = target,
                            spokenWord = rawSpokenTokens[spokenIndex],
                            status = WordPronunciationStatus.CORRECT,
                            feedback = "Pronunciada corretamente"
                        )
                    )
                    correctMatchesCount++
                    spokenIndex++
                } else {
                    // Check if target is omitted by checking if next target matches current spoken
                    val nextTargetMatches = (i + 1 < rawTargetTokens.size) &&
                            isDirectOrContractedMatch(normalizeToken(rawTargetTokens[i + 1]), normSpoken)

                    if (nextTargetMatches) {
                        wordDetails.add(
                            WordPronunciationDetail(
                                word = target,
                                spokenWord = null,
                                status = WordPronunciationStatus.OMITTED,
                                feedback = "Palavra pulada na fala"
                            )
                        )
                        omittedWords.add(target)
                    } else {
                        // Word was mispronounced or substituted
                        wordDetails.add(
                            WordPronunciationDetail(
                                word = target,
                                spokenWord = spoken,
                                status = WordPronunciationStatus.MISPRONOUNCED,
                                feedback = "Você falou \"$spoken\" em vez de \"$target\""
                            )
                        )
                        mispronouncedWords.add(target to spoken)
                        spokenIndex++
                    }
                }
            }
        }

        // Any trailing spoken words are extra words
        while (spokenIndex < rawSpokenTokens.size) {
            extraWords.add(rawSpokenTokens[spokenIndex])
            spokenIndex++
        }

        // Calculate scores
        val totalTargetWords = max(1, rawTargetTokens.size)
        val rawAccuracy = ((correctMatchesCount * 1.0f + closeMatchesCount * 0.85f) / totalTargetWords.toFloat())
        val omissionPenalty = (omittedWords.size.toFloat() / totalTargetWords.toFloat()) * 0.35f
        val extraPenalty = min(0.15f, extraWords.size * 0.05f)

        val accuracyScore = ((rawAccuracy - extraPenalty) * 100).toInt().coerceIn(0, 100)

        // Fluency score based on completeness and smooth recognition confidence
        val fluencyScore = ((1.0f - omissionPenalty) * recognitionConfidence.coerceIn(0.7f, 1.0f) * 100).toInt().coerceIn(0, 100)

        val finalScore = ((accuracyScore * 0.7f + fluencyScore * 0.3f)).toInt().coerceIn(0, 100)

        // Generate tailored Portuguese feedback
        val feedbackMessage = buildFeedbackMessage(
            finalScore = finalScore,
            omittedWords = omittedWords,
            mispronouncedWords = mispronouncedWords,
            extraWords = extraWords,
            isComplete = omittedWords.isEmpty() && mispronouncedWords.isEmpty()
        )

        val rhythmTip = buildRhythmAndIntonationTip(targetPhrase, omittedWords, mispronouncedWords)

        return PronunciationAnalysisResult(
            targetPhrase = targetPhrase,
            spokenPhrase = spokenPhrase,
            scorePercentage = finalScore,
            fluencyScore = fluencyScore,
            accuracyScore = accuracyScore,
            words = wordDetails,
            omittedWords = omittedWords,
            mispronouncedWords = mispronouncedWords,
            extraWords = extraWords,
            feedbackMessage = feedbackMessage,
            rhythmAndIntonationTip = rhythmTip,
            isUnderstood = finalScore >= 70
        )
    }

    private fun tokenize(text: String): List<String> {
        return text
            .replace("’", "'")
            .replace("“", "")
            .replace("”", "")
            .replace("\"", "")
            .replace(Regex("[.,!?;:()\\[\\]{}]"), " ")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
    }

    private fun normalizeToken(token: String): String {
        return token
            .lowercase()
            .replace("’", "'")
            .replace(Regex("[^a-z0-9']"), "")
            .trim()
    }

    private fun isDirectOrContractedMatch(target: String, spoken: String): Boolean {
        if (target == spoken) return true

        // Check contractions vs uncontracted equivalents
        val targetEquivs = EQUIVALENT_MAPPINGS[target] ?: emptyList()
        if (spoken in targetEquivs) return true

        val spokenEquivs = EQUIVALENT_MAPPINGS[spoken] ?: emptyList()
        if (target in spokenEquivs) return true

        // Check without apostrophe (e.g. "im" vs "i'm", "dont" vs "don't")
        if (target.replace("'", "") == spoken.replace("'", "")) return true

        return false
    }

    private fun isFuzzyOrAccentMatch(target: String, spoken: String): Boolean {
        // Phonetic similarities for function words
        val similarities = PHONETIC_SIMILARITIES[target] ?: emptyList()
        if (spoken in similarities) return true

        val reverseSimilarities = PHONETIC_SIMILARITIES[spoken] ?: emptyList()
        if (target in reverseSimilarities) return true

        // Levenshtein distance for minor accent tolerance (1 edit for short words, 2 for longer)
        val distance = levenshteinDistance(target, spoken)
        val threshold = if (target.length <= 4) 1 else 2
        return distance <= threshold
    }

    private fun levenshteinDistance(a: String, b: String): Int {
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j

        for (i in 1..a.length) {
            for (j in 1..b.length) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // deletion
                    dp[i][j - 1] + 1,      // insertion
                    dp[i - 1][j - 1] + cost // substitution
                )
            }
        }
        return dp[a.length][b.length]
    }

    private fun buildFeedbackMessage(
        finalScore: Int,
        omittedWords: List<String>,
        mispronouncedWords: List<Pair<String, String>>,
        extraWords: List<String>,
        isComplete: Boolean
    ): String {
        if (isComplete || finalScore >= 90) {
            return "Excelente pronúncia! Sua fala foi clara, natural e perfeitamente compreensível."
        }

        val issues = mutableListOf<String>()

        if (omittedWords.isNotEmpty()) {
            if (omittedWords.size == 1) {
                issues.add("Você não pronunciou a palavra \"${omittedWords.first()}\".")
            } else {
                issues.add("Você omitiu as palavras: ${omittedWords.joinToString(", ") { "\"$it\"" }}.")
            }
        }

        if (mispronouncedWords.isNotEmpty()) {
            val examples = mispronouncedWords.take(2).joinToString(", ") { (target, spoken) ->
                "\"$target\" (ouvido como \"$spoken\")"
            }
            issues.add("Pratique a pronúncia de: $examples.")
        }

        if (extraWords.isNotEmpty()) {
            issues.add("Palavras extras detectadas: ${extraWords.take(2).joinToString(", ") { "\"$it\"" }}.")
        }

        return if (issues.isNotEmpty()) {
            issues.joinToString(" ")
        } else if (finalScore >= 75) {
            "Boa pronúncia! Pequenos ajustes de ritmo e clareza vão deixar sua fala ainda mais natural."
        } else {
            "Continue praticando! Escute o áudio modelo e tente repetir em voz alta mantendo a continuidade."
        }
    }

    private fun buildRhythmAndIntonationTip(
        targetPhrase: String,
        omittedWords: List<String>,
        mispronouncedWords: List<Pair<String, String>>
    ): String {
        val clean = targetPhrase.trim()
        return when {
            clean.endsWith("?") ->
                "Dica de Entonação: Perguntas diretas geralmente terminam com entonação ascendente (voz sobe no final ↗️)."
            clean.contains("n't") || clean.contains("'m") || clean.contains("'ll") || clean.contains("'re") ->
                "Dica de Connected Speech: As contrações devem soar como uma única unidade rítmica contínua, sem pausas no meio."
            clean.contains("gonna") || clean.contains("wanna") || clean.contains("gotta") ->
                "Dica de Redução: Essas formas reduzem o som das vogais para o som rápido Schwa /ə/. Mantenha a fala relaxada."
            else ->
                "Dica de Ritmo: No inglês falado, enfatize os substantivos e verbos principais e passe rápido pelas palavras de ligação (to, for, the)."
        }
    }
}

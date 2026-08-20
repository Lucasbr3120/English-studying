package com.example.data.ai

import com.example.data.model.AiCorrectionResult
import com.example.data.model.ContractionPair
import com.example.data.model.ScenePhrase
import java.text.Normalizer
import java.util.Locale

object SemanticMatcher {

    fun normalizeText(text: String): String {
        val trimmed = text.trim().lowercase(Locale.ROOT)
        // Normalize accents (á -> a, ç -> c, etc.)
        val noAccents = Normalizer.normalize(trimmed, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        // Replace common punctuation with spaces
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
            "\\beu vou\\b" to "vou",
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
            "\\bloja\\b" to "supermercado"
        )
        for ((regex, replacement) in replacements) {
            norm = norm.replace(regex.toRegex(), replacement)
        }
        return norm.replace("\\s+".toRegex(), " ").trim()
    }

    fun evaluatePortugueseTranslation(
        userInput: String,
        phrase: ScenePhrase
    ): AiCorrectionResult {
        val userNorm = normalizePortugueseEquivalents(userInput)
        val targetNorm = normalizePortugueseEquivalents(phrase.portugueseTranslation)
        val allAcceptable = (listOf(phrase.portugueseTranslation) + phrase.acceptableTranslations)
            .map { normalizePortugueseEquivalents(it) }

        // 1. Direct match with expected or acceptable list
        if (allAcceptable.contains(userNorm)) {
            return AiCorrectionResult(
                isCorrect = true,
                feedbackTitle = "Excelente Tradução!",
                feedbackMessage = "Você capturou com precisão o sentido natural da frase, respeitando o contexto falado.",
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = 100
            )
        }

        // 2. Token overlap similarity
        val userTokens = userNorm.split(" ").filter { it.isNotBlank() }.toSet()
        var maxMatchRatio = 0.0

        for (target in allAcceptable) {
            val targetTokens = target.split(" ").filter { it.isNotBlank() }.toSet()
            val intersection = userTokens.intersect(targetTokens).size
            val union = userTokens.union(targetTokens).size
            val ratio = if (union > 0) intersection.toDouble() / union.toDouble() else 0.0
            if (ratio > maxMatchRatio) {
                maxMatchRatio = ratio
            }
        }

        // Levenshtein distance check on best match
        val bestTarget = allAcceptable.firstOrNull() ?: targetNorm
        val distance = levenshteinDistance(userNorm, bestTarget)
        val maxLen = maxOf(userNorm.length, bestTarget.length)
        val stringSimilarity = if (maxLen > 0) 1.0 - (distance.toDouble() / maxLen.toDouble()) else 0.0

        val overallSimilarity = maxOf(maxMatchRatio, stringSimilarity)

        return if (overallSimilarity >= 0.65 || (userTokens.size >= 3 && maxMatchRatio >= 0.55)) {
            AiCorrectionResult(
                isCorrect = true,
                feedbackTitle = "Muito Bom!",
                feedbackMessage = "Tradução aceita! O significado principal foi transmitido corretamente.",
                suggestedImprovement = "Outra forma comum no dia a dia: \"${phrase.portugueseTranslation}\"",
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = (overallSimilarity * 100).toInt().coerceIn(75, 100)
            )
        } else {
            AiCorrectionResult(
                isCorrect = false,
                feedbackTitle = "Quase lá!",
                feedbackMessage = "Sua tradução expressou parte do sentido, mas alguns detalhes essenciais da fala ficaram diferentes.",
                suggestedImprovement = "Tradução esperada: \"${phrase.portugueseTranslation}\"",
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = (overallSimilarity * 100).toInt().coerceIn(20, 60)
            )
        }
    }

    fun evaluateContractionExercise(
        userInput: String,
        phrase: ScenePhrase
    ): AiCorrectionResult {
        val userClean = userInput.trim().replace("\\s+".toRegex(), " ")
        val targetClean = phrase.naturalForm.trim().replace("\\s+".toRegex(), " ")

        // Check exact match (ignoring case)
        if (userClean.equals(targetClean, ignoreCase = true)) {
            val contractionsExplanation = buildContractionExplanation(phrase.contractionsUsed)
            return AiCorrectionResult(
                isCorrect = true,
                feedbackTitle = "Perfeito!",
                feedbackMessage = "Você aplicou todas as contrações naturais da cena com perfeição.",
                contractionAnalysis = contractionsExplanation,
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = 100
            )
        }

        // Check if user still typed uncontracted words
        val missingContractions = mutableListOf<ContractionPair>()
        val lowerInput = userClean.lowercase(Locale.ROOT)

        for (pair in phrase.contractionsUsed) {
            if (lowerInput.contains(pair.fullForm.lowercase(Locale.ROOT))) {
                missingContractions.add(pair)
            }
        }

        val distance = levenshteinDistance(userClean.lowercase(Locale.ROOT), targetClean.lowercase(Locale.ROOT))
        val isMinorTypo = distance in 1..2

        if (missingContractions.isNotEmpty()) {
            val missingText = missingContractions.joinToString(", ") { "\"${it.fullForm}\" → \"${it.contractedForm}\"" }
            return AiCorrectionResult(
                isCorrect = false,
                feedbackTitle = "Quase! Faltou contrair",
                feedbackMessage = "Você deixou algumas palavras na forma longa. Lembre-se de transformar: $missingText.",
                suggestedImprovement = "Forma natural esperada: \"${phrase.naturalForm}\"",
                contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = 50
            )
        }

        if (isMinorTypo) {
            return AiCorrectionResult(
                isCorrect = true,
                feedbackTitle = "Correto (com pequeno detalhe)",
                feedbackMessage = "Você usou as contrações certas! Houve apenas um pequeno desvio de digitação.",
                suggestedImprovement = "Forma exata: \"${phrase.naturalForm}\"",
                contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
                grammarExplanation = phrase.grammarTip,
                additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
                scorePercentage = 90
            )
        }

        return AiCorrectionResult(
            isCorrect = false,
            feedbackTitle = "Vamos revisar!",
            feedbackMessage = "A frase contraída esperada para esta cena é diferente.",
            suggestedImprovement = "Resposta esperada: \"${phrase.naturalForm}\"",
            contractionAnalysis = buildContractionExplanation(phrase.contractionsUsed),
            grammarExplanation = phrase.grammarTip,
            additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
            scorePercentage = 40
        )
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

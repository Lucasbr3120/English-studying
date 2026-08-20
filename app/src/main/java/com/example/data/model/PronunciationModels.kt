package com.example.data.model

enum class WordPronunciationStatus {
    CORRECT,        // Spoken clearly and accurately (Green)
    CLOSE_ENOUGH,   // Understandable with minor accent or variation (Yellow-Green)
    MISPRONOUNCED,  // Pronounced incorrectly / substituted (Orange-Red)
    OMITTED,        // Skipped or missing (Red)
    EXTRA           // Added extra word (Muted/Purple)
}

data class WordPronunciationDetail(
    val word: String,
    val spokenWord: String? = null,
    val status: WordPronunciationStatus,
    val feedback: String? = null,
    val phoneticTip: String? = null
)

data class PronunciationAnalysisResult(
    val targetPhrase: String,
    val spokenPhrase: String,
    val scorePercentage: Int,
    val fluencyScore: Int,
    val accuracyScore: Int,
    val words: List<WordPronunciationDetail>,
    val omittedWords: List<String>,
    val mispronouncedWords: List<Pair<String, String>>, // Target -> Spoken
    val extraWords: List<String>,
    val feedbackMessage: String,
    val rhythmAndIntonationTip: String,
    val isUnderstood: Boolean
)

enum class PronunciationCategory(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String
) {
    CONTRACTIONS(
        id = "contractions",
        title = "Contrações",
        description = "Fluidez e agilidade nas formas contraídas",
        iconName = "FlashOn"
    ),
    CONNECTED_SPEECH(
        id = "connected_speech",
        title = "Connected Speech",
        description = "Junção natural de palavras e sons contínuos",
        iconName = "AutoAwesome"
    ),
    WORD_REDUCTION(
        id = "word_reduction",
        title = "Redução de Palavras",
        description = "Uso correto do Schwa /ə/ e palavras funcionais fracas",
        iconName = "Speed"
    ),
    RHYTHM(
        id = "rhythm",
        title = "Ritmo & Stress",
        description = "Destaque nas palavras de conteúdo e cadência inglesa",
        iconName = "Equalizer"
    ),
    INTONATION(
        id = "intonation",
        title = "Entonação & Melodia",
        description = "Tons ascendentes e descendentes de acordo com a intenção",
        iconName = "GraphicEq"
    );

    val displayName: String get() = title
    val icon: String get() = when(this) {
        CONTRACTIONS -> "⚡"
        CONNECTED_SPEECH -> "✨"
        WORD_REDUCTION -> "🔉"
        RHYTHM -> "🥁"
        INTONATION -> "🎵"
    }
}

data class PronunciationExerciseItem(
    val id: String,
    val category: PronunciationCategory,
    val title: String,
    val targetPhrase: String,
    val naturalSpokenForm: String,
    val portugueseTranslation: String,
    val focusConcept: String,
    val explanation: String,
    val pronunciationTip: String,
    val difficulty: CefrLevel = CefrLevel.A2
)

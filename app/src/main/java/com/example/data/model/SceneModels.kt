package com.example.data.model

enum class CefrLevel(val code: String, val title: String, val description: String) {
    A1("A1", "Iniciante", "Frases simples e vocabulário básico do dia a dia."),
    A2("A2", "Básico+", "Frases maiores e contrações comuns do cotidiano."),
    B1("B1", "Intermediário", "Expressões naturais, phrasal verbs e ritmo de filmes."),
    B2("B2", "Intermediário+", "Inglês informal, expressões idiomáticas e estruturas ágeis."),
    C1("C1", "Avançado", "Diálogos rápidos, gírias moderadas, nuances e fala conectada.");

    companion object {
        fun fromCode(code: String): CefrLevel = values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: A1
    }
}

enum class SceneCategory(
    val id: String,
    val titlePt: String,
    val titleEn: String,
    val description: String,
    val iconTag: String
) {
    COTIDIANO("cotidiano", "Cotidiano", "Daily Life", "Rotina, casa, compras e pequenos momentos do dia a dia", "home"),
    ESCOLA("escola", "Escola & Estudos", "School & College", "Sala de aula, trabalhos em grupo, professores e vida acadêmica", "school"),
    TRABALHO("trabalho", "Trabalho & Negócios", "Work & Business", "Escritório, reuniões de equipe, feedbacks e propostas", "work"),
    RESTAURANTE("restaurante", "Restaurante & Café", "Restaurant & Dining", "Pedidos, mesas, pedidos especiais e conversas à mesa", "restaurant"),
    VIAGEM("viagem", "Viagem & Turismo", "Travel & Tourism", "Passeios, hotéis, direções e aventuras no exterior", "flight"),
    AEROPORTO("aeroporto", "Aeroporto & Imigração", "Airport & Flights", "Check-in, controle de passaporte, conexões e embarque", "local_airport"),
    RELACIONAMENTO("relacionamento", "Relacionamento & Romance", "Dating & Romance", "Encontros, declarações sinceras, conflitos e desabafos", "favorite"),
    AMIZADE("amizade", "Amizade & Social", "Friendship & Social", "Conversas leves, segredos, encontros casuais e apoio mútuo", "groups"),
    INVESTIGACAO("investigacao", "Investigação & Mistério", "Mystery & Crime", "Detetives, pistas, interrogatórios e revelações surpreendentes", "search"),
    ACAO("acao", "Ação & Perseguição", "Action & Thriller", "Planos arriscados, fugas, adrenalina e decisões sob pressão", "sports_kabaddi"),
    COMEDIA("comedia", "Comédia & Sitcom", "Comedy & Sitcom", "Situações hilárias, mal-entendidos e humor natural", "sentiment_very_satisfied"),
    DRAMA("drama", "Drama & Cinema", "Drama & Emotional", "Momentos intensos, despedidas e escolhas difíceis da vida", "theater_comedy"),
    TECNOLOGIA("tecnologia", "Tecnologia & Startups", "Tech & Innovation", "Projetos tech, bugs no sistema, reuniões de produto e IA", "devices");

    companion object {
        fun fromId(id: String): SceneCategory = values().firstOrNull { it.id.equals(id, ignoreCase = true) } ?: COTIDIANO
    }
}

enum class ExerciseType(val title: String) {
    CONTRACTION("Transforme para a forma contraída"),
    TRANSLATION("Traduza para português"),
    FILL_BLANK("Complete a frase"),
    MEANING_QUIZ("Escolha o significado correto")
}

data class ContractionPair(
    val fullForm: String,
    val contractedForm: String,
    val ruleExplanation: String,
    val whenToAvoid: String = "Evite em redações formais, contratos legais ou documentos acadêmicos."
)

data class SceneMediaConfig(
    val audioUrl: String? = null,
    val videoUrl: String? = null,
    val videoThumbnailUrl: String? = null,
    val durationSeconds: Int = 0,
    val isLicensedMediaAvailable: Boolean = false,
    val licenseNotice: String = "Arquitetura preparada para áudio e vídeo sob licença"
)

data class ScenePhrase(
    val id: String,
    val characterName: String,
    val fullForm: String,
    val naturalForm: String,
    val portugueseTranslation: String,
    val acceptableTranslations: List<String> = emptyList(),
    val contractionsUsed: List<ContractionPair> = emptyList(),
    val vocabularyNotes: String,
    val grammarTip: String,
    val additionalExample: String = "",
    val additionalExampleTranslation: String = "",
    // For Fill Blank exercise:
    val blankSentence: String = "",
    val blankCorrectAnswer: String = "",
    val blankOptions: List<String> = emptyList(),
    // For Meaning Quiz:
    val quizQuestion: String = "",
    val quizCorrectAnswer: String = "",
    val quizOptions: List<String> = emptyList(),
    val quizExplanation: String = ""
)

data class Scene(
    val id: String,
    val title: String,
    val category: SceneCategory = SceneCategory.COTIDIANO,
    val level: CefrLevel,
    val durationMinutes: Int = 4,
    val difficultyStars: Int = 3,
    val contextDescription: String,
    val characters: List<String> = emptyList(),
    val genre: String,
    val imageResName: String? = null,
    val mainVocabulary: List<String> = emptyList(),
    val expressions: List<String> = emptyList(),
    val phrases: List<ScenePhrase>,
    val mediaConfig: SceneMediaConfig? = null
)

data class AiCorrectionResult(
    val isCorrect: Boolean,
    val feedbackTitle: String,
    val feedbackMessage: String,
    val contractionAnalysis: String? = null,
    val grammarExplanation: String? = null,
    val additionalExample: String? = null,
    val suggestedImprovement: String? = null,
    val scorePercentage: Int = 100
)

package com.example.data.local

import com.example.data.model.CefrLevel
import com.example.data.model.ContractionPair
import com.example.data.model.YouTubeCategory
import com.example.data.model.YouTubeStudyPhrase
import com.example.data.model.YouTubeStudySet
import com.example.data.model.YouTubeVideoItem
import com.example.data.model.YouTubeVocabularyItem

object PrepopulatedYouTubeStudy {

    val curatedVideos: List<YouTubeVideoItem> = listOf(
        YouTubeVideoItem(
            id = "wJ_lX48z5jE",
            title = "Everyday English: Ordering Coffee & Morning Routines",
            channelTitle = "Natural English Bites",
            description = "Learn natural spoken English contractions and reductions at a coffee shop.",
            thumbnailUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=600&auto=format&fit=crop&q=80",
            durationFormatted = "4:12",
            durationSeconds = 252,
            suggestedLevel = CefrLevel.A2,
            hasClosedCaptions = true,
            isEmbeddable = true,
            license = "creativeCommon",
            publishedAt = "2024-03-10",
            category = YouTubeCategory.DAILY_LIFE,
            authorizedStudySet = YouTubeStudySet(
                videoId = "wJ_lX48z5jE",
                topicSummary = "Conversa em cafeteria pedindo café e combinando tarefas matinais.",
                sourceAttribution = "Creative Commons Educational Dialogue Series",
                phrases = listOf(
                    YouTubeStudyPhrase(
                        id = "yt_c1_p1",
                        fullForm = "I am going to call you later when I finish this coffee.",
                        contractedForm = "I'm going to call you later when I finish this coffee.",
                        portugueseTranslation = "Eu vou ligar para você mais tarde quando terminar este café.",
                        acceptableTranslations = listOf(
                            "Vou te ligar mais tarde quando terminar esse café.",
                            "Eu vou te ligar depois que terminar este café."
                        ),
                        informalSpokenForm = "I'm gonna call you later when I finish this coffee.",
                        contractionsList = listOf(
                            ContractionPair("I am", "I'm", "'I am' vira 'I'm'."),
                            ContractionPair("going to", "gonna", "'going to' é reduzido para 'gonna' na fala rápida e informal.")
                        ),
                        vocabularyNotes = listOf(
                            YouTubeVocabularyItem("later", "em um momento posterior", "mais tarde / depois", "I'll see you later.", CefrLevel.A1, false),
                            YouTubeVocabularyItem("finish", "completar ou consumir até o fim", "terminar / finalizar", "Finish your coffee.", CefrLevel.A1, false),
                            YouTubeVocabularyItem("gonna", "forma falada informal de 'going to'", "vou / vai (futuro)", "I'm gonna call you.", CefrLevel.A2, true)
                        ),
                        grammarExplanation = "A contração de 'I am' para 'I\'m' é universal. Na linguagem falada cotidiana, 'going to' é frequentemente pronunciado como 'gonna'.",
                        spokenTip = "Junte os sons: 'I'm gonna' soa quase como uma única palavra rítmica.",
                        comprehensionQuestion = "Who is the speaker going to call?",
                        comprehensionCorrectAnswer = "You (the listener) later",
                        comprehensionOptions = listOf(
                            "You (the listener) later",
                            "The barista right now",
                            "Their boss tomorrow",
                            "Nobody"
                        )
                    ),
                    YouTubeStudyPhrase(
                        id = "yt_c1_p2",
                        fullForm = "Do not worry, I will not forget to bring the documents.",
                        contractedForm = "Don't worry, I won't forget to bring the documents.",
                        portugueseTranslation = "Não se preocupe, eu não vou esquecer de trazer os documentos.",
                        acceptableTranslations = listOf(
                            "Não se preocupe, não vou esquecer de levar os documentos.",
                            "Fica tranquilo, não esquecerei de trazer os documentos."
                        ),
                        informalSpokenForm = "Don't worry, I won't forget to bring the docs.",
                        contractionsList = listOf(
                            ContractionPair("Do not", "Don't", "'Do not' contrai para 'Don't' no imperativo negativo."),
                            ContractionPair("will not", "won't", "'will not' transforma-se na forma irregular 'won't'.")
                        ),
                        vocabularyNotes = listOf(
                            YouTubeVocabularyItem("worry", "sentir ansiedade ou preocupação", "preocupar-se", "Don't worry about it.", CefrLevel.A1, false),
                            YouTubeVocabularyItem("forget", "não lembrar de algo", "esquecer", "Don't forget your keys.", CefrLevel.A2, false),
                            YouTubeVocabularyItem("bring", "transportar algo para onde você está", "trazer", "Can you bring water?", CefrLevel.A1, false)
                        ),
                        grammarExplanation = "'Will not' vira 'won't'. É uma das contrações irregulares mais importantes do inglês.",
                        spokenTip = "Pronuncie 'won't' com som fechado de /oʊ/ para não confundir com 'want'.",
                        comprehensionQuestion = "What will the speaker NOT forget to do?",
                        comprehensionCorrectAnswer = "Bring the documents",
                        comprehensionOptions = listOf(
                            "Bring the documents",
                            "Pay for the coffee",
                            "Order another drink",
                            "Leave the office"
                        )
                    ),
                    YouTubeStudyPhrase(
                        id = "yt_c1_p3",
                        fullForm = "We have got to leave now because we want to catch the train.",
                        contractedForm = "We've got to leave now because we want to catch the train.",
                        portugueseTranslation = "Nós temos que sair agora porque queremos pegar o trem.",
                        acceptableTranslations = listOf(
                            "Temos que ir agora porque queremos pegar o trem.",
                            "A gente tem que sair já porque quer pegar o trem."
                        ),
                        informalSpokenForm = "We gotta leave now 'cause we wanna catch the train.",
                        contractionsList = listOf(
                            ContractionPair("We have", "We've", "'We have' vira 'We've'."),
                            ContractionPair("got to", "gotta", "'got to' é reduzido informalmente para 'gotta'."),
                            ContractionPair("want to", "wanna", "'want to' é reduzido para 'wanna'.")
                        ),
                        vocabularyNotes = listOf(
                            YouTubeVocabularyItem("gotta", "forma informal de 'have got to' / ter que", "ter que / precisar", "I gotta go.", CefrLevel.A2, true),
                            YouTubeVocabularyItem("wanna", "forma informal de 'want to'", "querer", "Do you wanna come?", CefrLevel.A2, true),
                            YouTubeVocabularyItem("catch", "alcançar um transporte a tempo", "pegar (o trem/ônibus)", "Catch the train.", CefrLevel.A2, false)
                        ),
                        grammarExplanation = "'Gotta' (got to) e 'wanna' (want to) são essenciais no inglês falado de filmes e conversas casuais.",
                        spokenTip = "O 't' em 'gotta' e o 't' em 'wanna' sofrem flap/elipse nos Estados Unidos.",
                        comprehensionQuestion = "Why do they need to leave immediately?",
                        comprehensionCorrectAnswer = "To catch the train",
                        comprehensionOptions = listOf(
                            "To catch the train",
                            "To go to sleep",
                            "Because the coffee shop is closed",
                            "To meet a doctor"
                        )
                    )
                )
            )
        ),
        YouTubeVideoItem(
            id = "eI4an8aSXz8",
            title = "Airport Arrival & Customs Quick Guide",
            channelTitle = "Global Travelers English",
            description = "Essential spoken phrases for passing through customs, immigration and baggage claim.",
            thumbnailUrl = "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=600&auto=format&fit=crop&q=80",
            durationFormatted = "5:30",
            durationSeconds = 330,
            suggestedLevel = CefrLevel.B1,
            hasClosedCaptions = true,
            isEmbeddable = true,
            license = "creativeCommon",
            publishedAt = "2024-02-18",
            category = YouTubeCategory.TRAVEL,
            authorizedStudySet = YouTubeStudySet(
                videoId = "eI4an8aSXz8",
                topicSummary = "Diálogo na imigração e retirada de bagagem de aeroporto internacional.",
                sourceAttribution = "AeroEnglish Educational Commons",
                phrases = listOf(
                    YouTubeStudyPhrase(
                        id = "yt_tr_p1",
                        fullForm = "I cannot find my luggage on the carousel and I would like some assistance.",
                        contractedForm = "I can't find my luggage on the carousel and I'd like some assistance.",
                        portugueseTranslation = "Eu não consigo encontrar minha bagagem na esteira e gostaria de ajuda.",
                        acceptableTranslations = listOf(
                            "Não estou achando minha mala na esteira e gostaria de uma ajuda.",
                            "Não consigo achar minha bagagem na esteira e queria assistência."
                        ),
                        informalSpokenForm = "I can't find my bags on the carousel and I'd like some help.",
                        contractionsList = listOf(
                            ContractionPair("cannot", "can't", "'cannot' vira 'can't'."),
                            ContractionPair("I would", "I'd", "'I would like' contrai elegantemente para 'I'd like'.")
                        ),
                        vocabularyNotes = listOf(
                            YouTubeVocabularyItem("luggage", "malas e pertences de viagem", "bagagem / malas", "Heavy luggage.", CefrLevel.B1, false),
                            YouTubeVocabularyItem("carousel", "esteira giratória de bagagens", "esteira de bagagem", "Baggage carousel 4.", CefrLevel.B1, false),
                            YouTubeVocabularyItem("assistance", "termo polido para apoio ou socorro", "assistência / ajuda", "Need assistance?", CefrLevel.B1, false)
                        ),
                        grammarExplanation = "'I'd like' é muito mais natural e cortês do que 'I want'.",
                        spokenTip = "O 'd' de 'I'd' se conecta suavemente com 'like': /aɪd laɪk/.",
                        comprehensionQuestion = "What problem is the passenger facing?",
                        comprehensionCorrectAnswer = "They cannot find their luggage",
                        comprehensionOptions = listOf(
                            "They cannot find their luggage",
                            "They lost their passport",
                            "Their flight was cancelled",
                            "They missed their gate"
                        )
                    ),
                    YouTubeStudyPhrase(
                        id = "yt_tr_p2",
                        fullForm = "You should not leave your bags unattended at any moment.",
                        contractedForm = "You shouldn't leave your bags unattended at any moment.",
                        portugueseTranslation = "Você não deve deixar suas malas desacompanhadas em nenhum momento.",
                        acceptableTranslations = listOf(
                            "Você não deveria deixar suas bolsas sozinhas em momento algum.",
                            "Não deixe suas malas sem vigilância em nenhum momento."
                        ),
                        informalSpokenForm = "You shouldn't leave your stuff unattended.",
                        contractionsList = listOf(
                            ContractionPair("should not", "shouldn't", "'should not' contrai para 'shouldn't' com som suave de 't'.")
                        ),
                        vocabularyNotes = listOf(
                            YouTubeVocabularyItem("unattended", "sem ninguém cuidando por perto", "desacompanhado / sem vigilância", "Unattended luggage.", CefrLevel.B1, false),
                            YouTubeVocabularyItem("moment", "instante de tempo", "momento", "At any moment.", CefrLevel.A2, false)
                        ),
                        grammarExplanation = "'Shouldn't' expressa forte recomendação ou advertência de segurança.",
                        spokenTip = "Em 'shouldn't leave', o 't' final quase não é solto antes do 'l'.",
                        comprehensionQuestion = "What safety rule is being emphasized?",
                        comprehensionCorrectAnswer = "Never leave bags unattended",
                        comprehensionOptions = listOf(
                            "Never leave bags unattended",
                            "Always check in 5 hours early",
                            "Do not bring liquids on the train",
                            "Turn off phones on the runway"
                        )
                    )
                )
            )
        ),
        YouTubeVideoItem(
            id = "ZXsQAXx_ao0",
            title = "Job Interview Mastery: Confident Answers",
            channelTitle = "Career Spoken English",
            description = "How to sound natural, professional and fluent in workplace English interviews.",
            thumbnailUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=600&auto=format&fit=crop&q=80",
            durationFormatted = "6:45",
            durationSeconds = 405,
            suggestedLevel = CefrLevel.B2,
            hasClosedCaptions = true,
            isEmbeddable = true,
            license = "creativeCommon",
            publishedAt = "2024-01-22",
            category = YouTubeCategory.WORK,
            authorizedStudySet = YouTubeStudySet(
                videoId = "ZXsQAXx_ao0",
                topicSummary = "Respostas estratégicas em entrevista de emprego sobre liderança e resolução de problemas.",
                sourceAttribution = "Workplace English Pro Project",
                phrases = listOf(
                    YouTubeStudyPhrase(
                        id = "yt_wk_p1",
                        fullForm = "I have been working with international teams and I did not hesitate to take initiative.",
                        contractedForm = "I've been working with international teams and I didn't hesitate to take initiative.",
                        portugueseTranslation = "Eu tenho trabalhado com equipes internacionais e não hesitei em tomar iniciativa.",
                        acceptableTranslations = listOf(
                            "Venho trabalhando com times internacionais e não tive receio de tomar a iniciativa.",
                            "Trabalho com times do mundo todo e não hesitei em tomar frente."
                        ),
                        informalSpokenForm = "I've been working with global teams and didn't hesitate to step up.",
                        contractionsList = listOf(
                            ContractionPair("I have", "I've", "'I have' contrai para 'I've'."),
                            ContractionPair("did not", "didn't", "'did not' contrai para 'didn't'.")
                        ),
                        vocabularyNotes = listOf(
                            YouTubeVocabularyItem("hesitate", "pausar por dúvida ou incerteza", "hesitar / titubear", "Don't hesitate to ask.", CefrLevel.B2, false),
                            YouTubeVocabularyItem("initiative", "capacidade de agir antes dos outros", "iniciativa", "Show initiative.", CefrLevel.B2, false),
                            YouTubeVocabularyItem("step up", "assumir responsabilidade adicional", "dar um passo à frente / assumir a liderança", "Step up to the challenge.", CefrLevel.B2, true)
                        ),
                        grammarExplanation = "Present Perfect Continuous ('I've been working') conecta ações do passado recente com o momento presente.",
                        spokenTip = "'Didn't hesitate' soa como /dɪdnt ˈhɛzɪteɪt/ com ênfase na primeira sílaba de hesitate.",
                        comprehensionQuestion = "What quality does the candidate demonstrate?",
                        comprehensionCorrectAnswer = "Taking initiative and collaborating with international teams",
                        comprehensionOptions = listOf(
                            "Taking initiative and collaborating with international teams",
                            "Avoiding new responsibilities",
                            "Working only on individual tasks",
                            "Hesitating during difficult decisions"
                        )
                    )
                )
            )
        )
    )

    fun getStudySetForVideo(videoId: String): YouTubeStudySet? {
        return curatedVideos.firstOrNull { it.id == videoId }?.authorizedStudySet
    }
}

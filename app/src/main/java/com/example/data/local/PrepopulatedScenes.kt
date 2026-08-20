package com.example.data.local

import com.example.data.model.CefrLevel
import com.example.data.model.ContractionPair
import com.example.data.model.Scene
import com.example.data.model.SceneCategory
import com.example.data.model.SceneMediaConfig
import com.example.data.model.ScenePhrase

object PrepopulatedScenes {
    val allScenes: List<Scene> = listOf(
        // ================= 1. COTIDIANO (A1) =================
        Scene(
            id = "scene_cotidiano_coffee",
            title = "Morning Coffee Rush",
            category = SceneCategory.COTIDIANO,
            level = CefrLevel.A1,
            durationMinutes = 3,
            difficultyStars = 1,
            contextDescription = "Um cliente apressado chega a uma cafeteria movimentada em Nova York logo pela manhã precisando de cafeína.",
            characters = listOf("Lucas", "Barista Sarah"),
            genre = "Cotidiano / Comédia Leve",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("order", "black coffee", "lifesaver", "large cup", "sleep well"),
            expressions = listOf("You're a lifesaver", "I'll take...", "Have a nice day"),
            mediaConfig = SceneMediaConfig(
                durationSeconds = 180,
                isLicensedMediaAvailable = false,
                licenseNotice = "Áudio e vídeo preparados para conteúdo de estúdio parceiro"
            ),
            phrases = listOf(
                ScenePhrase(
                    id = "cotidiano_1_p1",
                    characterName = "Lucas",
                    fullForm = "I am very tired today because I did not sleep well.",
                    naturalForm = "I'm very tired today because I didn't sleep well.",
                    portugueseTranslation = "Eu estou muito cansado hoje porque não dormi bem.",
                    acceptableTranslations = listOf(
                        "Estou muito cansado hoje porque não dormi bem.",
                        "Tô muito cansado hoje porque não dormi bem.",
                        "Estou muito cansada hoje pois não dormi bem."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I am", "I'm", "'I am' vira 'I'm'. O 'a' é substituído pelo apóstrofo para fala rápida."),
                        ContractionPair("did not", "didn't", "'did not' é o passado negativo e vira 'didn't'.")
                    ),
                    vocabularyNotes = "'tired' = cansado(a). 'sleep well' = dormir bem.",
                    grammarTip = "O 'didn't' já indica o passado, logo o verbo 'sleep' permanece na forma base.",
                    additionalExample = "I'm sure that you didn't forget your keys.",
                    additionalExampleTranslation = "Tenho certeza de que você não esqueceu suas chaves.",
                    blankSentence = "I'm very tired today because I ______ sleep well.",
                    blankCorrectAnswer = "didn't",
                    blankOptions = listOf("didn't", "don't", "wasn't", "haven't"),
                    quizQuestion = "Qual a função de 'didn't' nesta frase?",
                    quizCorrectAnswer = "Indicar que a ação negativa ocorreu no passado",
                    quizOptions = listOf(
                        "Indicar que a ação negativa ocorreu no passado",
                        "Indicar um hábito no presente",
                        "Indicar uma certeza no futuro",
                        "Indicar uma sugestão educada"
                    ),
                    quizExplanation = "'Did' é o auxiliar do passado simples para perguntas e negativas."
                ),
                ScenePhrase(
                    id = "cotidiano_1_p2",
                    characterName = "Barista Sarah",
                    fullForm = "It is not a problem, we have extra strong coffee ready.",
                    naturalForm = "It's not a problem, we've got extra strong coffee ready.",
                    portugueseTranslation = "Não é um problema, nós temos café extra forte pronto.",
                    acceptableTranslations = listOf(
                        "Não tem problema, temos café extra forte pronto.",
                        "Não é problema, a gente tem café super forte pronto.",
                        "Sem problemas, nós temos café forte prontinho."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("It is", "It's", "'It is' vira 'It's'."),
                        ContractionPair("we have", "we've", "'we have got' vira 'we've got' no inglês falado.")
                    ),
                    vocabularyNotes = "'ready' = pronto/preparado. 'extra strong' = super forte.",
                    grammarTip = "'We've got' é a forma mais comum e coloquial no dia a dia para expressar posse.",
                    additionalExample = "It's not cold inside the shop.",
                    additionalExampleTranslation = "Não está frio dentro da loja.",
                    blankSentence = "______ not a problem at all.",
                    blankCorrectAnswer = "It's",
                    blankOptions = listOf("It's", "Its", "Is", "He's"),
                    quizQuestion = "O que significa 'It's' com apóstrofo?",
                    quizCorrectAnswer = "Contração de 'It is' ou 'It has'",
                    quizOptions = listOf(
                        "Contração de 'It is' ou 'It has'",
                        "Pronome possessivo dele/dela",
                        "Plural de 'it'",
                        "Abreviação de 'item'"
                    ),
                    quizExplanation = "'It's' com apóstrofo é contração verbal."
                ),
                ScenePhrase(
                    id = "cotidiano_1_p3",
                    characterName = "Lucas",
                    fullForm = "You are a lifesaver, I will take a large cup.",
                    naturalForm = "You're a lifesaver, I'll take a large cup.",
                    portugueseTranslation = "Você salvou minha vida, eu vou querer um copo grande.",
                    acceptableTranslations = listOf(
                        "Você salvou meu dia, vou querer um copo grande.",
                        "Você é um salva-vidas, vou levar um copo grande.",
                        "Você salvou minha pele, vou pegar um copo grande."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("You are", "You're", "'You are' vira 'You're'."),
                        ContractionPair("I will", "I'll", "'I will' vira 'I'll' para decisões no momento.")
                    ),
                    vocabularyNotes = "'lifesaver' = salvador da pátria, pessoa que ajuda muito.",
                    grammarTip = "'I'll take...' é a maneira mais autêntica de fazer pedidos em cafés americanos.",
                    additionalExample = "You're awesome, I'll recommend this place.",
                    additionalExampleTranslation = "Você é incrível, vou recomendar este lugar.",
                    blankSentence = "You're a lifesaver, ______ take a large one.",
                    blankCorrectAnswer = "I'll",
                    blankOptions = listOf("I'll", "I'd", "I'm", "I've"),
                    quizQuestion = "O que expressa 'You're a lifesaver'?",
                    quizCorrectAnswer = "Gratidão calorosa por uma ajuda muito oportuna",
                    quizOptions = listOf(
                        "Gratidão calorosa por uma ajuda muito oportuna",
                        "Profissão de salva-vidas na praia",
                        "Pedido de socorro médico",
                        "Crítica ao atendimento"
                    ),
                    quizExplanation = "É um elogio informal muito popular em filmes e séries."
                )
            )
        ),

        // ================= 2. COTIDIANO (A2) =================
        Scene(
            id = "scene_cotidiano_fridge",
            title = "Empty Fridge Dilemma",
            category = SceneCategory.COTIDIANO,
            level = CefrLevel.A2,
            durationMinutes = 4,
            difficultyStars = 2,
            contextDescription = "Dois amigos abrem a geladeira no domingo à noite e percebem que não têm nada para o jantar.",
            characters = listOf("Emma", "Mark"),
            genre = "Cotidiano / Sitcom",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("grocery store", "fridge", "dinner", "write down", "forget"),
            expressions = listOf("Don't worry", "Write it down", "Run out of"),
            mediaConfig = SceneMediaConfig(durationSeconds = 240),
            phrases = listOf(
                ScenePhrase(
                    id = "cotidiano_2_p1",
                    characterName = "Emma",
                    fullForm = "I am going to the store because I do not have any food.",
                    naturalForm = "I'm going to the store because I don't have any food.",
                    portugueseTranslation = "Eu vou ao mercado porque não tenho comida.",
                    acceptableTranslations = listOf(
                        "Vou ao mercado porque não tenho comida.",
                        "Estou indo ao mercado porque não tenho nada de comida.",
                        "Eu tô indo ao mercado porque não tenho comida nenhuma."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I am", "I'm", "Redução de 'I am' para 'I'm'."),
                        ContractionPair("do not", "don't", "'do not' vira 'don't' no presente simples.")
                    ),
                    vocabularyNotes = "'store' = supermercado/mercearia. 'any food' = comida alguma.",
                    grammarTip = "'Any' é obrigatório com 'don't have' para enfatizar a ausência.",
                    additionalExample = "I'm going out because I don't want to stay home.",
                    additionalExampleTranslation = "Estou saindo porque não quero ficar em casa.",
                    blankSentence = "I'm going to the store because I ______ have any food.",
                    blankCorrectAnswer = "don't",
                    blankOptions = listOf("don't", "didn't", "won't", "haven't"),
                    quizQuestion = "Por que usamos 'don't' e não 'didn't'?",
                    quizCorrectAnswer = "Porque expressa o estado presente da geladeira",
                    quizOptions = listOf(
                        "Porque expressa o estado presente da geladeira",
                        "Porque 'have' não aceita passado",
                        "Porque é uma promessa futura",
                        "Por se tratar de uma pergunta"
                    ),
                    quizExplanation = "'Don't' é o auxiliar do presente para I/You/We/They."
                ),
                ScenePhrase(
                    id = "cotidiano_2_p2",
                    characterName = "Mark",
                    fullForm = "We do not have any milk either, and she does not want tap water.",
                    naturalForm = "We don't have any milk either, and she doesn't want tap water.",
                    portugueseTranslation = "Nós também não temos leite, e ela não quer água da torneira.",
                    acceptableTranslations = listOf(
                        "A gente não tem leite também, e ela não quer água da torneira.",
                        "Também não temos leite, e ela não quer beber água de torneira."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("do not", "don't", "'do not' vira 'don't'."),
                        ContractionPair("does not", "doesn't", "'does not' vira 'doesn't'.")
                    ),
                    vocabularyNotes = "'tap water' = água da torneira. 'either' = também (em negativas).",
                    grammarTip = "Use 'either' no final de orações negativas, nunca 'too'.",
                    additionalExample = "He doesn't want to come either.",
                    additionalExampleTranslation = "Ele também não quer vir.",
                    blankSentence = "She ______ want to drink tap water.",
                    blankCorrectAnswer = "doesn't",
                    blankOptions = listOf("doesn't", "don't", "isn't", "didn't"),
                    quizQuestion = "Qual auxiliar combina com 'she' no presente negativo?",
                    quizCorrectAnswer = "doesn't",
                    quizOptions = listOf("doesn't", "don't", "aren't", "haven't"),
                    quizExplanation = "Terceira pessoa do singular (he/she/it) usa 'doesn't'."
                ),
                ScenePhrase(
                    id = "cotidiano_2_p3",
                    characterName = "Emma",
                    fullForm = "Do not worry, I will write everything down so I will not forget.",
                    naturalForm = "Don't worry, I'll write everything down so I won't forget.",
                    portugueseTranslation = "Não se preocupe, eu vou anotar tudo para não esquecer.",
                    acceptableTranslations = listOf(
                        "Relaxa, vou anotar tudo pra não esquecer.",
                        "Não esquenta, vou anotar tudo para eu não esquecer."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("Do not", "Don't", "'Do not' vira 'Don't'."),
                        ContractionPair("I will", "I'll", "'I will' vira 'I'll'."),
                        ContractionPair("will not", "won't", "'will not' vira 'won't'.")
                    ),
                    vocabularyNotes = "'write down' = anotar. 'won't forget' = não esquecerei.",
                    grammarTip = "'Will not' contrai irregularmente para 'won't'.",
                    additionalExample = "Don't worry, they won't leave without you.",
                    additionalExampleTranslation = "Não se preocupe, eles não vão sair sem você.",
                    blankSentence = "I'll write it down so I ______ forget.",
                    blankCorrectAnswer = "won't",
                    blankOptions = listOf("won't", "don't", "wouldn't", "can't"),
                    quizQuestion = "Qual é a contração de 'will not'?",
                    quizCorrectAnswer = "won't",
                    quizOptions = listOf("won't", "willn't", "woulnd't", "win't"),
                    quizExplanation = "'Will not' vira 'won't'."
                )
            )
        ),

        // ================= 3. ESCOLA (A2) =================
        Scene(
            id = "scene_escola_deadline",
            title = "Study Group Deadline",
            category = SceneCategory.ESCOLA,
            level = CefrLevel.A2,
            durationMinutes = 4,
            difficultyStars = 2,
            contextDescription = "Dois estudantes universitários na biblioteca revisam os slides do trabalho final antes da entrega da meia-noite.",
            characters = listOf("Chloe", "Alex"),
            genre = "Escola & Estudos / Juvenil",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("assignment", "deadline", "slides", "upload", "proofread"),
            expressions = listOf("Cut it close", "Hand in", "Almost done"),
            mediaConfig = SceneMediaConfig(durationSeconds = 240),
            phrases = listOf(
                ScenePhrase(
                    id = "escola_1_p1",
                    characterName = "Chloe",
                    fullForm = "We are running out of time, and I have not finished slide five yet.",
                    naturalForm = "We're running out of time, and I haven't finished slide five yet.",
                    portugueseTranslation = "Estamos ficando sem tempo, e eu ainda não terminei o slide cinco.",
                    acceptableTranslations = listOf(
                        "O nosso tempo está acabando, e ainda não terminei o quinto slide.",
                        "A gente tá sem tempo, e eu não terminei o slide 5 ainda."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("We are", "We're", "'We are' vira 'We're'."),
                        ContractionPair("have not", "haven't", "'have not' vira 'haven't'.")
                    ),
                    vocabularyNotes = "'run out of time' = ficar sem tempo. 'yet' = ainda.",
                    grammarTip = "'Haven't finished' usa Present Perfect porque a ação ainda é relevante agora.",
                    additionalExample = "We're late and they haven't arrived yet.",
                    additionalExampleTranslation = "Estamos atrasados e eles ainda não chegaram.",
                    blankSentence = "We're in a hurry and I ______ done it yet.",
                    blankCorrectAnswer = "haven't",
                    blankOptions = listOf("haven't", "didn't", "don't", "wasn't"),
                    quizQuestion = "O que significa 'running out of time'?",
                    quizCorrectAnswer = "Estar com o tempo quase esgotado",
                    quizOptions = listOf("Estar com o tempo quase esgotado", "Correr na esteira", "Perder o relógio", "Chegar adiantado"),
                    quizExplanation = "Expressão comum para urgência de prazos."
                ),
                ScenePhrase(
                    id = "escola_1_p2",
                    characterName = "Alex",
                    fullForm = "Do not panic, it is almost ready and we will submit it together.",
                    naturalForm = "Don't panic, it's almost ready and we'll submit it together.",
                    portugueseTranslation = "Não entre em pânico, está quase pronto e vamos enviar juntos.",
                    acceptableTranslations = listOf(
                        "Não pira, tá quase pronto e a gente vai mandar juntos.",
                        "Calma, já tá quase pronto e nós vamos enviar juntos."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("Do not", "Don't", "'Do not' vira 'Don't'."),
                        ContractionPair("it is", "it's", "'it is' vira 'it's'."),
                        ContractionPair("we will", "we'll", "'we will' vira 'we'll'.")
                    ),
                    vocabularyNotes = "'submit' = enviar/entregar uma tarefa. 'together' = juntos.",
                    grammarTip = "'We'll' é pronunciado com som suave de 'L' contra o céu da boca.",
                    additionalExample = "Don't worry, we'll fix the presentation.",
                    additionalExampleTranslation = "Não se preocupe, nós vamos consertar a apresentação.",
                    blankSentence = "Don't panic, ______ submit it on time.",
                    blankCorrectAnswer = "we'll",
                    blankOptions = listOf("we'll", "we're", "we've", "we'd"),
                    quizQuestion = "Qual a contração de 'we will'?",
                    quizCorrectAnswer = "we'll",
                    quizOptions = listOf("we'll", "we're", "well", "we'd"),
                    quizExplanation = "'We will' vira 'we'll'."
                )
            )
        ),

        // ================= 4. ESCOLA (B1) =================
        Scene(
            id = "scene_escola_thesis",
            title = "The Thesis Presentation",
            category = SceneCategory.ESCOLA,
            level = CefrLevel.B1,
            durationMinutes = 5,
            difficultyStars = 3,
            contextDescription = "Uma aluna conversa com seu orientador acadêmico após o ensaio geral da apresentação de conclusão de curso.",
            characters = listOf("Professor Evans", "Sarah"),
            genre = "Escola & Estudos / Acadêmico",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("thesis", "findings", "conclude", "rehearse", "feedback"),
            expressions = listOf("Hit the nail on the head", "Wrap up", "On point"),
            mediaConfig = SceneMediaConfig(durationSeconds = 300),
            phrases = listOf(
                ScenePhrase(
                    id = "escola_2_p1",
                    characterName = "Professor Evans",
                    fullForm = "You have got to explain your core methodology before you conclude.",
                    naturalForm = "You've gotta explain your core methodology before you conclude.",
                    portugueseTranslation = "Você tem que explicar sua metodologia central antes de concluir.",
                    acceptableTranslations = listOf(
                        "Você precisa explicar a metodologia principal antes de encerrar.",
                        "Você tem que detalhar sua metodologia chave antes de concluir."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("You have", "You've", "'You have' vira 'You've'."),
                        ContractionPair("got to", "gotta", "'have got to' vira 'gotta' na fala informal.")
                    ),
                    vocabularyNotes = "'core' = central/principal. 'methodology' = metodologia.",
                    grammarTip = "'Gotta' é a contração oral de 'have got to / have to' que indica necessidade.",
                    additionalExample = "You've gotta practice your timing.",
                    additionalExampleTranslation = "Você precisa praticar o seu tempo de fala.",
                    blankSentence = "You've ______ explain the data clearly.",
                    blankCorrectAnswer = "gotta",
                    blankOptions = listOf("gotta", "gonna", "wanna", "kinda"),
                    quizQuestion = "O que 'gotta' substitui na fala coloquial?",
                    quizCorrectAnswer = "got to / have to (precisar/ter que)",
                    quizOptions = listOf("got to / have to (precisar/ter que)", "going to (ir fazer)", "want to (querer)", "used to (costumava)"),
                    quizExplanation = "'Gotta' indica obrigação ou forte recomendação na fala."
                ),
                ScenePhrase(
                    id = "escola_2_p2",
                    characterName = "Sarah",
                    fullForm = "I would not have skipped it if I was not so nervous about the clock.",
                    naturalForm = "I wouldn't've skipped it if I wasn't so nervous about the clock.",
                    portugueseTranslation = "Eu não teria pulado isso se não estivesse tão nervosa com o relógio.",
                    acceptableTranslations = listOf(
                        "Não teria deixado de fora se eu não tivesse ficado tão ansiosa com o tempo.",
                        "Eu não teria pulado essa parte se não estivesse preocupada com o cronômetro."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("would not have", "wouldn't've", "'would not have' vira 'wouldn't've'."),
                        ContractionPair("was not", "wasn't", "'was not' vira 'wasn't'.")
                    ),
                    vocabularyNotes = "'skip' = pular/omitir. 'nervous about the clock' = preocupada com o tempo.",
                    grammarTip = "O condicional 'wouldn't have + particípio' avalia uma hipótese que não se concretizou.",
                    additionalExample = "I wouldn't've made this mistake if I wasn't in a hurry.",
                    additionalExampleTranslation = "Eu não teria cometido esse erro se não estivesse com pressa.",
                    blankSentence = "I ______ skipped it if I had more time.",
                    blankCorrectAnswer = "wouldn't have",
                    blankOptions = listOf("wouldn't have", "shouldn't have", "mustn't have", "won't have"),
                    quizQuestion = "Qual o significado de 'I wouldn't have skipped it'?",
                    quizCorrectAnswer = "Eu não teria deixado de falar sobre isso no passado",
                    quizOptions = listOf("Eu não teria deixado de falar sobre isso no passado", "Vou pular no futuro", "Eu odeio pular", "Eu devo pular"),
                    quizExplanation = "Indica hipótese contrária aos fatos passados."
                )
            )
        ),

        // ================= 5. TRABALHO (B1) =================
        Scene(
            id = "scene_trabalho_sprint",
            title = "The Sprint Retrospective",
            category = SceneCategory.TRABALHO,
            level = CefrLevel.B1,
            durationMinutes = 5,
            difficultyStars = 3,
            contextDescription = "Em uma reunião de equipe de tecnologia, o gerente e a desenvolvedora discutem o atraso na entrega da funcionalidade.",
            characters = listOf("Manager Dave", "Julia"),
            genre = "Trabalho & Negócios / Corporativo",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("blocker", "sprint", "release", "ship the feature", "heads up"),
            expressions = listOf("Give a heads up", "On the same page", "Touch base"),
            mediaConfig = SceneMediaConfig(durationSeconds = 300),
            phrases = listOf(
                ScenePhrase(
                    id = "trabalho_1_p1",
                    characterName = "Manager Dave",
                    fullForm = "Why did you not let us know that the server was down?",
                    naturalForm = "Why didn't you let us know that the server was down?",
                    portugueseTranslation = "Por que você não nos avisou que o servidor estava fora do ar?",
                    acceptableTranslations = listOf(
                        "Por que você não avisou a gente que o servidor tinha caído?",
                        "Por qual motivo você não nos deu um toque de que o servidor caiu?"
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("did not", "didn't", "'did not' vira 'didn't'.")
                    ),
                    vocabularyNotes = "'let someone know' = avisar/informar alguém. 'server down' = servidor fora do ar.",
                    grammarTip = "'Let us know' é a expressão mais natural em ambiente corporativo para 'nos avise'.",
                    additionalExample = "Why didn't you send the report earlier?",
                    additionalExampleTranslation = "Por que você não enviou o relatório mais cedo?",
                    blankSentence = "Why ______ you let us know about the bug?",
                    blankCorrectAnswer = "didn't",
                    blankOptions = listOf("didn't", "don't", "haven't", "aren't"),
                    quizQuestion = "O que significa 'let us know'?",
                    quizCorrectAnswer = "Nos avisar / nos manter informados",
                    quizOptions = listOf("Nos avisar / nos manter informados", "Nos deixar sair", "Conhecer a empresa", "Permitir o erro"),
                    quizExplanation = "'Let someone know' é o equivalente natural a 'avisar'."
                ),
                ScenePhrase(
                    id = "trabalho_1_p2",
                    characterName = "Julia",
                    fullForm = "I am sorry, but I thought we would fix it before anyone noticed.",
                    naturalForm = "I'm sorry, but I thought we'd fix it before anyone noticed.",
                    portugueseTranslation = "Peço desculpas, mas achei que a gente consertaria antes que alguém notasse.",
                    acceptableTranslations = listOf(
                        "Desculpe, mas pensei que conseguiríamos resolver antes de alguém perceber.",
                        "Foi mal, achei que a gente ia arrumar antes de alguém notar."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I am", "I'm", "'I am' vira 'I'm'."),
                        ContractionPair("we would", "we'd", "'we would' vira 'we'd'.")
                    ),
                    vocabularyNotes = "'noticed' = percebeu/notou. 'thought' = passado de think (pensei).",
                    grammarTip = "'We'd' contrai 'we would' expressando o futuro do pretérito.",
                    additionalExample = "I thought we'd meet at noon.",
                    additionalExampleTranslation = "Achei que nos encontraríamos ao meio-dia.",
                    blankSentence = "I thought ______ solve the problem faster.",
                    blankCorrectAnswer = "we'd",
                    blankOptions = listOf("we'd", "we'll", "we're", "we've"),
                    quizQuestion = "Nesta frase, 'we'd' é contração de qual estrutura?",
                    quizCorrectAnswer = "we would",
                    quizOptions = listOf("we would", "we had", "we did", "we should"),
                    quizExplanation = "Seguido de verbo base ('fix'), 'd' representa 'would'."
                )
            )
        ),

        // ================= 6. TRABALHO (B2) =================
        Scene(
            id = "scene_trabalho_salary",
            title = "Salary Negotiation Pitch",
            category = SceneCategory.TRABALHO,
            level = CefrLevel.B2,
            durationMinutes = 6,
            difficultyStars = 4,
            contextDescription = "Em uma sala executiva com vista panorâmica, uma consultora sênior negocia seu pacote salarial anual.",
            characters = listOf("Rachel", "Director Vance"),
            genre = "Trabalho & Negócios / Negociação",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("negotiation", "benchmark", "track record", "value proposition", "compensation"),
            expressions = listOf("Bring to the table", "Win-win situation", "Meet in the middle"),
            mediaConfig = SceneMediaConfig(durationSeconds = 360),
            phrases = listOf(
                ScenePhrase(
                    id = "trabalho_2_p1",
                    characterName = "Rachel",
                    fullForm = "Given my recent results, I do not think it is unreasonable to ask for a raise.",
                    naturalForm = "Given my recent results, I don't think it's unreasonable to ask for a raise.",
                    portugueseTranslation = "Considerando meus resultados recentes, não acho que seja absurdo pedir um aumento.",
                    acceptableTranslations = listOf(
                        "Pelos meus resultados recentes, não considero exagero pedir aumento.",
                        "Diante das minhas entregas recentes, não acho sem cabimento pedir aumento salarial."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("do not", "don't", "'do not' vira 'don't'."),
                        ContractionPair("it is", "it's", "'it is' vira 'it's'.")
                    ),
                    vocabularyNotes = "'given' = considerando/tendo em vista. 'ask for a raise' = pedir aumento de salário.",
                    grammarTip = "Litotes diplomática em inglês: 'I don't think it's unreasonable' (é muito razoável).",
                    additionalExample = "I don't think it's fair to delay the decision.",
                    additionalExampleTranslation = "Não acho justo adiar a decisão.",
                    blankSentence = "I ______ think it's too much to ask.",
                    blankCorrectAnswer = "don't",
                    blankOptions = listOf("don't", "didn't", "won't", "haven't"),
                    quizQuestion = "O que significa a expressão 'ask for a raise'?",
                    quizCorrectAnswer = "Pedir um aumento de salário",
                    quizOptions = listOf("Pedir um aumento de salário", "Pedir demissão", "Pedir férias antecipadas", "Pedir um empréstimo"),
                    quizExplanation = "'A raise' no contexto de trabalho é o aumento salarial."
                ),
                ScenePhrase(
                    id = "trabalho_2_p2",
                    characterName = "Director Vance",
                    fullForm = "You have certainly proven your value, so we will make an exception for you.",
                    naturalForm = "You've certainly proven your value, so we'll make an exception for you.",
                    portugueseTranslation = "Você certamente provou seu valor, então abriremos uma exceção para você.",
                    acceptableTranslations = listOf(
                        "Você com certeza provou o seu valor, por isso faremos uma exceção para você.",
                        "Você sem dúvida comprovou seu resultado, então vamos abrir uma exceção."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("You have", "You've", "'You have' vira 'You've'."),
                        ContractionPair("we will", "we'll", "'we will' vira 'we'll'.")
                    ),
                    vocabularyNotes = "'proven your value' = comprovou seu valor/competência. 'make an exception' = abrir exceção.",
                    grammarTip = "'You've proven' conecta a trajetória passada de sucesso ao momento presente.",
                    additionalExample = "You've earned our trust, so we'll support your project.",
                    additionalExampleTranslation = "Você conquistou nossa confiança, então apoiaremos seu projeto.",
                    blankSentence = "You've done great, so ______ back your proposal.",
                    blankCorrectAnswer = "we'll",
                    blankOptions = listOf("we'll", "we'd", "we're", "we've"),
                    quizQuestion = "O que significa 'make an exception'?",
                    quizCorrectAnswer = "Abrir uma exceção a favor de alguém",
                    quizOptions = listOf("Abrir uma exceção a favor de alguém", "Criar uma regra rígida", "Contratar uma nova equipe", "Recusar a proposta"),
                    quizExplanation = "Indica flexibilização de regra."
                )
            )
        ),

        // ================= 7. RESTAURANTE (A1) =================
        Scene(
            id = "scene_restaurante_table",
            title = "Table for Two, Please",
            category = SceneCategory.RESTAURANTE,
            level = CefrLevel.A1,
            durationMinutes = 3,
            difficultyStars = 1,
            contextDescription = "Um casal chega a um bistrô acolhedor no centro de Chicago para jantar sem reserva prévia.",
            characters = listOf("David", "Waiter Marco"),
            genre = "Restaurante & Café / Cotidiano",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("reservation", "window table", "menu", "specials", "appetizer"),
            expressions = listOf("Table for two", "Right this way", "Could we get..."),
            mediaConfig = SceneMediaConfig(durationSeconds = 180),
            phrases = listOf(
                ScenePhrase(
                    id = "restaurante_1_p1",
                    characterName = "David",
                    fullForm = "Good evening, we do not have a reservation, but we are looking for a table.",
                    naturalForm = "Good evening, we don't have a reservation, but we're looking for a table.",
                    portugueseTranslation = "Boa noite, não temos reserva, mas estamos procurando uma mesa.",
                    acceptableTranslations = listOf(
                        "Boa noite, a gente não tem reserva, mas tá procurando mesa.",
                        "Boa noite, nós não fizemos reserva, mas queríamos uma mesa."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("do not", "don't", "'do not' vira 'don't'."),
                        ContractionPair("we are", "we're", "'we are' vira 'we're'.")
                    ),
                    vocabularyNotes = "'reservation' = reserva. 'looking for' = procurando por.",
                    grammarTip = "'We're looking for' no presente contínuo expressa intenção imediata.",
                    additionalExample = "We don't have a car, so we're walking.",
                    additionalExampleTranslation = "Não temos carro, então estamos caminhando.",
                    blankSentence = "Good evening, we ______ have a booking.",
                    blankCorrectAnswer = "don't",
                    blankOptions = listOf("don't", "didn't", "haven't", "aren't"),
                    quizQuestion = "Qual a forma polida de dizer que você não tem reserva?",
                    quizCorrectAnswer = "We don't have a reservation",
                    quizOptions = listOf("We don't have a reservation", "I no reserve", "Reservation is none", "Cancel my table"),
                    quizExplanation = "Frase padrão de etiqueta em restaurantes no exterior."
                ),
                ScenePhrase(
                    id = "restaurante_1_p2",
                    characterName = "Waiter Marco",
                    fullForm = "It is your lucky day, we have got a quiet corner table by the window.",
                    naturalForm = "It's your lucky day, we've got a quiet corner table by the window.",
                    portugueseTranslation = "É o seu dia de sorte, temos uma mesa reservada no canto perto da janela.",
                    acceptableTranslations = listOf(
                        "Hoje é seu dia de sorte, a gente tem uma mesa tranquila no canto da janela.",
                        "Vocês tão com sorte, temos uma mesa silenciosa bem na janela."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("It is", "It's", "'It is' vira 'It's'."),
                        ContractionPair("we have", "we've", "'we have got' vira 'we've got'.")
                    ),
                    vocabularyNotes = "'lucky day' = dia de sorte. 'corner table' = mesa de canto.",
                    grammarTip = "'By the window' significa 'ao lado / junto à janela'.",
                    additionalExample = "It's cold outside, but we've got hot tea.",
                    additionalExampleTranslation = "Está frio lá fora, mas temos chá quente.",
                    blankSentence = "It's your lucky day, ______ got a table.",
                    blankCorrectAnswer = "we've",
                    blankOptions = listOf("we've", "we'll", "we'd", "we're"),
                    quizQuestion = "O que significa 'by the window'?",
                    quizCorrectAnswer = "Perto ou ao lado da janela",
                    quizOptions = listOf("Perto ou ao lado da janela", "Longe da janela", "Embaixo do vidro", "Fora do restaurante"),
                    quizExplanation = "A preposição 'by' indica proximidade física imediata."
                )
            )
        ),

        // ================= 8. RESTAURANTE (B1) =================
        Scene(
            id = "scene_restaurante_chef",
            title = "The Chef's Secret Recipe",
            category = SceneCategory.RESTAURANTE,
            level = CefrLevel.B1,
            durationMinutes = 5,
            difficultyStars = 3,
            contextDescription = "Um crítico gastronômico elogia o prato principal e tenta descobrir o ingrediente secreto com o chef.",
            characters = listOf("Food Critic Leo", "Chef Antonio"),
            genre = "Restaurante & Café / Culinária",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("seasoning", "sauce", "compliments to the chef", "secret ingredient", "culinary"),
            expressions = listOf("My compliments to the chef", "Mouth-watering", "Hit the spot"),
            mediaConfig = SceneMediaConfig(durationSeconds = 300),
            phrases = listOf(
                ScenePhrase(
                    id = "restaurante_2_p1",
                    characterName = "Food Critic Leo",
                    fullForm = "I have not tasted a sauce this rich in a very long time.",
                    naturalForm = "I haven't tasted a sauce this rich in a very long time.",
                    portugueseTranslation = "Eu não provava um molho tão encorpado há muito tempo.",
                    acceptableTranslations = listOf(
                        "Faz muito tempo que eu não provava um molho tão saboroso.",
                        "Havia muito tempo que eu não experimentava um molho tão rico."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("have not", "haven't", "'have not' vira 'haven't'.")
                    ),
                    vocabularyNotes = "'sauce' = molho. 'rich' = rico/encorpado/saboroso.",
                    grammarTip = "'This rich' usa 'this' como intensificador coloquial (tão encorpado assim).",
                    additionalExample = "I haven't seen a movie this good this year.",
                    additionalExampleTranslation = "Não vi um filme tão bom este ano.",
                    blankSentence = "I ______ tasted pasta this delicious before.",
                    blankCorrectAnswer = "haven't",
                    blankOptions = listOf("haven't", "didn't", "don't", "wasn't"),
                    quizQuestion = "Qual o sentido de 'rich' quando aplicado a um molho ou comida?",
                    quizCorrectAnswer = "Encorpado, amanteigado e de sabor profundo",
                    quizOptions = listOf("Encorpado, amanteigado e de sabor profundo", "Muito caro em dinheiro", "Cheio de ouro", "Doce como açúcar"),
                    quizExplanation = "Em gastronomia, 'rich' descreve textura aveludada e sabor intenso."
                ),
                ScenePhrase(
                    id = "restaurante_2_p2",
                    characterName = "Chef Antonio",
                    fullForm = "I will not reveal the exact herbs, but there is a touch of smoked paprika.",
                    naturalForm = "I won't reveal the exact herbs, but there's a touch of smoked paprika.",
                    portugueseTranslation = "Eu não vou revelar as ervas exatas, mas há um toque de páprica defumada.",
                    acceptableTranslations = listOf(
                        "Não vou entregar as ervas exatas, mas tem um toque de páprica defumada.",
                        "Não revelarei o segredo das ervas, só que tem uma pitada de páprica defumada."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("will not", "won't", "'will not' vira 'won't'."),
                        ContractionPair("there is", "there's", "'there is' vira 'there's'.")
                    ),
                    vocabularyNotes = "'herbs' = ervas aromáticas. 'smoked paprika' = páprica defumada.",
                    grammarTip = "'There's' contrai 'there is' para indicar existência singular no presente.",
                    additionalExample = "I won't tell anyone, but there's a surprise planned.",
                    additionalExampleTranslation = "Eu não vou contar a ninguém, mas há uma surpresa planejada.",
                    blankSentence = "I ______ tell the secret, but there's garlic in it.",
                    blankCorrectAnswer = "won't",
                    blankOptions = listOf("won't", "don't", "wouldn't", "can't"),
                    quizQuestion = "Qual é a contração de 'there is'?",
                    quizCorrectAnswer = "there's",
                    quizOptions = listOf("there's", "theres", "their's", "they're"),
                    quizExplanation = "'There is' contrai para 'there's'."
                )
            )
        ),

        // ================= 9. VIAGEM (A2) =================
        Scene(
            id = "scene_viagem_rome",
            title = "Lost in Downtown Rome",
            category = SceneCategory.VIAGEM,
            level = CefrLevel.A2,
            durationMinutes = 4,
            difficultyStars = 2,
            contextDescription = "Uma turista pede ajuda a um guia local ao tentar encontrar a famosa Fontana di Trevi em um beco antigo.",
            characters = listOf("Clara", "Tour Guide Paolo"),
            genre = "Viagem & Turismo / Aventura",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("landmark", "alley", "lost", "fountain", "turn left"),
            expressions = listOf("Excuse me", "How do I get to...", "Just around the corner"),
            mediaConfig = SceneMediaConfig(durationSeconds = 240),
            phrases = listOf(
                ScenePhrase(
                    id = "viagem_1_p1",
                    characterName = "Clara",
                    fullForm = "Excuse me, I am completely lost and my map is not loading.",
                    naturalForm = "Excuse me, I'm completely lost and my map isn't loading.",
                    portugueseTranslation = "Com licença, estou completamente perdida e meu mapa não está carregando.",
                    acceptableTranslations = listOf(
                        "Desculpe, tô totalmente perdida e o mapa não carrega.",
                        "Com licença, me perdi toda e o meu mapa não tá abrindo."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I am", "I'm", "'I am' vira 'I'm'."),
                        ContractionPair("is not", "isn't", "'is not' vira 'isn't'.")
                    ),
                    vocabularyNotes = "'completely lost' = totalmente perdida. 'loading' = carregando dados.",
                    grammarTip = "O gerúndio com 'isn't loading' descreve uma falha contínua no momento.",
                    additionalExample = "I'm looking for the museum, but the app isn't working.",
                    additionalExampleTranslation = "Estou procurando o museu, mas o aplicativo não está funcionando.",
                    blankSentence = "Excuse me, my phone ______ loading the GPS.",
                    blankCorrectAnswer = "isn't",
                    blankOptions = listOf("isn't", "aren't", "don't", "didn't"),
                    quizQuestion = "Qual a melhor forma de pedir ajuda na rua em inglês?",
                    quizCorrectAnswer = "Excuse me, I'm lost...",
                    quizOptions = listOf("Excuse me, I'm lost...", "Hey you help me", "Where street is", "I no know city"),
                    quizExplanation = "'Excuse me' é a introdução educada padrão."
                ),
                ScenePhrase(
                    id = "viagem_1_p2",
                    characterName = "Tour Guide Paolo",
                    fullForm = "You are not far at all; it is just two blocks straight ahead.",
                    naturalForm = "You're not far at all; it's just two blocks straight ahead.",
                    portugueseTranslation = "Você não está nem um pouco longe; fica a apenas duas quadras em frente.",
                    acceptableTranslations = listOf(
                        "Você não tá longe de jeito nenhum; são só dois quarteirões em linha reta.",
                        "Tá pertinho; só duas quadras seguindo direto."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("You are", "You're", "'You are' vira 'You're'."),
                        ContractionPair("it is", "it's", "'it is' vira 'it's'.")
                    ),
                    vocabularyNotes = "'not far at all' = nada longe. 'straight ahead' = direto em frente.",
                    grammarTip = "'At all' reforça a ideia de negatividade total.",
                    additionalExample = "You're almost there; it's right on the left.",
                    additionalExampleTranslation = "Você está quase lá; fica bem à esquerda.",
                    blankSentence = "Don't worry, ______ only a short walk.",
                    blankCorrectAnswer = "it's",
                    blankOptions = listOf("it's", "its", "is", "he's"),
                    quizQuestion = "O que significa a direção 'straight ahead'?",
                    quizCorrectAnswer = "Seguir em frente / em linha reta",
                    quizOptions = listOf("Seguir em frente / em linha reta", "Virar à direita", "Dar meia-volta", "Pegar o metrô"),
                    quizExplanation = "'Straight ahead' significa seguir adiante no mesmo caminho."
                )
            )
        ),

        // ================= 10. VIAGEM (B1) =================
        Scene(
            id = "scene_viagem_cabin",
            title = "The Mountain Cabin Roadtrip",
            category = SceneCategory.VIAGEM,
            level = CefrLevel.B1,
            durationMinutes = 5,
            difficultyStars = 3,
            contextDescription = "Dois amigos em uma viagem de carro pelo interior das montanhas percebem que o tanque de gasolina está na reserva.",
            characters = listOf("Sam", "Jessica"),
            genre = "Viagem & Turismo / Estrada",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("roadtrip", "gas station", "empty tank", "scenic route", "pull over"),
            expressions = listOf("Hit the road", "Run on fumes", "Pull over"),
            mediaConfig = SceneMediaConfig(durationSeconds = 300),
            phrases = listOf(
                ScenePhrase(
                    id = "viagem_2_p1",
                    characterName = "Sam",
                    fullForm = "We should have stopped at that last gas station because we are running on fumes.",
                    naturalForm = "We should've stopped at that last gas station 'cause we're running on fumes.",
                    portugueseTranslation = "Nós devíamos ter parado naquele último posto porque estamos rodando no cheiro da gasolina.",
                    acceptableTranslations = listOf(
                        "Deveríamos ter abastecido no posto anterior porque o tanque tá no vapor.",
                        "A gente devia ter parado no último posto, tamo quase sem combustível."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("should have", "should've", "'should have' vira 'should've'."),
                        ContractionPair("because", "'cause", "'because' vira ''cause' na fala cotidiana."),
                        ContractionPair("we are", "we're", "'we are' vira 'we're'.")
                    ),
                    vocabularyNotes = "'gas station' = posto de gasolina. 'running on fumes' = rodando no limite da reserva.",
                    grammarTip = "'Should've stopped' indica que a decisão ideal no passado não foi tomada.",
                    additionalExample = "We should've left earlier 'cause the traffic is awful.",
                    additionalExampleTranslation = "Deveríamos ter saído mais cedo porque o trânsito está horrível.",
                    blankSentence = "We ______ stopped before the highway.",
                    blankCorrectAnswer = "should've",
                    blankOptions = listOf("should've", "must've", "couldn't", "won't have"),
                    quizQuestion = "O que a gíria 'running on fumes' significa no carro?",
                    quizCorrectAnswer = "Estar quase sem gasolina (na reserva extrema)",
                    quizOptions = listOf("Estar quase sem gasolina (na reserva extrema)", "O motor estar soltando fumaça", "O ar condicionado estar quebrado", "Dirigir muito veloz"),
                    quizExplanation = "Metáfora comum que indica andar apenas com o vapor da gasolina."
                ),
                ScenePhrase(
                    id = "viagem_2_p2",
                    characterName = "Jessica",
                    fullForm = "There is a small town up ahead, so we will make it if you do not speed.",
                    naturalForm = "There's a small town up ahead, so we'll make it if you don't speed.",
                    portugueseTranslation = "Tem um vilarejo logo à frente, então vamos conseguir chegar se você não correr.",
                    acceptableTranslations = listOf(
                        "Tem uma cidadezinha logo adiante, então a gente chega lá se você não acelerar.",
                        "Há um vilarejo logo à frente, a gente dá conta se você não correr."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("There is", "There's", "'There is' vira 'There's'."),
                        ContractionPair("we will", "we'll", "'we will' vira 'we'll'."),
                        ContractionPair("do not", "don't", "'do not' vira 'don't'.")
                    ),
                    vocabularyNotes = "'up ahead' = logo adiante. 'speed' = correr/ultrapassar a velocidade.",
                    grammarTip = "First Conditional clássico: 'We'll make it if you don't speed'.",
                    additionalExample = "There's hope, so we'll succeed if you don't give up.",
                    additionalExampleTranslation = "Há esperança, então venceremos se você não desistir.",
                    blankSentence = "We'll make it if you ______ drive too fast.",
                    blankCorrectAnswer = "don't",
                    blankOptions = listOf("don't", "won't", "didn't", "haven't"),
                    quizQuestion = "O que significa 'we'll make it'?",
                    quizCorrectAnswer = "Vamos conseguir chegar a tempo / dar conta",
                    quizOptions = listOf("Vamos conseguir chegar a tempo / dar conta", "Vamos fabricar algo", "Vamos desistir", "Vamos comprar um carro"),
                    quizExplanation = "'To make it' significa ter sucesso em alcançar o destino."
                )
            )
        ),

        // ================= 11. AEROPORTO (A2) =================
        Scene(
            id = "scene_aeroporto_customs",
            title = "Immigration & Customs Check",
            category = SceneCategory.AEROPORTO,
            level = CefrLevel.A2,
            durationMinutes = 4,
            difficultyStars = 2,
            contextDescription = "Um viajante internacional se aproxima da cabine de imigração no aeroporto JFK em Nova York.",
            characters = listOf("Passenger Bruno", "Officer Davis"),
            genre = "Aeroporto & Imigração / Formal",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("passport", "customs", "purpose of visit", "sightseeing", "return ticket"),
            expressions = listOf("Purpose of your visit", "Enjoy your stay", "Boarding pass"),
            mediaConfig = SceneMediaConfig(durationSeconds = 240),
            phrases = listOf(
                ScenePhrase(
                    id = "aeroporto_1_p1",
                    characterName = "Officer Davis",
                    fullForm = "What is the purpose of your trip, and how long are you going to stay?",
                    naturalForm = "What's the purpose of your trip, and how long're you gonna stay?",
                    portugueseTranslation = "Qual é o objetivo da sua viagem e quanto tempo você vai ficar?",
                    acceptableTranslations = listOf(
                        "Qual o motivo da sua viagem e por quanto tempo você vai ficar?",
                        "Qual a razão da visita e quanto tempo você pretende ficar?"
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("What is", "What's", "'What is' vira 'What's'."),
                        ContractionPair("how long are", "how long're", "'how long are' se conecta em 'how long're'."),
                        ContractionPair("going to", "gonna", "'going to' vira 'gonna'.")
                    ),
                    vocabularyNotes = "'purpose' = objetivo/motivo. 'stay' = permanecer/hospedar-se.",
                    grammarTip = "'What's the purpose of your visit?' é a pergunta padrão de todos os agentes de imigração.",
                    additionalExample = "What's your plan and where're you gonna sleep?",
                    additionalExampleTranslation = "Qual é o seu plano e onde você vai dormir?",
                    blankSentence = "What's the purpose and how long are you ______ stay?",
                    blankCorrectAnswer = "going to",
                    blankOptions = listOf("going to", "go to", "went to", "goes to"),
                    quizQuestion = "Qual a resposta ideal para a pergunta de 'purpose of trip' em turismo?",
                    quizCorrectAnswer = "I'm here for vacation and sightseeing",
                    quizOptions = listOf("I'm here for vacation and sightseeing", "I want to move here forever", "I have no idea", "I don't care"),
                    quizExplanation = "Informar turismo e férias é a resposta clara e esperada."
                ),
                ScenePhrase(
                    id = "aeroporto_1_p2",
                    characterName = "Passenger Bruno",
                    fullForm = "I am here on vacation, and I will be staying for two weeks at a hotel in Manhattan.",
                    naturalForm = "I'm here on vacation, and I'll be staying for two weeks at a hotel in Manhattan.",
                    portugueseTranslation = "Estou aqui de férias e vou ficar por duas semanas em um hotel em Manhattan.",
                    acceptableTranslations = listOf(
                        "Tô aqui de férias e vou me hospedar por duas semanas em Manhattan.",
                        "Vim de férias e ficarei duas semanas num hotel em Manhattan."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I am", "I'm", "'I am' vira 'I'm'."),
                        ContractionPair("I will", "I'll", "'I will' vira 'I'll'.")
                    ),
                    vocabularyNotes = "'on vacation' = de férias. 'two weeks' = duas semanas.",
                    grammarTip = "Future Continuous 'I'll be staying' expressa plano seguro e temporário.",
                    additionalExample = "I'm here for business and I'll be leaving on Sunday.",
                    additionalExampleTranslation = "Estou aqui a negócios e irei embora no domingo.",
                    blankSentence = "I'm here for vacation and ______ stay in Manhattan.",
                    blankCorrectAnswer = "I'll",
                    blankOptions = listOf("I'll", "I'd", "I'm", "I've"),
                    quizQuestion = "Como se diz 'estar de férias' em inglês?",
                    quizCorrectAnswer = "On vacation",
                    quizOptions = listOf("On vacation", "In holyday", "At rest", "Off workly"),
                    quizExplanation = "A expressão correta em inglês americano é 'on vacation'."
                )
            )
        ),

        // ================= 12. AEROPORTO (B1) =================
        Scene(
            id = "scene_aeroporto_connection",
            title = "Tight Flight Connection",
            category = SceneCategory.AEROPORTO,
            level = CefrLevel.B1,
            durationMinutes = 5,
            difficultyStars = 3,
            contextDescription = "Com o voo de conexão quase decolando, um passageiro ofegante corre até o portão de embarque internacional.",
            characters = listOf("Passenger Tom", "Gate Agent Lisa"),
            genre = "Aeroporto & Imigração / Correria",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("boarding gate", "connection", "layover", "final call", "overhead bin"),
            expressions = listOf("Final boarding call", "Make a connection", "Breathe a sigh of relief"),
            mediaConfig = SceneMediaConfig(durationSeconds = 300),
            phrases = listOf(
                ScenePhrase(
                    id = "aeroporto_2_p1",
                    characterName = "Passenger Tom",
                    fullForm = "Tell me that you have not closed the gate yet; my first flight was delayed.",
                    naturalForm = "Tell me you haven't closed the gate yet; my first flight was delayed.",
                    portugueseTranslation = "Me diga que você ainda não fechou o portão; meu primeiro voo atrasou.",
                    acceptableTranslations = listOf(
                        "Diz que o portão não fechou ainda; meu voo anterior atrasou.",
                        "Me diz que vocês ainda não fecharam o portão, meu primeiro voo atrasou."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("have not", "haven't", "'have not' vira 'haven't'.")
                    ),
                    vocabularyNotes = "'delayed' = atrasado. 'gate closed' = portão fechado.",
                    grammarTip = "Omissão natural de 'that' após verbos de fala: 'Tell me [that] you haven't...'.",
                    additionalExample = "Tell me you haven't lost your boarding pass.",
                    additionalExampleTranslation = "Me diga que você não perdeu seu cartão de embarque.",
                    blankSentence = "Tell me they ______ departed yet.",
                    blankCorrectAnswer = "haven't",
                    blankOptions = listOf("haven't", "didn't", "don't", "wasn't"),
                    quizQuestion = "O que significa um voo ser 'delayed'?",
                    quizCorrectAnswer = "O voo sofreu atraso no horário",
                    quizOptions = listOf("O voo sofreu atraso no horário", "O voo foi cancelado", "O avião aterrissou antes", "O piloto trocou de rota"),
                    quizExplanation = "'Delayed' significa atrasado."
                ),
                ScenePhrase(
                    id = "aeroporto_2_p2",
                    characterName = "Gate Agent Lisa",
                    fullForm = "You are just in time, but you have got to scan your boarding pass right now.",
                    naturalForm = "You're just in time, but you've gotta scan your boarding pass right now.",
                    portugueseTranslation = "Você chegou bem a tempo, mas tem que escanear seu cartão de embarque agora mesmo.",
                    acceptableTranslations = listOf(
                        "Chegou na hora certa, mas precisa escanear o bilhete de embarque já.",
                        "Você tá em cima da hora, mas precisa passar seu cartão de embarque agora."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("You are", "You're", "'You are' vira 'You're'."),
                        ContractionPair("have got to", "gotta", "'have got to' vira 'gotta' na fala veloz.")
                    ),
                    vocabularyNotes = "'just in time' = bem na hora/em cima do laço. 'scan' = escanear/ler código.",
                    grammarTip = "'Right now' expressa imediatismo imperativo.",
                    additionalExample = "You're right on time, but you've gotta hurry.",
                    additionalExampleTranslation = "Você está na hora, mas precisa se apressar.",
                    blankSentence = "You're in time, but you've ______ hurry.",
                    blankCorrectAnswer = "gotta",
                    blankOptions = listOf("gotta", "gonna", "wanna", "ought"),
                    quizQuestion = "Qual o significado de 'just in time'?",
                    quizCorrectAnswer = "Exatamente no último momento possível",
                    quizOptions = listOf("Exatamente no último momento possível", "Muito adiantado", "Tarde demais", "Sem pressa"),
                    quizExplanation = "Significa chegar na hora exata antes do encerramento."
                )
            )
        ),

        // ================= 13. RELACIONAMENTO (A2) =================
        Scene(
            id = "scene_relacionamento_date",
            title = "The Awkward First Date",
            category = SceneCategory.RELACIONAMENTO,
            level = CefrLevel.A2,
            durationMinutes = 4,
            difficultyStars = 2,
            contextDescription = "Em um restaurante à luz de velas, duas pessoas tentam quebrar o gelo após um momento de silêncio constrangedor.",
            characters = listOf("Nina", "Daniel"),
            genre = "Relacionamento & Romance / Comédia Romântica",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("awkward", "break the ice", "honest", "laugh", "first impression"),
            expressions = listOf("Break the ice", "To be honest", "Have in common"),
            mediaConfig = SceneMediaConfig(durationSeconds = 240),
            phrases = listOf(
                ScenePhrase(
                    id = "relacionamento_1_p1",
                    characterName = "Nina",
                    fullForm = "I am not usually this quiet, but I was nervous about meeting you.",
                    naturalForm = "I'm not usually this quiet, but I was nervous about meeting you.",
                    portugueseTranslation = "Eu geralmente não sou tão quieta assim, mas estava nervosa para te conhecer.",
                    acceptableTranslations = listOf(
                        "Eu não costumo ser tão calada, mas tava com vergonha de te encontrar.",
                        "Não sou tão quieta no dia a dia, é que fiquei ansiosa pra te ver."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I am", "I'm", "'I am' vira 'I'm'.")
                    ),
                    vocabularyNotes = "'quiet' = quieto(a)/calado(a). 'meeting you' = conhecer você pessoalmente.",
                    grammarTip = "'This quiet' usa 'this' como advérbio de intensidade (tão quieta assim).",
                    additionalExample = "I'm not usually shy, but this is new to me.",
                    additionalExampleTranslation = "Geralmente não sou tímido, mas isso é novidade para mim.",
                    blankSentence = "______ not usually this shy on dates.",
                    blankCorrectAnswer = "I'm",
                    blankOptions = listOf("I'm", "I've", "I'll", "I'd"),
                    quizQuestion = "O que 'this quiet' quer dizer?",
                    quizCorrectAnswer = "Tão quieta quanto agora",
                    quizOptions = listOf("Tão quieta quanto agora", "Muito barulhenta", "Aquela pessoa calada", "Sem voz"),
                    quizExplanation = "'This' seguido de adjetivo intensifica a qualidade no presente."
                ),
                ScenePhrase(
                    id = "relacionamento_1_p2",
                    characterName = "Daniel",
                    fullForm = "That is a huge relief because I thought you did not like my jokes.",
                    naturalForm = "That's a huge relief 'cause I thought you didn't like my jokes.",
                    portugueseTranslation = "Isso é um alívio enorme porque achei que você não tivesse gostado das minhas piadas.",
                    acceptableTranslations = listOf(
                        "Que alívio, porque achei que você não tinha curtido minhas piadas.",
                        "Isso é um alívio danado, achei que você não tinha achado graça."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("That is", "That's", "'That is' vira 'That's'."),
                        ContractionPair("because", "'cause", "'because' reduz para ''cause'."),
                        ContractionPair("did not", "didn't", "'did not' vira 'didn't'.")
                    ),
                    vocabularyNotes = "'huge relief' = alívio enorme. 'jokes' = piadas.",
                    grammarTip = "'Didn't like' no passado indica a impressão anterior na conversa.",
                    additionalExample = "That's good to hear 'cause I was worried.",
                    additionalExampleTranslation = "Bom saber, porque eu estava preocupado.",
                    blankSentence = "That's a relief 'cause I thought you ______ enjoy it.",
                    blankCorrectAnswer = "didn't",
                    blankOptions = listOf("didn't", "don't", "weren't", "won't"),
                    quizQuestion = "O que significa 'a huge relief'?",
                    quizCorrectAnswer = "Um alívio muito grande",
                    quizOptions = listOf("Um alívio muito grande", "Uma dor de cabeça", "Uma piada ruim", "Um encontro caro"),
                    quizExplanation = "Alívio emocional após uma incerteza."
                )
            )
        ),

        // ================= 14. RELACIONAMENTO (B1) =================
        Scene(
            id = "scene_relacionamento_rooftop",
            title = "Rooftop Confessions",
            category = SceneCategory.RELACIONAMENTO,
            level = CefrLevel.B1,
            durationMinutes = 5,
            difficultyStars = 3,
            contextDescription = "No terraço de um prédio ao pôr do sol, dois amigos de longa data conversam sobre sentimentos que nunca tiveram coragem de admitir.",
            characters = listOf("Liam", "Maya"),
            genre = "Relacionamento & Romance / Drama Romântico",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("confession", "honest", "gut feeling", "regret", "feelings"),
            expressions = listOf("Take a leap of faith", "Turn down", "Speak from the heart"),
            mediaConfig = SceneMediaConfig(durationSeconds = 300),
            phrases = listOf(
                ScenePhrase(
                    id = "relacionamento_2_p1",
                    characterName = "Liam",
                    fullForm = "I cannot believe you are going to turn down that job offer in London.",
                    naturalForm = "I can't believe you're gonna turn down that job offer in London.",
                    portugueseTranslation = "Não consigo acreditar que você vai recusar aquela proposta de emprego em Londres.",
                    acceptableTranslations = listOf(
                        "Não acredito que você vai rejeitar a proposta de trabalho em Londres.",
                        "Inacreditável que você vai recusar aquela vaga em Londres."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("cannot", "can't", "'cannot' vira 'can't'."),
                        ContractionPair("you are", "you're", "'you are' vira 'you're'."),
                        ContractionPair("going to", "gonna", "'going to' vira 'gonna'.")
                    ),
                    vocabularyNotes = "'turn down' = recusar/rejeitar. 'job offer' = oferta de emprego.",
                    grammarTip = "'Gonna turn down' mostra a contração oral de 'going to' com verbo de ação.",
                    additionalExample = "I can't believe you're gonna leave without saying goodbye.",
                    additionalExampleTranslation = "Não acredito que você vai embora sem se despedir.",
                    blankSentence = "I can't believe you're gonna ______ that opportunity.",
                    blankCorrectAnswer = "turn down",
                    blankOptions = listOf("turn down", "turn up", "turn off", "turn into"),
                    quizQuestion = "Qual o significado de 'turn down'?",
                    quizCorrectAnswer = "Recusar ou rejeitar uma oportunidade",
                    quizOptions = listOf("Recusar ou rejeitar uma oportunidade", "Aumentar o volume", "Girar a maçaneta", "Aceitar prontamente"),
                    quizExplanation = "'Turn down' é o phrasal verb para recusar."
                ),
                ScenePhrase(
                    id = "relacionamento_2_p2",
                    characterName = "Maya",
                    fullForm = "I would have taken it, but it is not what I really want to do with my life.",
                    naturalForm = "I would've taken it, but it's not what I really wanna do with my life.",
                    portugueseTranslation = "Eu teria aceitado, mas não é o que eu realmente quero fazer da minha vida.",
                    acceptableTranslations = listOf(
                        "Eu teria aceito, só que não é o que eu de verdade quero fazer com a minha vida.",
                        "Teria aceitado a vaga, mas não é isso que quero pra mim."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I would have", "I would've", "'would have' vira 'would've'."),
                        ContractionPair("it is", "it's", "'it is' vira 'it's'."),
                        ContractionPair("want to", "wanna", "'want to' vira 'wanna'.")
                    ),
                    vocabularyNotes = "'take it' = aceitar. 'with my life' = com a minha vida.",
                    grammarTip = "O Third Conditional 'I would've taken it' expressa a escolha alternativa no passado.",
                    additionalExample = "She would've called you, but she didn't wanna bother you.",
                    additionalExampleTranslation = "Ela teria te ligado, mas não queria te incomodar.",
                    blankSentence = "I ______ accepted, but it didn't feel right.",
                    blankCorrectAnswer = "would've",
                    blankOptions = listOf("would've", "should've", "must've", "could've"),
                    quizQuestion = "O que 'would've taken' indica gramaticalmente?",
                    quizCorrectAnswer = "Uma ação que teria ocorrido no passado sob outra condição",
                    quizOptions = listOf("Uma ação que teria ocorrido no passado sob outra condição", "Um plano para amanhã", "Um arrependimento moral obrigatório", "Uma certeza presente"),
                    quizExplanation = "'Would have' é a estrutura condicional do passado."
                )
            )
        ),

        // ================= 15. AMIZADE (A2) =================
        Scene(
            id = "scene_amizade_birthday",
            title = "Surprise Birthday Planning",
            category = SceneCategory.AMIZADE,
            level = CefrLevel.A2,
            durationMinutes = 4,
            difficultyStars = 2,
            contextDescription = "Dois amigos cochicham na sala de estar planejando uma festa surpresa para o colega de quarto.",
            characters = listOf("Felipe", "Bianca"),
            genre = "Amizade & Social / Cotidiano",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("surprise party", "secret", "decorations", "keep quiet", "cake"),
            expressions = listOf("Keep it a secret", "Spill the beans", "Count me in"),
            mediaConfig = SceneMediaConfig(durationSeconds = 240),
            phrases = listOf(
                ScenePhrase(
                    id = "amizade_1_p1",
                    characterName = "Felipe",
                    fullForm = "Do not tell anyone about the party because he does not suspect a thing.",
                    naturalForm = "Don't tell anyone about the party 'cause he doesn't suspect a thing.",
                    portugueseTranslation = "Não conte a ninguém sobre a festa porque ele não desconfia de nada.",
                    acceptableTranslations = listOf(
                        "Não fala pra ninguém da festa, ele não tá desconfiando de nada.",
                        "Fica quieto sobre a festa porque ele não suspeita de nada."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("Do not", "Don't", "'Do not' vira 'Don't'."),
                        ContractionPair("because", "'cause", "'because' vira ''cause'."),
                        ContractionPair("does not", "doesn't", "'does not' vira 'doesn't'.")
                    ),
                    vocabularyNotes = "'suspect a thing' = desconfiar de qualquer coisa. 'secret' = segredo.",
                    grammarTip = "O pronome 'anyone' é usado após imperativos e negativas.",
                    additionalExample = "Don't say a word 'cause she doesn't know yet.",
                    additionalExampleTranslation = "Não diga uma palavra porque ela ainda não sabe.",
                    blankSentence = "Don't tell anyone 'cause he ______ know.",
                    blankCorrectAnswer = "doesn't",
                    blankOptions = listOf("doesn't", "don't", "isn't", "didn't"),
                    quizQuestion = "Qual auxiliar combina com 'he' no presente negativo?",
                    quizCorrectAnswer = "doesn't",
                    quizOptions = listOf("doesn't", "don't", "aren't", "won't"),
                    quizExplanation = "'He/she/it' pede 'doesn't'."
                ),
                ScenePhrase(
                    id = "amizade_1_p2",
                    characterName = "Bianca",
                    fullForm = "My lips are sealed; I will bring the cake and the balloons at six.",
                    naturalForm = "My lips're sealed; I'll bring the cake and the balloons at six.",
                    portugueseTranslation = "Minha boca é um túmulo; eu vou trazer o bolo e os balões às seis.",
                    acceptableTranslations = listOf(
                        "Boca fechada; vou levar o bolo e as bexigas às seis horas.",
                        "Pode confiar; vou trazer o bolo e a decoração às seis."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("lips are", "lips're", "'lips are' vira 'lips're' na fala conectada."),
                        ContractionPair("I will", "I'll", "'I will' vira 'I'll'.")
                    ),
                    vocabularyNotes = "'my lips are sealed' = minha boca é um túmulo / segredo guardado.",
                    grammarTip = "Idiom clássico de filmes para prometer sigilo absoluto.",
                    additionalExample = "Don't worry, my lips're sealed, I'll never tell.",
                    additionalExampleTranslation = "Não se preocupe, minha boca é um túmulo, nunca contarei.",
                    blankSentence = "My lips are sealed, ______ bring the food.",
                    blankCorrectAnswer = "I'll",
                    blankOptions = listOf("I'll", "I'd", "I'm", "I've"),
                    quizQuestion = "O que significa 'My lips are sealed'?",
                    quizCorrectAnswer = "Guardarei o segredo e não contarei a ninguém",
                    quizOptions = listOf("Guardarei o segredo e não contarei a ninguém", "Estou com sede", "Não posso falar por dor", "Perdi a chave"),
                    quizExplanation = "Expressão idiomática de discrição total."
                )
            )
        ),

        // ================= 16. AMIZADE (B1) =================
        Scene(
            id = "scene_amizade_call",
            title = "Late Night Phone Call",
            category = SceneCategory.AMIZADE,
            level = CefrLevel.B1,
            durationMinutes = 5,
            difficultyStars = 3,
            contextDescription = "Tarde da noite, uma amiga liga para desabafar sobre um dia difícil e pedir conselhos sinceros.",
            characters = listOf("Toby", "Megan"),
            genre = "Amizade & Social / Conversa Íntima",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("vent", "rough day", "hear out", "lean on", "cheer up"),
            expressions = listOf("Lean on me", "Hear me out", "Rough patch"),
            mediaConfig = SceneMediaConfig(durationSeconds = 300),
            phrases = listOf(
                ScenePhrase(
                    id = "amizade_2_p1",
                    characterName = "Megan",
                    fullForm = "I am sorry for calling so late, but I really need to vent.",
                    naturalForm = "I'm sorry for callin' so late, but I really gotta vent.",
                    portugueseTranslation = "Desculpe por ligar tão tarde, mas eu realmente preciso desabafar.",
                    acceptableTranslations = listOf(
                        "Desculpa ligar essa hora, mas eu preciso muito desabafar.",
                        "Foi mal ligar tão tarde, é que eu precisava botar pra fora."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I am", "I'm", "'I am' vira 'I'm'."),
                        ContractionPair("calling", "callin'", "Redução do '-ing' para '-in'' na conversa informal."),
                        ContractionPair("need to", "gotta", "'need to / have got to' vira 'gotta'.")
                    ),
                    vocabularyNotes = "'vent' = desabafar, botar para fora frustrações.",
                    grammarTip = "'Sorry for + verbo com ing' é a estrutura padrão de desculpas.",
                    additionalExample = "I'm sorry for interruptin', but I gotta ask.",
                    additionalExampleTranslation = "Desculpe interromper, mas preciso perguntar.",
                    blankSentence = "I'm having a rough time and I need to ______.",
                    blankCorrectAnswer = "vent",
                    blankOptions = listOf("vent", "sleep", "climb", "hide"),
                    quizQuestion = "O que significa o verbo 'to vent' no contexto de amizade?",
                    quizCorrectAnswer = "Desabafar sobre problemas e sentimentos",
                    quizOptions = listOf("Desabafar sobre problemas e sentimentos", "Ligar o ventilador", "Abrir a janela", "Cobrar uma dívida"),
                    quizExplanation = "'To vent' é o verbo específico para desabafo emocional."
                ),
                ScenePhrase(
                    id = "amizade_2_p2",
                    characterName = "Toby",
                    fullForm = "You do not have to apologize; that is what friends are here for.",
                    naturalForm = "You don't gotta apologize; that's what friends're here for.",
                    portugueseTranslation = "Você não tem que se desculpar; é para isso que servem os amigos.",
                    acceptableTranslations = listOf(
                        "Não precisa pedir desculpa; amigos são pra essas coisas.",
                        "Não esquenta com isso; é pra isso que os amigos servem."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("do not", "don't", "'do not' vira 'don't'."),
                        ContractionPair("have to", "gotta", "'have to' vira 'gotta'."),
                        ContractionPair("that is", "that's", "'that is' vira 'that's'."),
                        ContractionPair("friends are", "friends're", "'friends are' vira 'friends're'.")
                    ),
                    vocabularyNotes = "'apologize' = pedir desculpas. 'what friends are for' = para o que amigos servem.",
                    grammarTip = "Frase carinhosa clássica em filmes sobre amizade verdadeira.",
                    additionalExample = "You don't gotta worry, that's what family's here for.",
                    additionalExampleTranslation = "Você não precisa se preocupar, é para isso que a família serve.",
                    blankSentence = "You don't gotta apologize, ______ what I'm here for.",
                    blankCorrectAnswer = "that's",
                    blankOptions = listOf("that's", "this", "it", "there"),
                    quizQuestion = "O que expressa a frase 'That's what friends are for'?",
                    quizCorrectAnswer = "Apoio e acolhimento incondicional entre amigos",
                    quizOptions = listOf("Apoio e acolhimento incondicional entre amigos", "Cobrança por favores prestados", "Despedida fria", "Recusa em ajudar"),
                    quizExplanation = "Significa: 'Amigos servem exatamente para ajudar nessas horas'."
                )
            )
        ),

        // ================= 17. INVESTIGAÇÃO (B2) =================
        Scene(
            id = "scene_investigacao_noir",
            title = "Midnight Interrogation",
            category = SceneCategory.INVESTIGACAO,
            level = CefrLevel.B2,
            durationMinutes = 6,
            difficultyStars = 4,
            contextDescription = "Em uma delegacia chuvosa às duas da manhã, um detetive experiente confronta uma testemunha que está escondendo algo crucial.",
            characters = listOf("Detective Cole", "Witness Jack"),
            genre = "Investigação & Mistério / Policial Noir",
            imageResName = "scene_detective_noir",
            mainVocabulary = listOf("alibi", "shady", "spill the beans", "turn to", "cut the act"),
            expressions = listOf("Cut the act", "Turn to someone", "Spill the beans"),
            mediaConfig = SceneMediaConfig(durationSeconds = 360),
            phrases = listOf(
                ScenePhrase(
                    id = "investigacao_1_p1",
                    characterName = "Detective Cole",
                    fullForm = "You should not have come here tonight if you were not ready to talk.",
                    naturalForm = "You shouldn't've come here tonight if you weren't ready to talk.",
                    portugueseTranslation = "Você não deveria ter vindo aqui hoje à noite se não estivesse pronto para falar.",
                    acceptableTranslations = listOf(
                        "Você não devia ter vindo esta noite se não fosse pra abrir o jogo.",
                        "Não deveria ter vindo aqui se não estivesse disposto a colaborar."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("should not have", "shouldn't've", "'should not have' vira 'shouldn't've'."),
                        ContractionPair("were not", "weren't", "'were not' vira 'weren't'.")
                    ),
                    vocabularyNotes = "'ready to talk' = pronto para confessar ou depor. 'tonight' = esta noite.",
                    grammarTip = "'Shouldn't have + particípio' expressa censura de uma ação passada.",
                    additionalExample = "You shouldn't've hidden the truth if you weren't guilty.",
                    additionalExampleTranslation = "Você não deveria ter escondido a verdade se não fosse culpado.",
                    blankSentence = "You ______ come if you weren't ready to confess.",
                    blankCorrectAnswer = "shouldn't have",
                    blankOptions = listOf("shouldn't have", "mustn't have", "haven't", "won't have"),
                    quizQuestion = "O que 'shouldn't have come' comunica no interrogatório?",
                    quizCorrectAnswer = "Reprovação por uma atitude errada tomada no passado",
                    quizOptions = listOf("Reprovação por uma atitude errada tomada no passado", "Um convite amigável", "Uma ordem de prisão perpétua", "Uma dúvida sobre o endereço"),
                    quizExplanation = "'Shouldn't have' indica erro de julgamento no passado."
                ),
                ScenePhrase(
                    id = "investigacao_1_p2",
                    characterName = "Witness Jack",
                    fullForm = "I did not know who else to turn to, and they are definitely watching my house.",
                    naturalForm = "I didn't know who else to turn to, and they're definitely watching my house.",
                    portugueseTranslation = "Eu não sabia a quem mais recorrer, e eles com certeza estão vigiando minha casa.",
                    acceptableTranslations = listOf(
                        "Não tinha a quem mais pedir ajuda, e eles estão de olho na minha casa.",
                        "Eu não sabia a quem procurar, e eles com certeza tão vigiando meu apartamento."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("did not", "didn't", "'did not' vira 'didn't'."),
                        ContractionPair("they are", "they're", "'they are' vira 'they're'.")
                    ),
                    vocabularyNotes = "'turn to' = recorrer a alguém para proteção. 'watching' = vigiando/monitorando.",
                    grammarTip = "'Who else to turn to' é uma estrutura de infinitivo muito expressiva.",
                    additionalExample = "I didn't know where else to go, and they're following me.",
                    additionalExampleTranslation = "Não sabia para onde mais ir, e eles estão me seguindo.",
                    blankSentence = "I didn't know who else to ______ for help.",
                    blankCorrectAnswer = "turn to",
                    blankOptions = listOf("turn to", "turn on", "turn away", "turn over"),
                    quizQuestion = "O que significa o phrasal verb 'turn to someone'?",
                    quizCorrectAnswer = "Buscar socorro ou conselho com alguém",
                    quizOptions = listOf("Buscar socorro ou conselho com alguém", "Girar fisicamente", "Atacar alguém", "Fugir da polícia"),
                    quizExplanation = "'To turn to' significa procurar auxílio."
                )
            )
        ),

        // ================= 18. INVESTIGAÇÃO (B2) =================
        Scene(
            id = "scene_investigacao_heirloom",
            title = "The Missing Heirloom",
            category = SceneCategory.INVESTIGACAO,
            level = CefrLevel.B2,
            durationMinutes = 6,
            difficultyStars = 4,
            contextDescription = "Em uma mansão isolada na Inglaterra, um inspetor examina o cofre vazio após o desaparecimento de um colar secular.",
            characters = listOf("Inspector Sterling", "Lady Victoria"),
            genre = "Investigação & Mistério / Mansão Inglesa",
            imageResName = "scene_detective_noir",
            mainVocabulary = listOf("heirloom", "tampered", "inside job", "safe", "foul play"),
            expressions = listOf("Inside job", "Foul play", "Piece together"),
            mediaConfig = SceneMediaConfig(durationSeconds = 360),
            phrases = listOf(
                ScenePhrase(
                    id = "investigacao_2_p1",
                    characterName = "Inspector Sterling",
                    fullForm = "The lock was not broken, which means it must have been an inside job.",
                    naturalForm = "The lock wasn't broken, which means it must've been an inside job.",
                    portugueseTranslation = "A fechadura não foi arrombada, o que significa que deve ter sido alguém de dentro.",
                    acceptableTranslations = listOf(
                        "A tranca não foi forçada, ou seja, tem que ter sido trabalho interno.",
                        "A fechadura não tava violada, o que indica que foi alguém da própria casa."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("was not", "wasn't", "'was not' vira 'wasn't'."),
                        ContractionPair("must have", "must've", "'must have' dedução lógica vira 'must've'.")
                    ),
                    vocabularyNotes = "'lock broken' = fechadura arrombada. 'inside job' = crime cometido por alguém de dentro.",
                    grammarTip = "'Must have been' expressa dedução lógica quase certa sobre o passado.",
                    additionalExample = "The window wasn't open, so it must've been locked from inside.",
                    additionalExampleTranslation = "A janela não estava aberta, então deve ter sido trancada por dentro.",
                    blankSentence = "The safe wasn't forced, so it ______ been an inside job.",
                    blankCorrectAnswer = "must've",
                    blankOptions = listOf("must've", "should've", "wouldn't", "can't have"),
                    quizQuestion = "O que significa 'an inside job' em uma investigação policial?",
                    quizCorrectAnswer = "Crime facilitado ou praticado por alguém que pertence ao local",
                    quizOptions = listOf("Crime facilitado ou praticado por alguém que pertence ao local", "Trabalho feito em escritório fechado", "Crime cometido na chuva", "Mistério insolúvel"),
                    quizExplanation = "'Inside job' é a expressão clássica para golpe interno."
                ),
                ScenePhrase(
                    id = "investigacao_2_p2",
                    characterName = "Lady Victoria",
                    fullForm = "I cannot believe someone on my staff could have betrayed my trust.",
                    naturalForm = "I can't believe someone on my staff could've betrayed my trust.",
                    portugueseTranslation = "Não consigo acreditar que alguém da minha equipe pôde ter traído minha confiança.",
                    acceptableTranslations = listOf(
                        "Não acredito que alguém dos meus funcionários tenha traído minha confiança.",
                        "Inacreditável que alguém da minha casa tenha sido capaz de me trair."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("cannot", "can't", "'cannot' vira 'can't'."),
                        ContractionPair("could have", "could've", "'could have' vira 'could've'.")
                    ),
                    vocabularyNotes = "'staff' = equipe/funcionários. 'betrayed trust' = traiu a confiança.",
                    grammarTip = "'Could've betrayed' expressa a capacidade/possibilidade no passado.",
                    additionalExample = "I can't believe they could've kept this secret.",
                    additionalExampleTranslation = "Não acredito que eles possam ter guardado esse segredo.",
                    blankSentence = "I can't believe he ______ done such a thing.",
                    blankCorrectAnswer = "could've",
                    blankOptions = listOf("could've", "should've", "must've", "would've"),
                    quizQuestion = "O que 'betray one's trust' significa?",
                    quizCorrectAnswer = "Trair a confiança de alguém",
                    quizOptions = listOf("Trair a confiança de alguém", "Emprestar dinheiro", "Descobrir um colar", "Contratar empregados"),
                    quizExplanation = "Significa romper a lealdade depositada em alguém."
                )
            )
        ),

        // ================= 19. AÇÃO (C1) =================
        Scene(
            id = "scene_acao_heist",
            title = "The Art Gallery Heist",
            category = SceneCategory.ACAO,
            level = CefrLevel.C1,
            durationMinutes = 6,
            difficultyStars = 5,
            contextDescription = "Em um galpão abandonado em Londres, os membros de uma equipe de especialistas sincronizam relógios antes da invasão do museu.",
            characters = listOf("Arthur", "Elena"),
            genre = "Ação & Perseguição / Golpe Inteligente",
            imageResName = "scene_detective_noir",
            mainVocabulary = listOf("pull off", "blueprint", "power glitch", "silent alarm", "biometric lock"),
            expressions = listOf("Pull it off", "Get ahead of ourselves", "Cut the power"),
            mediaConfig = SceneMediaConfig(durationSeconds = 360),
            phrases = listOf(
                ScenePhrase(
                    id = "acao_1_p1",
                    characterName = "Arthur",
                    fullForm = "If there had not been a power glitch, we could not have pulled this off without tripping the alarm.",
                    naturalForm = "If there hadn't been a power glitch, we couldn't've pulled this off without tripping the alarm.",
                    portugueseTranslation = "Se não tivesse havido uma falha de energia, nós não teríamos conseguido sem disparar o alarme.",
                    acceptableTranslations = listOf(
                        "Se não fosse aquela pane de energia, a gente não teria dado conta sem acionar o alarme.",
                        "Sem aquela queda de força, não teríamos conseguido executar o plano sem disparar o alarme."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("had not", "hadn't", "'had not' vira 'hadn't'."),
                        ContractionPair("could not have", "couldn't've", "Contração tríplice avançada: 'could not have' vira 'couldn't've'.")
                    ),
                    vocabularyNotes = "'pull off' = realizar com sucesso algo improvável. 'trip an alarm' = disparar alarme.",
                    grammarTip = "Third Conditional com contração tríplice ('couldn't've') é o ápice da fala conectada.",
                    additionalExample = "If you hadn't arrived, we couldn't've finished the mission.",
                    additionalExampleTranslation = "Se você não tivesse chegado, não teríamos conseguido concluir a missão.",
                    blankSentence = "We couldn't've ______ this off without your help.",
                    blankCorrectAnswer = "pulled",
                    blankOptions = listOf("pulled", "pushed", "turned", "called"),
                    quizQuestion = "O que significa 'to pull something off'?",
                    quizCorrectAnswer = "Conseguir realizar com êxito algo incrivelmente difícil",
                    quizOptions = listOf("Conseguir realizar com êxito algo incrivelmente difícil", "Puxar a cortina do palco", "Desligar os cabos", "Abandonar o plano"),
                    quizExplanation = "'Pull off' é o idioma para vitória em missão complexa."
                ),
                ScenePhrase(
                    id = "acao_1_p2",
                    characterName = "Elena",
                    fullForm = "Let us not get ahead of ourselves; we still have got to bypass the vault biometric lock.",
                    naturalForm = "Let's not get ahead of ourselves; we've still gotta bypass the vault biometric lock.",
                    portugueseTranslation = "Não vamos nos precipitar; ainda temos que burlar a tranca biométrica do cofre.",
                    acceptableTranslations = listOf(
                        "Não vamos comemorar antes da hora; a gente ainda precisa contornar a trava biométrica do cofre.",
                        "Calma lá, não vamos cantar vitória; ainda temos que passar pela segurança do cofre."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("Let us", "Let's", "'Let us' vira 'Let's'."),
                        ContractionPair("we have", "we've", "'we have' vira 'we've'."),
                        ContractionPair("got to", "gotta", "'have got to' vira 'gotta'.")
                    ),
                    vocabularyNotes = "'get ahead of oneself' = precipitar-se. 'bypass' = burlar/contornar.",
                    grammarTip = "'Let's not...' é o imperativo compartilhado negativo.",
                    additionalExample = "Let's not celebrate yet; we've still gotta escape.",
                    additionalExampleTranslation = "Não vamos comemorar ainda; ainda temos que escapar.",
                    blankSentence = "Don't get ______ of yourself, the job isn't done.",
                    blankCorrectAnswer = "ahead",
                    blankOptions = listOf("ahead", "front", "back", "away"),
                    quizQuestion = "O que a expressão 'don't get ahead of yourself' adverte?",
                    quizCorrectAnswer = "Não cantar vitória antes da hora nem se precipitar",
                    quizOptions = listOf("Não cantar vitória antes da hora nem se precipitar", "Não correr na frente", "Não olhar para trás", "Não gastar o dinheiro"),
                    quizExplanation = "Equivale a 'não colocar a carroça na frente dos bois'."
                )
            )
        ),

        // ================= 20. AÇÃO (C1) =================
        Scene(
            id = "scene_acao_drone",
            title = "Rooftop Drone Pursuit",
            category = SceneCategory.ACAO,
            level = CefrLevel.C1,
            durationMinutes = 6,
            difficultyStars = 5,
            contextDescription = "Dois agentes especiais em fuga correm pelos telhados de Hong Kong enquanto despistam drones de vigilância.",
            characters = listOf("Agent Cross", "Tech Specialist Dex"),
            genre = "Ação & Perseguição / Espionagem",
            imageResName = "scene_detective_noir",
            mainVocabulary = listOf("evade", "radar", "jammer", "scramble", "perimeter"),
            expressions = listOf("Out of the woods", "Lock onto", "Clock is ticking"),
            mediaConfig = SceneMediaConfig(durationSeconds = 360),
            phrases = listOf(
                ScenePhrase(
                    id = "acao_2_p1",
                    characterName = "Agent Cross",
                    fullForm = "They would not have locked onto our signal if you had not left the tracker active.",
                    naturalForm = "They wouldn't've locked onto our signal if you hadn't left the tracker active.",
                    portugueseTranslation = "Eles não teriam rastreado nosso sinal se você não tivesse deixado o localizador ativo.",
                    acceptableTranslations = listOf(
                        "Eles não teriam travado a mira no nosso sinal se você não tivesse deixado o rastreador ligado.",
                        "Não teriam detectado nossa frequência se o localizador não tivesse ficado ativo."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("would not have", "wouldn't've", "'would not have' vira 'wouldn't've'."),
                        ContractionPair("had not", "hadn't", "'had not' vira 'hadn't'.")
                    ),
                    vocabularyNotes = "'lock onto' = travar mira / rastrear sinal fixo. 'tracker' = rastreador.",
                    grammarTip = "Inversão hipotética no passado com contração de três palavras.",
                    additionalExample = "They wouldn't've found us if you hadn't answered the phone.",
                    additionalExampleTranslation = "Eles não teriam nos achado se você não tivesse atendido o telefone.",
                    blankSentence = "They ______ tracked us without that beacon.",
                    blankCorrectAnswer = "wouldn't have",
                    blankOptions = listOf("wouldn't have", "shouldn't have", "mustn't have", "won't have"),
                    quizQuestion = "O que 'lock onto a signal' significa em termos técnicos?",
                    quizCorrectAnswer = "Localizar e fixar o rastreamento em uma frequência",
                    quizOptions = listOf("Localizar e fixar o rastreamento em uma frequência", "Destruir uma antena", "Perder o contato", "Desligar o rádio"),
                    quizExplanation = "Termo técnico para travamento de alvo ou sinal."
                ),
                ScenePhrase(
                    id = "acao_2_p2",
                    characterName = "Tech Specialist Dex",
                    fullForm = "I am scrambling their frequencies right now, so we will be in the clear in five seconds.",
                    naturalForm = "I'm scramblin' their frequencies right now, so we'll be in the clear in five seconds.",
                    portugueseTranslation = "Estou embaralhando as frequências deles agora, então estaremos livres em cinco segundos.",
                    acceptableTranslations = listOf(
                        "Tô bloqueando o sinal deles agora, a gente vai tá a salvo em cinco segundos.",
                        "Já tô bagunçando a frequência deles, tamo livre em 5 segundos."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I am", "I'm", "'I am' vira 'I'm'."),
                        ContractionPair("scrambling", "scramblin'", "Queda do 'g' no gerúndio informal."),
                        ContractionPair("we will", "we'll", "'we will' vira 'we'll'.")
                    ),
                    vocabularyNotes = "'scramble' = embaralhar/interferir em sinal. 'in the clear' = livre de perigo.",
                    grammarTip = "'In the clear' é o idiom militar para ausência de ameaça iminente.",
                    additionalExample = "Once we cross the bridge, we'll be in the clear.",
                    additionalExampleTranslation = "Assim que cruzarmos a ponte, estaremos fora de perigo.",
                    blankSentence = "Once the alarm stops, ______ be in the clear.",
                    blankCorrectAnswer = "we'll",
                    blankOptions = listOf("we'll", "we'd", "we're", "we've"),
                    quizQuestion = "O que significa a expressão 'in the clear'?",
                    quizCorrectAnswer = "Estar fora de perigo ou livre de suspeitas",
                    quizOptions = listOf("Estar fora de perigo ou livre de suspeitas", "Estar no meio da névoa", "Estar sem sinal de internet", "Estar desarmado"),
                    quizExplanation = "'In the clear' significa sem obstáculos ou perigo."
                )
            )
        ),

        // ================= 21. COMÉDIA (A1) =================
        Scene(
            id = "scene_comedia_cooking",
            title = "The Cooking Disaster",
            category = SceneCategory.COMEDIA,
            level = CefrLevel.A1,
            durationMinutes = 3,
            difficultyStars = 1,
            contextDescription = "Dois amigos tentam fazer um jantar simples, mas o alarme de fumaça dispara quando o bolo queima no forno.",
            characters = listOf("Oliver", "Ben"),
            genre = "Comédia & Sitcom / Confusão",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("oven", "smoke", "burnt", "fire alarm", "order takeout"),
            expressions = listOf("Burn to a crisp", "Order takeout", "Smoke alarm"),
            mediaConfig = SceneMediaConfig(durationSeconds = 180),
            phrases = listOf(
                ScenePhrase(
                    id = "comedia_1_p1",
                    characterName = "Oliver",
                    fullForm = "It is smoking! I did not know the oven was set so high!",
                    naturalForm = "It's smokin'! I didn't know the oven was set so high!",
                    portugueseTranslation = "Está saindo fumaça! Eu não sabia que o forno estava tão alto!",
                    acceptableTranslations = listOf(
                        "Tá fumegando! Não sabia que o forno tava tão forte!",
                        "Tá saindo muita fumaça! Não imaginei que o forno tava nessa temperatura!"
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("It is", "It's", "'It is' vira 'It's'."),
                        ContractionPair("smoking", "smokin'", "Redução de '-ing' para '-in'' na exclamação."),
                        ContractionPair("did not", "didn't", "'did not' vira 'didn't'.")
                    ),
                    vocabularyNotes = "'smokin'' = saindo fumaça. 'oven' = forno.",
                    grammarTip = "'Set so high' refere-se à temperatura ajustada no botão do fogão.",
                    additionalExample = "It's hot! I didn't touch the stove.",
                    additionalExampleTranslation = "Está quente! Eu não toquei no fogão.",
                    blankSentence = "It's smoking because I ______ check the timer.",
                    blankCorrectAnswer = "didn't",
                    blankOptions = listOf("didn't", "don't", "wasn't", "haven't"),
                    quizQuestion = "O que 'oven set so high' quer dizer no contexto de culinária?",
                    quizCorrectAnswer = "O forno estava em uma temperatura muito elevada",
                    quizOptions = listOf("O forno estava em uma temperatura muito elevada", "O forno estava no alto da parede", "O fogão era novo", "A cozinha estava cheia"),
                    quizExplanation = "'High' aqui é o nível de calor do forno."
                ),
                ScenePhrase(
                    id = "comedia_1_p2",
                    characterName = "Ben",
                    fullForm = "Do not panic, I will open the windows and we will order pizza instead.",
                    naturalForm = "Don't panic, I'll open the windows and we'll order pizza instead.",
                    portugueseTranslation = "Não entre em pânico, eu vou abrir as janelas e vamos pedir pizza em vez disso.",
                    acceptableTranslations = listOf(
                        "Calma, vou abrir as janelas e a gente pede pizza no lugar.",
                        "Não pira, eu abro as janelas e nós pedimos uma pizza."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("Do not", "Don't", "'Do not' vira 'Don't'."),
                        ContractionPair("I will", "I'll", "'I will' vira 'I'll'."),
                        ContractionPair("we will", "we'll", "'we will' vira 'we'll'.")
                    ),
                    vocabularyNotes = "'instead' = em vez disso/ao invés. 'order pizza' = pedir pizza.",
                    grammarTip = "'Instead' posiciona-se elegantemente no fim da frase para indicar alternativa.",
                    additionalExample = "Don't worry, we'll take a taxi instead.",
                    additionalExampleTranslation = "Não se preocupe, vamos de táxi em vez disso.",
                    blankSentence = "Don't worry, ______ order takeout.",
                    blankCorrectAnswer = "we'll",
                    blankOptions = listOf("we'll", "we'd", "we're", "we've"),
                    quizQuestion = "O que significa 'order pizza instead'?",
                    quizCorrectAnswer = "Pedir pizza como plano alternativo ao jantar queimado",
                    quizOptions = listOf("Pedir pizza como plano alternativo ao jantar queimado", "Nunca comer pizza", "Cozinhar a pizza no forno", "Vender pizza"),
                    quizExplanation = "'Instead' mostra a substituição do plano."
                )
            )
        ),

        // ================= 22. DRAMA (B2) =================
        Scene(
            id = "scene_drama_station",
            title = "Saying Goodbye at the Station",
            category = SceneCategory.DRAMA,
            level = CefrLevel.B2,
            durationMinutes = 6,
            difficultyStars = 4,
            contextDescription = "Em uma plataforma de trem com neblina, dois parceiros de vida precisam se despedir antes de uma viagem sem data de retorno.",
            characters = listOf("Julian", "Sophie"),
            genre = "Drama & Cinema / Despedida",
            imageResName = "scene_detective_noir",
            mainVocabulary = listOf("farewell", "platform", "cross paths", "cherish", "long distance"),
            expressions = listOf("Cross paths again", "Keep in touch", "Time stands still"),
            mediaConfig = SceneMediaConfig(durationSeconds = 360),
            phrases = listOf(
                ScenePhrase(
                    id = "drama_1_p1",
                    characterName = "Julian",
                    fullForm = "I cannot pretend that this does not break my heart into a million pieces.",
                    naturalForm = "I can't pretend that this doesn't break my heart into a million pieces.",
                    portugueseTranslation = "Não consigo fingir que isso não parte meu coração em um milhão de pedaços.",
                    acceptableTranslations = listOf(
                        "Não posso fingir que isso não despedaça o meu coração.",
                        "Não dá pra fingir que isso não me quebra por dentro."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("cannot", "can't", "'cannot' vira 'can't'."),
                        ContractionPair("does not", "doesn't", "'does not' vira 'doesn't'.")
                    ),
                    vocabularyNotes = "'pretend' = fingir. 'break my heart' = partir meu coração.",
                    grammarTip = "Atenção ao falso amigo: 'pretend' significa FINGIR (e não pretender).",
                    additionalExample = "I can't pretend that everything's fine when it's not.",
                    additionalExampleTranslation = "Não consigo fingir que tudo está bem quando não está.",
                    blankSentence = "I can't pretend it ______ hurt.",
                    blankCorrectAnswer = "doesn't",
                    blankOptions = listOf("doesn't", "don't", "isn't", "didn't"),
                    quizQuestion = "O que significa o verbo 'to pretend'?",
                    quizCorrectAnswer = "Fingir ou simular algo",
                    quizOptions = listOf("Fingir ou simular algo", "Ter intenção de fazer", "Proteger alguém", "Prometer solenemente"),
                    quizExplanation = "'Pretend' é um clássico falso cognato (significa fingir)."
                ),
                ScenePhrase(
                    id = "drama_1_p2",
                    characterName = "Sophie",
                    fullForm = "We will see each other again; I know our paths are going to cross.",
                    naturalForm = "We'll see each other again; I know our paths're gonna cross.",
                    portugueseTranslation = "Nós nos veremos de novo; eu sei que nossos caminhos vão se cruzar.",
                    acceptableTranslations = listOf(
                        "A gente vai se ver de novo; tenho certeza de que nossos caminhos vão se cruzar.",
                        "Nós vamos nos reencontrar; sei que nossos caminhos irão se encontrar."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("We will", "We'll", "'We will' vira 'We'll'."),
                        ContractionPair("paths are", "paths're", "'paths are' conecta em 'paths're'."),
                        ContractionPair("going to", "gonna", "'going to' vira 'gonna'.")
                    ),
                    vocabularyNotes = "'each other' = um ao outro mutuamente. 'paths cross' = caminhos se cruzarem.",
                    grammarTip = "'Cross paths' é a expressão poética para reencontros do destino.",
                    additionalExample = "We'll meet again 'cause our paths're meant to cross.",
                    additionalExampleTranslation = "Nos encontraremos de novo porque nossos caminhos devem se cruzar.",
                    blankSentence = "I know we'll meet again and our paths ______ cross.",
                    blankCorrectAnswer = "will",
                    blankOptions = listOf("will", "did", "have", "were"),
                    quizQuestion = "O que significa a expressão 'our paths will cross'?",
                    quizCorrectAnswer = "Nós iremos nos reencontrar no futuro",
                    quizOptions = listOf("Nós iremos nos reencontrar no futuro", "Nossos carros vão bater", "Vamos nos perder na estrada", "Nunca mais nos falaremos"),
                    quizExplanation = "'To cross paths' significa encontrar-se novamente."
                )
            )
        ),

        // ================= 23. TECNOLOGIA (B2) =================
        Scene(
            id = "scene_tecnologia_pitch",
            title = "Pitching the AI Startup",
            category = SceneCategory.TECNOLOGIA,
            level = CefrLevel.B2,
            durationMinutes = 6,
            difficultyStars = 4,
            contextDescription = "No Vale do Silício, um fundador apresenta sua plataforma de inteligência artificial generativa para uma investidora de capital de risco.",
            characters = listOf("Founder Lucas", "Investor Evelyn"),
            genre = "Tecnologia & Startups / Inovação",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("pitch", "scalability", "user retention", "machine learning", "seed round"),
            expressions = listOf("Game changer", "Bottom line", "Value proposition"),
            mediaConfig = SceneMediaConfig(durationSeconds = 360),
            phrases = listOf(
                ScenePhrase(
                    id = "tecnologia_1_p1",
                    characterName = "Founder Lucas",
                    fullForm = "Our platform is not just a wrapper; it is an intelligent multimodal engine.",
                    naturalForm = "Our platform isn't just a wrapper; it's an intelligent multimodal engine.",
                    portugueseTranslation = "Nossa plataforma não é só uma casca; é um motor multimodal inteligente.",
                    acceptableTranslations = listOf(
                        "Nossa plataforma não é apenas uma interface básica; trata-se de um motor inteligente.",
                        "Nosso produto não é só um wrapper; é uma engine multimodal completa."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("is not", "isn't", "'is not' vira 'isn't'."),
                        ContractionPair("it is", "it's", "'it is' vira 'it's'.")
                    ),
                    vocabularyNotes = "'wrapper' = jargão de software para interface rasa. 'engine' = motor/núcleo de IA.",
                    grammarTip = "'Not just... but...' enfatiza valor diferencial em apresentações de produto.",
                    additionalExample = "It isn't just an app, it's an entire ecosystem.",
                    additionalExampleTranslation = "Não é apenas um aplicativo, é um ecossistema inteiro.",
                    blankSentence = "Our tool ______ just a toy, it's powerful.",
                    blankCorrectAnswer = "isn't",
                    blankOptions = listOf("isn't", "aren't", "don't", "haven't"),
                    quizQuestion = "No contexto de startups de tecnologia, o que é um 'wrapper'?",
                    quizCorrectAnswer = "Uma camada superficial sobre uma API de terceiros",
                    quizOptions = listOf("Uma camada superficial sobre uma API de terceiros", "Um tipo de embalagem física", "Um vírus de computador", "Um contrato jurídico"),
                    quizExplanation = "'Wrapper' refere-se a software que apenas repassa dados sem valor próprio."
                ),
                ScenePhrase(
                    id = "tecnologia_1_p2",
                    characterName = "Investor Evelyn",
                    fullForm = "You have got impressive retention numbers, so we are ready to discuss the term sheet.",
                    naturalForm = "You've got impressive retention numbers, so we're ready to discuss the term sheet.",
                    portugueseTranslation = "Vocês têm números de retenção impressionantes, então estamos prontos para discutir a proposta de investimento.",
                    acceptableTranslations = listOf(
                        "Vocês têm uma retenção impressionante, então a gente tá pronto para discutir o contrato de aporte.",
                        "Suas métricas de retenção são ótimas, por isso estamos prontos para falar do investimento."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("You have", "You've", "'You have got' vira 'You've got'."),
                        ContractionPair("we are", "we're", "'we are' vira 'we're'.")
                    ),
                    vocabularyNotes = "'retention numbers' = métricas de retenção de clientes. 'term sheet' = proposta formal de investimento.",
                    grammarTip = "'You've got' é a forma mais natural para expressar que a empresa possui tais dados.",
                    additionalExample = "You've got great traction, so we're making an offer.",
                    additionalExampleTranslation = "Vocês têm ótima tração, por isso estamos fazendo uma oferta.",
                    blankSentence = "You've got good metrics, so ______ ready to invest.",
                    blankCorrectAnswer = "we're",
                    blankOptions = listOf("we're", "we'll", "we've", "we'd"),
                    quizQuestion = "O que significa 'term sheet' no mundo dos negócios e startups?",
                    quizCorrectAnswer = "Documento inicial com as condições de um investimento financeiro",
                    quizOptions = listOf("Documento inicial com as condições de um investimento financeiro", "Glossário de termos em inglês", "Manual do usuário", "Extrato bancário comum"),
                    quizExplanation = "'Term sheet' é o acordo preliminar de investimento."
                )
            )
        ),

        // ================= 24. TECNOLOGIA (C1) =================
        Scene(
            id = "scene_tecnologia_server",
            title = "Server Meltdown at 3 AM",
            category = SceneCategory.TECNOLOGIA,
            level = CefrLevel.C1,
            durationMinutes = 6,
            difficultyStars = 5,
            contextDescription = "Às três da manhã, engenheiros de infraestrutura tentam conter uma sobrecarga massiva no banco de dados durante a Black Friday.",
            characters = listOf("DevOps Lead Kevin", "Engineer Maya"),
            genre = "Tecnologia & Startups / Alta Pressão",
            imageResName = "scene_detective_noir",
            mainVocabulary = listOf("meltdown", "database shard", "failover", "bottleneck", "latency spike"),
            expressions = listOf("Put out fires", "All hands on deck", "Back against the wall"),
            mediaConfig = SceneMediaConfig(durationSeconds = 360),
            phrases = listOf(
                ScenePhrase(
                    id = "tecnologia_2_p1",
                    characterName = "DevOps Lead Kevin",
                    fullForm = "If we had not enabled auto-scaling, the production cluster would have crashed twenty minutes ago.",
                    naturalForm = "If we hadn't enabled auto-scaling, the production cluster would've crashed twenty minutes ago.",
                    portugueseTranslation = "Se não tivéssemos habilitado o escalonamento automático, o cluster de produção teria caído há vinte minutos.",
                    acceptableTranslations = listOf(
                        "Se a gente não tivesse ativado o auto-scaling, os servidores de produção teriam caído 20 minutos atrás.",
                        "Sem o escalonamento automático, toda a infraestrutura teria desabado vinte minutos atrás."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("had not", "hadn't", "'had not' vira 'hadn't'."),
                        ContractionPair("would have", "would've", "'would have' vira 'would've'.")
                    ),
                    vocabularyNotes = "'auto-scaling' = redimensionamento automático de servidores. 'cluster crashed' = servidores caíram.",
                    grammarTip = "Third conditional com vocabulário técnico de alta disponibilidade.",
                    additionalExample = "If we hadn't backed up the database, we would've lost everything.",
                    additionalExampleTranslation = "Se não tivéssemos feito o backup do banco de dados, teríamos perdido tudo.",
                    blankSentence = "Without the patch, the server ______ crashed.",
                    blankCorrectAnswer = "would've",
                    blankOptions = listOf("would've", "should've", "must've", "couldn't"),
                    quizQuestion = "O que 'auto-scaling' faz em servidores de tecnologia?",
                    quizCorrectAnswer = "Aumenta a capacidade de máquinas automaticamente sob alta demanda",
                    quizOptions = listOf("Aumenta a capacidade de máquinas automaticamente sob alta demanda", "Desliga os computadores à noite", "Mede o peso dos aparelhos", "Exclui contas antigas"),
                    quizExplanation = "'Auto-scaling' provisiona poder computacional conforme o tráfego aumenta."
                ),
                ScenePhrase(
                    id = "tecnologia_2_p2",
                    characterName = "Engineer Maya",
                    fullForm = "I have already rerouted the traffic, so it is just a matter of minutes until the latency drops.",
                    naturalForm = "I've already rerouted the traffic, so it's just a matter of minutes until the latency drops.",
                    portugueseTranslation = "Eu já redirecionei o tráfego, então é só uma questão de minutos até a latência cair.",
                    acceptableTranslations = listOf(
                        "Já fiz o desvio do tráfego, é questão de minutos pro tempo de resposta baixar.",
                        "Já redirecionei as requisições, então a lentidão vai sumir em poucos minutos."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I have", "I've", "'I have' vira 'I've'."),
                        ContractionPair("it is", "it's", "'it is' vira 'it's'.")
                    ),
                    vocabularyNotes = "'rerouted' = redirecionou. 'latency drops' = tempo de resposta diminui.",
                    grammarTip = "'Just a matter of minutes' é a expressão para 'apenas questão de minutos'.",
                    additionalExample = "I've deployed the fix, so it's just a matter of time.",
                    additionalExampleTranslation = "Já publiquei a correção, então é apenas questão de tempo.",
                    blankSentence = "I've deployed the update, so ______ just a matter of minutes.",
                    blankCorrectAnswer = "it's",
                    blankOptions = listOf("it's", "its", "is", "he's"),
                    quizQuestion = "O que significa 'latency' em sistemas computacionais?",
                    quizCorrectAnswer = "Tempo de atraso ou resposta na transmissão de dados",
                    quizOptions = listOf("Tempo de atraso ou resposta na transmissão de dados", "Espaço em disco rígido", "Preço da energia elétrica", "Número de monitores"),
                    quizExplanation = "'Latency' é a métrica de tempo de resposta de um sistema."
                )
            )
        )
    )
}

package com.example.data.local

import com.example.data.model.CefrLevel
import com.example.data.model.PronunciationCategory
import com.example.data.model.PronunciationExerciseItem

object PrepopulatedPronunciation {

    val allExercises: List<PronunciationExerciseItem> = listOf(
        // CONTRACTIONS
        PronunciationExerciseItem(
            id = "pron_cont_1",
            category = PronunciationCategory.CONTRACTIONS,
            title = "I'm going to the store",
            targetPhrase = "I'm going to the store.",
            naturalSpokenForm = "I'm going to the store.",
            portugueseTranslation = "Eu estou indo ao mercado.",
            focusConcept = "Contração I + am = I'm",
            explanation = "Junte 'I' e 'am' em uma única sílaba /aɪm/ sem pronunciar o 'am' separado.",
            pronunciationTip = "Feche os lábios no final no som de /m/ antes de começar 'going'.",
            difficulty = CefrLevel.A1
        ),
        PronunciationExerciseItem(
            id = "pron_cont_2",
            category = PronunciationCategory.CONTRACTIONS,
            title = "We haven't seen them yet",
            targetPhrase = "We haven't seen them yet.",
            naturalSpokenForm = "We haven't seen them yet.",
            portugueseTranslation = "Nós ainda não os vimos.",
            focusConcept = "Contração have + not = haven't",
            explanation = "'Haven't' soa como /ˈhæv.ənt/, com a terminação /nt/ bem rápida.",
            pronunciationTip = "A ponta da língua toca o céu da boca suavemente no /nt/.",
            difficulty = CefrLevel.A2
        ),
        PronunciationExerciseItem(
            id = "pron_cont_3",
            category = PronunciationCategory.CONTRACTIONS,
            title = "You shouldn't've done that",
            targetPhrase = "You shouldn't've done that.",
            naturalSpokenForm = "You shouldn't've done that.",
            portugueseTranslation = "Você não deveria ter feito isso.",
            focusConcept = "Dupla contração: should + not + have = shouldn't've",
            explanation = "Em filmes e séries, a forma falada soa como /ˈʃʊd.ənt.əv/ em uma respiração.",
            pronunciationTip = "Não pause entre 'shouldn't' e ''ve' — deslize os sons.",
            difficulty = CefrLevel.B2
        ),
        PronunciationExerciseItem(
            id = "pron_cont_4",
            category = PronunciationCategory.CONTRACTIONS,
            title = "There's something I need to tell you",
            targetPhrase = "There's something I need to tell you.",
            naturalSpokenForm = "There's something I need to tell you.",
            portugueseTranslation = "Tem uma coisa que preciso te contar.",
            focusConcept = "Contração There + is = There's",
            explanation = "O 'There is' vira /ðɛərz/ direto para o 'something'.",
            pronunciationTip = "Mantenha a língua entre os dentes para o 'Th' e solte no /z/ contínuo.",
            difficulty = CefrLevel.B1
        ),

        // CONNECTED SPEECH
        PronunciationExerciseItem(
            id = "pron_conn_1",
            category = PronunciationCategory.CONNECTED_SPEECH,
            title = "What are you doing tonight?",
            targetPhrase = "What are you doing tonight?",
            naturalSpokenForm = "Whatcha doin' tonight?",
            portugueseTranslation = "O que você vai fazer hoje à noite?",
            focusConcept = "Assimilação /t/ + /j/ = /tʃ/ ('Whatcha')",
            explanation = "A junção de 'What' com 'are you' vira o clássico 'Whatcha' nos diálogos naturais.",
            pronunciationTip = "Pronuncie como 'uót-cha' sem separar cada palavra individualmente.",
            difficulty = CefrLevel.A2
        ),
        PronunciationExerciseItem(
            id = "pron_conn_2",
            category = PronunciationCategory.CONNECTED_SPEECH,
            title = "I want to get out of here",
            targetPhrase = "I want to get out of here.",
            naturalSpokenForm = "I wanna get outta here.",
            portugueseTranslation = "Eu quero sair daqui.",
            focusConcept = "Linking: want to -> wanna | out of -> outta",
            explanation = "Duas conexões principais que dão o ritmo característico do inglês falado.",
            pronunciationTip = "O 'out of' soa como 'áuta' com som de /t/ suave (flap T).",
            difficulty = CefrLevel.B1
        ),
        PronunciationExerciseItem(
            id = "pron_conn_3",
            category = PronunciationCategory.CONNECTED_SPEECH,
            title = "Could you give me a minute?",
            targetPhrase = "Could you give me a minute?",
            naturalSpokenForm = "Couldja gimme a minute?",
            portugueseTranslation = "Você poderia me dar um minuto?",
            focusConcept = "Junção Could you -> /kʊdʒə/ | give me -> /gɪmi/",
            explanation = "O /d/ antes de 'you' vira som de /dʒ/ (como 'dja'), e 'give me' vira 'gimme'.",
            pronunciationTip = "Faça a frase inteira soar como uma onda fluida sem interrupções.",
            difficulty = CefrLevel.B2
        ),

        // WORD REDUCTION (Weak Forms & Schwa)
        PronunciationExerciseItem(
            id = "pron_red_1",
            category = PronunciationCategory.WORD_REDUCTION,
            title = "Fish and chips for dinner",
            targetPhrase = "Fish and chips for dinner.",
            naturalSpokenForm = "Fish 'n' chips fer dinner.",
            portugueseTranslation = "Peixe com batatas fritas para o jantar.",
            focusConcept = "Redução de 'and' para /ən/ e 'for' para /fər/",
            explanation = "Palavras gramaticais (and, for) quase nunca são enfatizadas, virando sons neutros.",
            pronunciationTip = "Gaste 80% do tempo em 'Fish', 'chips' e 'dinner'; 'and' e 'for' são rápidos como um sussurro.",
            difficulty = CefrLevel.A2
        ),
        PronunciationExerciseItem(
            id = "pron_red_2",
            category = PronunciationCategory.WORD_REDUCTION,
            title = "I have to go to the bank",
            targetPhrase = "I have to go to the bank.",
            naturalSpokenForm = "I hafta go tə the bank.",
            portugueseTranslation = "Eu tenho que ir ao banco.",
            focusConcept = "Redução da preposição 'to' para som Schwa /tə/",
            explanation = "Não pronuncie 'to' como 'tuuu' longo. Ele soa como um 'tã' neutro e ultrarrápido.",
            pronunciationTip = "A boca fica quase relaxada na vogal Schwa /ə/.",
            difficulty = CefrLevel.A2
        ),
        PronunciationExerciseItem(
            id = "pron_red_3",
            category = PronunciationCategory.WORD_REDUCTION,
            title = "A cup of coffee, please",
            targetPhrase = "A cup of coffee, please.",
            naturalSpokenForm = "A cuppa coffee, please.",
            portugueseTranslation = "Uma xícara de café, por favor.",
            focusConcept = "Redução de 'of' para /əv/ ou /ə/ ('cuppa')",
            explanation = "A preposição 'of' perde o som forte de 'óv' e conecta na palavra anterior.",
            pronunciationTip = "Diga 'cáp-a cófi' com ênfase no 'CUP' e no 'COF'.",
            difficulty = CefrLevel.B1
        ),

        // RHYTHM & STRESS-TIMING
        PronunciationExerciseItem(
            id = "pron_rhy_1",
            category = PronunciationCategory.RHYTHM,
            title = "I bought a car for my sister",
            targetPhrase = "I bought a car for my sister.",
            naturalSpokenForm = "I BOUGHT a CAR for my SISTER.",
            portugueseTranslation = "Eu comprei um carro para a minha irmã.",
            focusConcept = "Stress-timing: Content words vs Function words",
            explanation = "Em inglês o tempo é medido pelas batidas fortes: BOUGHT, CAR, SISTER.",
            pronunciationTip = "Bata o pé ou o dedo em cada uma das 3 palavras em maiúsculas.",
            difficulty = CefrLevel.B1
        ),
        PronunciationExerciseItem(
            id = "pron_rhy_2",
            category = PronunciationCategory.RHYTHM,
            title = "Cats like eating fresh fish",
            targetPhrase = "Cats like eating fresh fish.",
            naturalSpokenForm = "CATS LIKE EATING FRESH FISH.",
            portugueseTranslation = "Gatos gostam de comer peixe fresco.",
            focusConcept = "Todas as palavras com carga de conteúdo (5 batidas iguais)",
            explanation = "Como quase todas as palavras carregam significado, cada uma recebe uma batida rítmica clara.",
            pronunciationTip = "Mantenha o mesmo intervalo de tempo entre cada palavra.",
            difficulty = CefrLevel.B2
        ),
        PronunciationExerciseItem(
            id = "pron_rhy_3",
            category = PronunciationCategory.RHYTHM,
            title = "We need to talk about the plan",
            targetPhrase = "We need to talk about the plan.",
            naturalSpokenForm = "We NEED to TALK about the PLAN.",
            portugueseTranslation = "Nós precisamos conversar sobre o plano.",
            focusConcept = "Batidas principais em NEED, TALK e PLAN",
            explanation = "As palavras 'we', 'to', 'about the' são comprimidas para caber entre os pulsos fortes.",
            pronunciationTip = "Não pause antes de 'about the' — passe correndo até pousar firme em 'PLAN'.",
            difficulty = CefrLevel.B1
        ),

        // INTONATION & MELODY
        PronunciationExerciseItem(
            id = "pron_int_1",
            category = PronunciationCategory.INTONATION,
            title = "Are you ready to go?",
            targetPhrase = "Are you ready to go?",
            naturalSpokenForm = "Are you ready to go? ↗️",
            portugueseTranslation = "Você está pronto para ir?",
            focusConcept = "Rising Intonation (Entonação Ascendente em Yes/No questions)",
            explanation = "Perguntas de 'Sim ou Não' fazem o tom da voz subir no final da frase.",
            pronunciationTip = "Eleve a nota da sua voz na palavra 'GO' como se estivesse fazendo uma curva para cima.",
            difficulty = CefrLevel.A1
        ),
        PronunciationExerciseItem(
            id = "pron_int_2",
            category = PronunciationCategory.INTONATION,
            title = "Where did you put the keys?",
            targetPhrase = "Where did you put the keys?",
            naturalSpokenForm = "Where did you put the keys? ↘️",
            portugueseTranslation = "Onde você colocou as chaves?",
            focusConcept = "Falling Intonation (Entonação Descendente em Wh- questions)",
            explanation = "Perguntas que começam com Where, What, Why terminam com o tom da voz descendo.",
            pronunciationTip = "Comece com tom firme em 'Where' e baixe a voz ao terminar em 'keys'.",
            difficulty = CefrLevel.A2
        ),
        PronunciationExerciseItem(
            id = "pron_int_3",
            category = PronunciationCategory.INTONATION,
            title = "Really? You're actually moving?",
            targetPhrase = "Really? You're actually moving?",
            naturalSpokenForm = "Really?! ↗️ You're actually moving?! ↗️",
            portugueseTranslation = "Sério?! Você vai mesmo se mudar?!",
            focusConcept = "High-Rising Pitch de Surpresa / Emoção Dramática",
            explanation = "Diálogos de filmes usam picos de tom para expressar choque e surpresa genuína.",
            pronunciationTip = "Aumente a energia e o tom nos dois pontos de interrogação.",
            difficulty = CefrLevel.B2
        ),
        PronunciationExerciseItem(
            id = "pron_int_4",
            category = PronunciationCategory.INTONATION,
            title = "I like coffee, tea, and juice",
            targetPhrase = "I like coffee, tea, and juice.",
            naturalSpokenForm = "I like coffee ↗️, tea ↗️, and juice ↘️.",
            portugueseTranslation = "Eu gosto de café, chá e suco.",
            focusConcept = "List Intonation: sobe nos itens intermediários e desce no último",
            explanation = "Em enumerações, a voz sobe em cada item para indicar que a lista continua, e desce no final.",
            pronunciationTip = "Suba o tom em 'coffee' e 'tea', e conclua descendo firme em 'juice'.",
            difficulty = CefrLevel.A2
        )
    )
}

package com.example.data.model

enum class ContractionCategory(val titlePt: String) {
    TO_BE("Verbo TO BE"),
    NEGATIONS("Negações"),
    AUXILIARIES("Auxiliares & Futuro"),
    MODALS("Modais & Condicionais"),
    MOVIES_INFORMAL("Informal de Filmes")
}

data class ContractionRule(
    val id: String,
    val fullForm: String,
    val contractedForm: String,
    val category: ContractionCategory,
    val explanationPt: String,
    val exampleSentenceFull: String,
    val exampleSentenceContracted: String,
    val translationPt: String,
    val whenToAvoid: String = "Evite em redações formais, artigos acadêmicos ou documentos jurídicos."
)

object ContractionCatalog {
    val allRules: List<ContractionRule> = listOf(
        ContractionRule(
            id = "c_i_am",
            fullForm = "I am",
            contractedForm = "I'm",
            category = ContractionCategory.TO_BE,
            explanationPt = "Junção do pronome 'I' com o verbo 'am'. O som do 'a' desaparece.",
            exampleSentenceFull = "I am ready to leave now.",
            exampleSentenceContracted = "I'm ready to leave now.",
            translationPt = "Estou pronto para sair agora."
        ),
        ContractionRule(
            id = "c_you_are",
            fullForm = "You are",
            contractedForm = "You're",
            category = ContractionCategory.TO_BE,
            explanationPt = "Junção de 'You' e 'are'. Cuidado para não confundir com o possessivo 'your'!",
            exampleSentenceFull = "You are doing a fantastic job.",
            exampleSentenceContracted = "You're doing a fantastic job.",
            translationPt = "Você está fazendo um trabalho fantástico."
        ),
        ContractionRule(
            id = "c_he_is",
            fullForm = "He is",
            contractedForm = "He's",
            category = ContractionCategory.TO_BE,
            explanationPt = "Junção de 'He' e 'is'. Também pode ser a contração de 'He has' no present perfect.",
            exampleSentenceFull = "He is waiting outside the door.",
            exampleSentenceContracted = "He's waiting outside the door.",
            translationPt = "Ele está esperando do lado de fora da porta."
        ),
        ContractionRule(
            id = "c_she_is",
            fullForm = "She is",
            contractedForm = "She's",
            category = ContractionCategory.TO_BE,
            explanationPt = "Junção de 'She' e 'is'. Soa muito mais fluido e natural em diálogos falados.",
            exampleSentenceFull = "She is not going to accept this excuse.",
            exampleSentenceContracted = "She's not going to accept this excuse.",
            translationPt = "Ela não vai aceitar essa desculpa."
        ),
        ContractionRule(
            id = "c_it_is",
            fullForm = "It is",
            contractedForm = "It's",
            category = ContractionCategory.TO_BE,
            explanationPt = "Junção de 'It' e 'is'. 'It's' tem apóstrofo; 'its' sem apóstrofo é o pronome possessivo dele/dela.",
            exampleSentenceFull = "It is time to make a decision.",
            exampleSentenceContracted = "It's time to make a decision.",
            translationPt = "É hora de tomar uma decisão."
        ),
        ContractionRule(
            id = "c_we_are",
            fullForm = "We are",
            contractedForm = "We're",
            category = ContractionCategory.TO_BE,
            explanationPt = "Junção de 'We' e 'are'. A pronúncia soa semelhante a 'weer'.",
            exampleSentenceFull = "We are running out of time.",
            exampleSentenceContracted = "We're running out of time.",
            translationPt = "Estamos ficando sem tempo."
        ),
        ContractionRule(
            id = "c_they_are",
            fullForm = "They are",
            contractedForm = "They're",
            category = ContractionCategory.TO_BE,
            explanationPt = "Junção de 'They' e 'are'. Tem a mesma pronúncia que 'there' e 'their'.",
            exampleSentenceFull = "They are planning a surprise party.",
            exampleSentenceContracted = "They're planning a surprise party.",
            translationPt = "Eles estão planejando uma festa surpresa."
        ),
        ContractionRule(
            id = "c_do_not",
            fullForm = "Do not",
            contractedForm = "Don't",
            category = ContractionCategory.NEGATIONS,
            explanationPt = "A negação mais comum no presente simples em conversas normais.",
            exampleSentenceFull = "I do not know what you mean.",
            exampleSentenceContracted = "I don't know what you mean.",
            translationPt = "Eu não sei o que você quer dizer."
        ),
        ContractionRule(
            id = "c_does_not",
            fullForm = "Does not",
            contractedForm = "Doesn't",
            category = ContractionCategory.NEGATIONS,
            explanationPt = "Negação para he/she/it no presente simples.",
            exampleSentenceFull = "She does not live here anymore.",
            exampleSentenceContracted = "She doesn't live here anymore.",
            translationPt = "Ela não mora mais aqui."
        ),
        ContractionRule(
            id = "c_did_not",
            fullForm = "Did not",
            contractedForm = "Didn't",
            category = ContractionCategory.NEGATIONS,
            explanationPt = "Negação padrão para o passado simples em todos os pronomes.",
            exampleSentenceFull = "We did not see anything suspicious.",
            exampleSentenceContracted = "We didn't see anything suspicious.",
            translationPt = "Nós não vimos nada de suspeito."
        ),
        ContractionRule(
            id = "c_cannot",
            fullForm = "Cannot",
            contractedForm = "Can't",
            category = ContractionCategory.MODALS,
            explanationPt = "Indica incapacidade ou proibição. No inglês americano soa como 'kænt'.",
            exampleSentenceFull = "I cannot believe you did that.",
            exampleSentenceContracted = "I can't believe you did that.",
            translationPt = "Eu não consigo acreditar que você fez isso."
        ),
        ContractionRule(
            id = "c_could_not",
            fullForm = "Could not",
            contractedForm = "Couldn't",
            category = ContractionCategory.MODALS,
            explanationPt = "Passado de can't ou possibilidade negativa no condicional.",
            exampleSentenceFull = "He could not find his keys.",
            exampleSentenceContracted = "He couldn't find his keys.",
            translationPt = "Ele não conseguiu encontrar as chaves dele."
        ),
        ContractionRule(
            id = "c_would_not",
            fullForm = "Would not",
            contractedForm = "Wouldn't",
            category = ContractionCategory.MODALS,
            explanationPt = "Negação condicional. Usado frequentemente em diálogos dramáticos.",
            exampleSentenceFull = "I would not do that if I were you.",
            exampleSentenceContracted = "I wouldn't do that if I were you.",
            translationPt = "Eu não faria isso se fosse você."
        ),
        ContractionRule(
            id = "c_should_not",
            fullForm = "Should not",
            contractedForm = "Shouldn't",
            category = ContractionCategory.MODALS,
            explanationPt = "Expressa conselho ou recomendação negativa.",
            exampleSentenceFull = "You should not walk alone at night.",
            exampleSentenceContracted = "You shouldn't walk alone at night.",
            translationPt = "Você não deveria andar sozinho à noite."
        ),
        ContractionRule(
            id = "c_will_not",
            fullForm = "Will not",
            contractedForm = "Won't",
            category = ContractionCategory.AUXILIARIES,
            explanationPt = "Atenção: 'will not' se transforma em 'won't' (mudança fonética e ortográfica histórica).",
            exampleSentenceFull = "I will not let you down.",
            exampleSentenceContracted = "I won't let you down.",
            translationPt = "Eu não vou te decepcionar."
        ),
        ContractionRule(
            id = "c_i_will",
            fullForm = "I will",
            contractedForm = "I'll",
            category = ContractionCategory.AUXILIARIES,
            explanationPt = "Futuro imediato ou promessa rápida no diálogo.",
            exampleSentenceFull = "I will call you as soon as I arrive.",
            exampleSentenceContracted = "I'll call you as soon as I arrive.",
            translationPt = "Eu vou te ligar assim que eu chegar."
        ),
        ContractionRule(
            id = "c_i_have",
            fullForm = "I have",
            contractedForm = "I've",
            category = ContractionCategory.AUXILIARIES,
            explanationPt = "Usado no Present Perfect. 'I have got' vira 'I've got'.",
            exampleSentenceFull = "I have got a bad feeling about this.",
            exampleSentenceContracted = "I've got a bad feeling about this.",
            translationPt = "Estou com um mau pressentimento sobre isso."
        ),
        ContractionRule(
            id = "c_you_have",
            fullForm = "You have",
            contractedForm = "You've",
            category = ContractionCategory.AUXILIARIES,
            explanationPt = "Junção de 'You' com 'have'.",
            exampleSentenceFull = "You have been working too hard.",
            exampleSentenceContracted = "You've been working too hard.",
            translationPt = "Você tem trabalhado duro demais."
        ),
        ContractionRule(
            id = "c_i_would",
            fullForm = "I would",
            contractedForm = "I'd",
            category = ContractionCategory.MODALS,
            explanationPt = "Contração de 'I would' (ou 'I had'). Contexto do verbo seguinte define o sentido.",
            exampleSentenceFull = "I would love to help you with that.",
            exampleSentenceContracted = "I'd love to help you with that.",
            translationPt = "Eu adoraria te ajudar com isso."
        ),
        ContractionRule(
            id = "c_is_not",
            fullForm = "Is not",
            contractedForm = "Isn't",
            category = ContractionCategory.NEGATIONS,
            explanationPt = "Negação de terceira pessoa do singular no presente.",
            exampleSentenceFull = "This is not what we agreed on.",
            exampleSentenceContracted = "This isn't what we agreed on.",
            translationPt = "Isso não é o que nós combinamos."
        ),
        ContractionRule(
            id = "c_are_not",
            fullForm = "Are not",
            contractedForm = "Aren't",
            category = ContractionCategory.NEGATIONS,
            explanationPt = "Negação para plural e segunda pessoa.",
            exampleSentenceFull = "They are not coming to the meeting.",
            exampleSentenceContracted = "They aren't coming to the meeting.",
            translationPt = "Eles não estão vindo para a reunião."
        ),
        ContractionRule(
            id = "c_was_not",
            fullForm = "Was not",
            contractedForm = "Wasn't",
            category = ContractionCategory.NEGATIONS,
            explanationPt = "Negação do verbo to be no passado singular.",
            exampleSentenceFull = "It was not my fault.",
            exampleSentenceContracted = "It wasn't my fault.",
            translationPt = "Não foi minha culpa."
        ),
        ContractionRule(
            id = "c_were_not",
            fullForm = "Were not",
            contractedForm = "Weren't",
            category = ContractionCategory.NEGATIONS,
            explanationPt = "Negação do verbo to be no passado plural.",
            exampleSentenceFull = "We were not prepared for this storm.",
            exampleSentenceContracted = "We weren't prepared for this storm.",
            translationPt = "Nós não estávamos preparados para esta tempestade."
        ),
        ContractionRule(
            id = "c_have_not",
            fullForm = "Have not",
            contractedForm = "Haven't",
            category = ContractionCategory.NEGATIONS,
            explanationPt = "Present Perfect na negativa.",
            exampleSentenceFull = "I have not seen him since yesterday.",
            exampleSentenceContracted = "I haven't seen him since yesterday.",
            translationPt = "Eu não o vejo desde ontem."
        ),
        ContractionRule(
            id = "c_has_not",
            fullForm = "Has not",
            contractedForm = "Hasn't",
            category = ContractionCategory.NEGATIONS,
            explanationPt = "Present Perfect na negativa para he/she/it.",
            exampleSentenceFull = "The train has not arrived yet.",
            exampleSentenceContracted = "The train hasn't arrived yet.",
            translationPt = "O trem ainda não chegou."
        ),
        ContractionRule(
            id = "c_going_to",
            fullForm = "Going to",
            contractedForm = "Gonna",
            category = ContractionCategory.MOVIES_INFORMAL,
            explanationPt = "Muito comum na fala rápida de séries e filmes. Seguido sempre por verbo no infinitivo.",
            exampleSentenceFull = "I am going to tell you the truth.",
            exampleSentenceContracted = "I'm gonna tell you the truth.",
            translationPt = "Eu vou te contar a verdade.",
            whenToAvoid = "Estritamente informal. Nunca utilize em redações formais ou provas."
        ),
        ContractionRule(
            id = "c_want_to",
            fullForm = "Want to",
            contractedForm = "Wanna",
            category = ContractionCategory.MOVIES_INFORMAL,
            explanationPt = "Redução fonética ultra comum no inglês falado.",
            exampleSentenceFull = "Do you want to grab some coffee?",
            exampleSentenceContracted = "Do you wanna grab some coffee?",
            translationPt = "Você quer tomar um café?",
            whenToAvoid = "Use apenas na fala e conversas casuais."
        ),
        ContractionRule(
            id = "c_have_to",
            fullForm = "Have to",
            contractedForm = "Gotta",
            category = ContractionCategory.MOVIES_INFORMAL,
            explanationPt = "De 'have got to' / 'have to' indicando obrigação.",
            exampleSentenceFull = "I have to get out of here.",
            exampleSentenceContracted = "I gotta get out of here.",
            translationPt = "Eu tenho que sair daqui.",
            whenToAvoid = "Uso oral informal."
        )
    )
}

package dutrabot

import kotlin.properties.Delegates

class PremiumBot(model: NNModel) : AdvancedBot(model) {

    /**
     * Ações que o robô consegue realizar
     */
    private val actions: Map<String, (text: String) -> String> = mapOf(
        "fazer café" to { text -> "$text Fazendo café..." },
        "subir escadas" to { text -> "$text Subindo escadas..." }
    )

    /**
     * Trata a resposta antes de retorna-la
     *
     * @param answerIdx Indice da resposta
     * @param text Texto enviado pelo utilizador
     * @return Texto de resposta
     */
    override fun answer(answerIdx: Int, text: String): String {
        var answerText by Delegates.notNull<String>()

        if (answerIdx < 8) answerText = super.answer(answerIdx, text)
        else {
            answerText = botResponses[answerIdx]!!
            val actionKey = actions.keys.firstOrNull { text.lowercase().contains(it) }
            if (actionKey != null) answerText = actions[actionKey]!!.invoke(answerText)
        }

        return answerText
    }
}
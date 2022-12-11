package dutrabot

import dutrabot.helpers.operations

open class AdvancedBot(model: NNModel) : Bot(model) {

    /**
     * Trata a resposta antes de retorna-la
     *
     * @param answerIdx Indice da resposta
     * @param text Texto enviado pelo utilizador
     * @return Texto de resposta
     */
    override fun answer(answerIdx: Int, text: String): String {
        var sAnswer = botResponses[if (answerIdx > 7) 6 else answerIdx]!!

        if (answerIdx == 7) {
            val solution = getOperationSolution(text)
            sAnswer = "$sAnswer A resposta é '$solution'."
        }

        return sAnswer
    }

    /**
     * Esta função é responsável por resolver a operação matemática solicitada
     *
     * @param text Texto de entrada para o robô
     * @return Valor resultante da operação
     */
    private fun getOperationSolution(text: String): Double? {
        val tokens = text.split(' ')
        val key = tokens.firstOrNull { operations.keys.contains(it.lowercase()) }

        val values =
            tokens.filter { it.toDoubleOrNull() != null }.map { it.toDouble() }
        return operations[key?.lowercase()]?.invoke(values)
    }
}
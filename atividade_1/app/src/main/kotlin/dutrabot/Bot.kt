package dutrabot

import dutrabot.helpers.mapQuestion
import dutrabot.helpers.*

open class Bot(private val model: NNModel) {

    /**
     * Hash com respostas do Bot
     */
    val botResponses: Map<Int, String> =
        responses.entries.associate { (key, value) -> value to key }

    /**
     * Função de ativação REctified Linear Unit.
     * https://en.wikipedia.org/wiki/Rectifier_(neural_networks)
     *
     * @param matrix Matriz de resultados
     * @return Matriz resultante
     */
    private fun relu(matrix: Array<DoubleArray>): Array<DoubleArray> =
        Array(matrix.size) { i -> DoubleArray(matrix[0].size) { j -> if (matrix[i][j] > 0) matrix[i][j] else 0.0 } }

    /**
     * Função softmax para mapear resultado em vertor de probabilidades
     * https://en.wikipedia.org/wiki/Softmax_function
     *
     * @param matrix Matriz de resultados
     * @return Matriz/Vetor de probabilidade
     */
    private fun softmax(matrix: Array<DoubleArray>): Array<DoubleArray> {
        val matrixExp = (matrix - matrix.max()).exp()
        return matrixExp / matrixExp.sum()
    }

    /**
     * Pondera valores de entrada (mapeamento de texto) para
     * predição de resposta
     *
     * @param input Vetor de valores para predição
     * @return Indice da classe resultante da predição
     */
    private fun predict(input: DoubleArray): Int {
        // prepara valores de modelo
        val layers = model.coefs zip model.intercepts.map { arrayOf(it) }
        val result = layers.foldIndexed(arrayOf(input)) { idx, lastOut, (weights, bias) ->
            // calcula forward propagation
            val layer = (lastOut * weights) + bias
            // retorna valores após função de ativação (utilizando softmax verificação de probabilidades)
            if (idx == (layers.size - 1)) softmax(layer) else relu(layer)
        }

        // verifica indice com maior probabilidade
        val argmax = result.argMax()
        return model.classes[argmax[0]]
    }

    /**
     * Trata a resposta antes de retorna-la
     *
     * @param answerIdx Indice da resposta
     * @param text Texto enviado pelo utilizador
     * @return Texto de resposta
     */
    open fun answer(answerIdx: Int, text: String): String {
        val idx = if (6 > answerIdx) answerIdx else 6
        return botResponses[idx]!!
    }

    /**
     * Função responsável por responder a um texto de entrada
     *
     * @param text Texto de entrada
     * @return Resposto do Bot
     */
    fun reply(text: String): String {
        // analisa texto de entrada
        val mapped = mapQuestion(text).map { if (it) 1.0 else (0.0) }.toDoubleArray()

        // varifica resposta
        var answerIdx = predict(mapped)

        // responde
        return answer(answerIdx, text)
    }
}

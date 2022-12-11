package dutrabot

/**
 * Essa classe é utilizada para ler o arquivo JSON do modelo
 * do algorítimo de aprendizagem
 */
data class NNModel(
    val coefs: Array<Array<DoubleArray>>,
    val intercepts: Array<DoubleArray>,
    val classes: IntArray,
    val params: Map<String, Any>
)
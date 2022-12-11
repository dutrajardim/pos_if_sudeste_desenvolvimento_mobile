package dutrabot.helpers


/**
 * Verifica se uma palavra passada como argumento esta com tosas as letras em caixa alta
 *
 * @param word Palavra a ser verificada.
 * @return Verdadeiro/Falso (Boolean).
 */
fun isAllUpperCase(word: String): Boolean {
    val wordClean = word.replace("[^A-zÀ-ú0-9]".toRegex(), "")
    return wordClean.isNotEmpty() && wordClean.all { it.isUpperCase() }
}


/**
 * Verifica se um texto passado como argumento é uma pergunta. (terminado com interrogação)
 *
 * @param text Texto a ser verificado.
 * @return Verdadeiro/Falso (Boolean).
 */
fun isQuestion(text: String): Boolean =
    text.trim().isNotEmpty() && text.trim().last() == '?'

/**
 * Verifica se um texto passado como argumento possui um grito. (palavra em caixa alta)
 *
 * @param text Texto a ser verificado.
 * @return Verdadeiro/Falso (Boolean).
 */
fun isScreaming(text: String): Boolean =
    text.replace("^[A-Z] ".toRegex(), "").split(' ').any { isAllUpperCase(it) }

/**
 * Verifica se um texto passado como argumento é vazio.
 *
 * @param text Texto a ser verificado.
 * @return Verdadeiro/Falso (Boolean).
 */
fun isSayingNothing(text: String): Boolean = text.trim().isEmpty()

/**
 * Verifica se um texto passado como argumento possui a palevra eu.
 *
 * @param text Texto a ser verificado.
 * @return Verdadeiro/Falso (Boolean).
 */
fun hasI(text: String): Boolean =
    text.split(' ')
        .any { it.replace("[^A-Za-z]".toRegex(), "").lowercase() == "eu" }

/**
 * Verifica se um texto passado como argumento possui uma das operações conhecidas. As operações conhecidas estão
 * salvas numa variável.
 *
 * @param text Texto a ser verificado.
 * @return Verdadeiro/Falso (Boolean).
 */
fun hasKnownOperation(text: String): Boolean =
    text.split(' ')
        .firstOrNull { operations.keys.contains(it.lowercase()) } != null

/**
 * Verifica se um texto passado como argumento possui a palavra 'agir'.
 *
 * @param text Texto a ser verificado.
 * @return Verdadeiro/Falso (Boolean).
 */
fun hasCallToAction(text: String): Boolean =
    text.split(' ')
        .any { it.replace("[^A-Za-z]".toRegex(), "").lowercase() == "agir" }


/**
 * Mapeia texto para vetor com resultado de verificações (se é pergunta, se contém grito, ...)
 *
 * @param text Texto a ser mapeado
 * @return Vetor de valores double indicando resultado de verificações
 */
fun mapQuestion(text: String): BooleanArray =
    booleanArrayOf(
        isQuestion(text),
        isScreaming(text),
        isSayingNothing(text),
        hasI(text),
        hasKnownOperation(text),
        hasCallToAction(text)
    )


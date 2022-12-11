package dutrabot.helpers

import kotlin.math.exp as kExp

/**
 * Esta função é responsavel pela operação de multiplicação
 * entre duas matrizes
 *
 * @param other Matriz utilizado como segundo fator.
 * @return Matriz produto
 */
operator fun Array<DoubleArray>.times(other: Array<DoubleArray>): Array<DoubleArray> =
    Array(this.size) { i ->
        DoubleArray(other[0].size) { j ->
            // somatório da multiplicação linha k por coluna k
            DoubleArray(this[0].size) { k -> this[i][k] * other[k][j] }.reduce { acc, cur -> acc + cur }
        }
    }

/**
 * Esta função é responsável pela operação de divisão entre
 * uma matriz e um valor escalar
 *
 * @param scalar Valor a ser utilizado como divisor
 * @return Matriz quociente
 */
operator fun Array<DoubleArray>.div(scalar: Double): Array<DoubleArray> =
    Array(this.size) { i -> DoubleArray(this[0].size) { j -> this[i][j] / scalar } }

/**
 * Esta função é responsavel pela operação de adição
 * entre duas matrizes
 *
 * @param other Matriz utilizado como segundo fator.
 * @return Matriz soma
 */
operator fun Array<DoubleArray>.plus(other: Array<DoubleArray>): Array<DoubleArray> =
    Array(this.size) { i -> DoubleArray(this[0].size) { j -> this[i][j] + other[i][j] } }

/**
 * Esta função é responsável pela subitração entre
 * dusa matrizes
 *
 * @param scalar Valor a ser utilizado como subtraendo
 * @return Matriz diferença
 */
operator fun Array<DoubleArray>.minus(scalar: Double): Array<DoubleArray> =
    Array(this.size) { i -> DoubleArray(this[0].size) { j -> this[i][j] - scalar } }

/**
 * Esta função é responsavel por retornar o index do maior
 * valor para cada linha
 *
 * @return Vetor de indeces
 */
fun Array<DoubleArray>.argMax(): IntArray =
    this.map { it.foldIndexed(0) { idx, lastMax, cur -> if (cur > it[lastMax]) idx else lastMax } }.toIntArray()

/**
 * Função responsável por retornar o maior valor
 * de uma matriz
 *
 * @return Maior valor
 */
fun Array<DoubleArray>.max(): Double =
    DoubleArray(this.size) { this[it].maxOrNull()!! }.maxOrNull()!!

/**
 * Função responsável por calcular o exponencial de cada elemento
 * da matriz
 *
 * @return Matriz resultante
 */
fun Array<DoubleArray>.exp(): Array<DoubleArray> =
    Array(this.size) { i -> DoubleArray(this[0].size) { j -> kExp(this[i][j]) } }

/**
 * Função responsável por calcular o somatório de todos os valores
 * de uma matriz
 *
 * @return Somatório
 */
fun Array<DoubleArray>.sum(): Double =
    Array(this.size) { this[it].sum() }.sum()
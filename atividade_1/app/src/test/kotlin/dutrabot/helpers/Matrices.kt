/*
 * Conjunto de testes utilizados para verificar o retorno das funções auxiliares para matriz
 */
package dutrabot.helpers

import kotlin.properties.Delegates
import kotlin.test.*
import kotlin.math.exp as kExp

class MatricesTest {

    // matriz 2 x 3 (A)
    // 1.0  1.0  1.0
    // 1.0  1.0  1.0
    private val mA = Array(2) { DoubleArray(3) { 1.0 } }

    // matriz 3 x 2 (B)
    // 1.0  1.0
    // 1.0  1.0
    // 1.0  1.0
    private val mB = Array(3) { DoubleArray(2) { 1.0 } }

    @Test
    fun testTimes() {

        // A . B = C (matriz 2 x 2)
        // 3.0  3.0
        // 3.0  3.0
        val mC = Array(2) { DoubleArray(2) { 3.0 } }

        // C . C = D (matriz 2 x 2)
        // 18.0  18.0
        // 18.0  18.0
        val mD = Array(2) { DoubleArray(2) { 18.0 } }

        var expected by Delegates.notNull<Boolean>()

        expected = mC.contentDeepEquals(mA * mB)
        assertTrue(expected, "test A . B = C should result true")

        expected = mD.contentDeepEquals(mC * mC)
        assertTrue(expected, "test C . C = D should result true")
    }

    @Test
    fun testDiv() {
        val mC = Array(2) { DoubleArray(3) { 4.0 } }
        val mD = Array(2) { DoubleArray(3) { 4.0 / 2 } }

        val expected = mD.contentDeepEquals(mC / 2.0)
        assertTrue(expected, "text C / 2 = D should result true")
    }

    @Test
    fun testPlus() {
        // A + A = C (matriz 2 x 3)
        // 2.0  2.0  2.0
        // 2.0  2.0  2.0
        val mC = Array(2) { DoubleArray(3) { 2.0 } }

        // B + B = D (matriz 3 x 2)
        // 2.0  2.0
        // 2.0  2.0
        // 2.0  2.0
        val mD = Array(3) { DoubleArray(2) { 2.0 } }

        var expected by Delegates.notNull<Boolean>()

        expected = mC.contentDeepEquals(mA + mA)
        assertTrue(expected, "test A + A = C should result true")

        expected = mD.contentDeepEquals(mB + mB)
        assertTrue(expected, "test B + B = D should result true")
    }

    @Test
    fun testArgMax() {
        // matriz 2 x 3 (X)
        val mX = arrayOf(
            doubleArrayOf(2.4, 3.1, 1.0), // maior é o segundo (indice 1)
            doubleArrayOf(.2, 3.5, 7.3) // maior é o terceiro (indice 2)
        )

        // vector 2 (Y)
        val vY = intArrayOf(1, 2)

        assertContentEquals(vY, mX.argMax())
    }

    @Test
    fun testMax() {
        // matriz 2 x 3 (X)
        val mX = arrayOf(
            doubleArrayOf(2.4, 3.1, 1.0),
            doubleArrayOf(.2, 3.5, 7.3)
        ) // max is 7.3

        assertEquals(7.3, mX.max())
    }

    @Test
    fun testExp() {
        // matriz 2 x 2 (X)
        val mX = arrayOf(
            doubleArrayOf(1.0, 2.0),
            doubleArrayOf(3.0, 4.0)
        )

        val mY = arrayOf(
            doubleArrayOf(kExp(1.0), kExp(2.0)),
            doubleArrayOf(kExp(3.0), kExp(4.0))
        )

        val expected = mY.contentDeepEquals(mX.exp())
        assertTrue(expected, "test exp(X) = Y should result true")
    }

    @Test
    fun testSum() {
        // matriz 2 x 3 (X)
        val mX = arrayOf(
            doubleArrayOf(2.4, 3.1, 1.0),
            doubleArrayOf(.2, 3.5, 7.3)
        ) // max is 7.3

        val expected = 2.4 + 3.1 + 1.0 + .2 + 3.5 + 7.3

        assertEquals(expected, mX.sum())
    }
}
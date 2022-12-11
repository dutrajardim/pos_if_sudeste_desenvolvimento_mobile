/*
 * Conjunto de testes utilizados para verificar o retorno das funções auxiliares.
 */
package dutrabot.helpers

import kotlin.properties.Delegates
import kotlin.test.*

class HelpersTest {

    @Test
    fun testIsAllUpperCase() {
        assertTrue(isAllUpperCase("UPPERCASE"), "should return true to 'UPPERCASE'")
        assertTrue(isAllUpperCase("UPPERCASE!"), "should return true to 'UPPERCASE!")
        assertFalse(isAllUpperCase("1234"), "should return false to '1234'")
        assertFalse(isAllUpperCase("SnakeCase"), "should return false to 'SnakeCase'")
        assertFalse(isAllUpperCase(""), "should return false to empty string")
        assertFalse(isAllUpperCase(" "), "should return false to ' '")
    }

    @Test
    fun testIsQuestion() {
        assertTrue(isQuestion("What?"), "should return true to 'What?'")
        assertTrue(isQuestion("?"), "should return true to '?'")
        assertTrue(isQuestion("Você gosta?"), "should return true to 'Você gosta?'")
        assertFalse(isQuestion("Tem? Não sei!"), "should return false to 'Tem? Não sei!'")
        assertFalse(isQuestion("1234"), "should return false to '1234'")
        assertFalse(isQuestion(""), "should return false to empty string")
        assertFalse(isQuestion(" "), "should return false to ' '")
    }

    @Test
    fun testIsScreaming() {
        assertTrue(isScreaming("O que OCORREU?"), "should return true to 'O que OCORREU?'")
        assertTrue(isScreaming("CORRA!"), "should return true to 'OCORRA!'")
        assertTrue(isScreaming("OS que ocorreram?"), "should return true to 'OS que ocorreram?'")
        assertFalse(isScreaming("O que ocorreu?"), "should return false to 'O que ocorreu?'")
        assertFalse(isScreaming("1234"), "should return false to '1234'")
        assertFalse(isScreaming(""), "should return false to empty string")
        assertFalse(isScreaming(" "), "should return false to ' '")
    }

    @Test
    fun testIsSayingNothing() {
        assertTrue(isSayingNothing(""), "should return true to empty string")
        assertTrue(isSayingNothing(" "), "should return true to ' '")
        assertFalse(isSayingNothing("1"), "should return false to '1'")
        assertFalse(isSayingNothing("Oi!"), "should return false to 'Oi!'")
    }

    @Test
    fun testHasI() {
        assertTrue(hasI("Eu sei o que preciso."), "should return true to 'Eu sei o que preciso.'")
        assertTrue(hasI("EU"), "should return true to 'EU'")
        assertTrue(hasI("O que eu sei?"), "should return true to 'O que eu sei?'")
        assertFalse(hasI("O que ocorreu?"), "should return false to 'O que ocorreu?'")
        assertFalse(hasI("1234"), "should return false to '1234'")
        assertFalse(hasI(""), "should return false to empty string")
        assertFalse(hasI(" "), "should return false to ' '")
    }

    @Test
    fun testHasKnownOperation() {
        val lowerOperation = operations.keys.first().lowercase()
        assertTrue(
            hasKnownOperation("Quero que $lowerOperation 4 e 3."),
            "should return true to 'Quero que $lowerOperation 4 e 3.'"
        )
        val upperOperation = operations.keys.last().uppercase()
        assertTrue(
            hasKnownOperation("Quero que $upperOperation 4 e 3."),
            "should return true to 'Quero que $upperOperation 4 e 3.'"
        )
        val operation = operations.keys.first()
        assertTrue(hasKnownOperation(operation), "should return true to '$operation'")
        assertFalse(hasKnownOperation("O que ocorreu?"), "should return false to 'O que ocorreu?'")
        assertFalse(hasKnownOperation("1234"), "should return false to '1234'")
        assertFalse(hasKnownOperation(""), "should return false to empty string")
        assertFalse(hasKnownOperation(" "), "should return false to ' '")
    }

    @Test
    fun testHasCallToAction() {
        assertTrue(hasCallToAction("Agir."), "shoul return true to 'Agir.'")
        assertTrue(hasCallToAction("Faça bolo, agir."), "shoul return true to 'Faça bolo, agir.'")
        assertFalse(hasCallToAction("O que ocorreu?"), "should return false to 'O que ocorreu?'")
        assertFalse(hasCallToAction("1234"), "should return false to '1234'")
        assertFalse(hasCallToAction(""), "should return false to empty string")
        assertFalse(hasCallToAction(" "), "should return false to ' '")
    }

    @Test
    fun testMapQuestion() {
        var expected by Delegates.notNull<BooleanArray>()

        expected = booleanArrayOf(true, false, false, false, false, false)
        assertContentEquals(expected, mapQuestion("Você já comeu ervilha?"))

        expected = booleanArrayOf(false, true, false, false, false, false)
        assertContentEquals(expected, mapQuestion("Fale CATAGUASES!"))

        expected = booleanArrayOf(false, false, true, false, false, false)
        assertContentEquals(expected, mapQuestion(""))

        expected = booleanArrayOf(true, true, false, false, false, false)
        assertContentEquals(expected, mapQuestion("Esta afim de ARRUINAR tudo?"))

        expected = booleanArrayOf(false, false, false, true, false, false)
        assertContentEquals(expected, mapQuestion("Eu quero comer um biscoito."))

        expected = booleanArrayOf(false, false, false, true, false, false)
        assertContentEquals(expected, mapQuestion("Eu quero comer um biscoito."))

        expected = booleanArrayOf(false, false, false, false, true, false)
        assertContentEquals(expected, mapQuestion("Divida 4 por 3."))

        expected = booleanArrayOf(false, false, false, false, false, true)
        assertContentEquals(expected, mapQuestion("Dançar valsa. Agir."))
    }
}

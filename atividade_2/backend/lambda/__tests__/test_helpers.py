import unittest
import sys
from random import choice

sys.path.append("..")
from helpers import (
    is_screaming,
    is_questioning,
    is_saying_nothing,
    has_I,
    has_known_operation,
    known_operations,
    has_call_to_action,
    map_question,
)


class TestHelpers(unittest.TestCase):
    def test_is_screaming(self):
        self.assertTrue(is_screaming("O que OCORREU?"))
        self.assertTrue(is_screaming("CORRA!"))
        self.assertTrue(is_screaming("OS que ocorreram?"))
        self.assertFalse(is_screaming("O que ocorreu?"))
        self.assertFalse(is_screaming("1234"))
        self.assertFalse(is_screaming(""))
        self.assertFalse(is_screaming(" "))

    def test_is_questioning(self):
        self.assertTrue(is_questioning("What?"))
        self.assertTrue(is_questioning("?"))
        self.assertTrue(is_questioning("Você gosta?"))
        self.assertFalse(is_questioning("Tem? Não sei!"))
        self.assertFalse(is_questioning("1234"))
        self.assertFalse(is_questioning(""))
        self.assertFalse(is_questioning(" "))

    def test_is_saying_nothing(self):
        self.assertTrue(is_saying_nothing(""), "should return true to empty string")
        self.assertTrue(is_saying_nothing(" "), "should return true to ' '")
        self.assertFalse(is_saying_nothing("1"), "should return false to '1'")
        self.assertFalse(is_saying_nothing("Oi!"), "should return false to 'Oi!'")

    def test_has_I(self):
        self.assertTrue(has_I("Eu sei o que preciso."))
        self.assertTrue(has_I("EU"))
        self.assertTrue(has_I("O que eu sei?"))
        self.assertFalse(has_I("O que ocorreu?"))
        self.assertFalse(has_I("1234"))
        self.assertFalse(has_I(""))
        self.assertFalse(has_I(" "))

    def test_has_known_operation(self):
        known_operation = choice(known_operations).lower()
        self.assertTrue(has_known_operation("Quero que %s 4 e 3." % known_operation))
        known_operation = choice(known_operations).upper()
        self.assertTrue(has_known_operation("Quero que %s 4 e 3." % known_operation))
        nown_operation = choice(known_operations)
        self.assertTrue(has_known_operation(nown_operation))
        self.assertFalse(has_known_operation("O que ocorreu?"))
        self.assertFalse(has_known_operation("1234"))
        self.assertFalse(has_known_operation(""))
        self.assertFalse(has_known_operation(" "))

    def test_has_call_to_action(self):
        self.assertTrue(has_call_to_action("Agir."))
        self.assertTrue(has_call_to_action("Faça bolo, agir."))
        self.assertFalse(has_call_to_action("O que ocorreu?"))
        self.assertFalse(has_call_to_action("1234"))
        self.assertFalse(has_call_to_action(""))
        self.assertFalse(has_call_to_action(" "))

    def test_map_question(self):
        expected = [True, False, False, False, False, False]
        self.assertListEqual(expected, map_question("Você já comeu ervilha?"))

        expected = [False, True, False, False, False, False]
        self.assertListEqual(expected, map_question("Fale CATAGUASES!"))

        expected = [False, False, True, False, False, False]
        self.assertListEqual(expected, map_question(""))

        expected = [True, True, False, False, False, False]
        self.assertListEqual(expected, map_question("Esta afim de ARRUINAR tudo?"))

        expected = [False, False, False, True, False, False]
        self.assertListEqual(expected, map_question("Eu quero comer um biscoito."))

        expected = [False, False, False, True, False, False]
        self.assertListEqual(expected, map_question("Eu quero comer um biscoito."))

        expected = [False, False, False, False, True, False]
        self.assertListEqual(expected, map_question("Divida 4 por 3."))

        expected = [False, False, False, False, False, True]
        self.assertListEqual(expected, map_question("Dançar valsa. Agir."))


if __name__ == "__main__":
    unittest.main()

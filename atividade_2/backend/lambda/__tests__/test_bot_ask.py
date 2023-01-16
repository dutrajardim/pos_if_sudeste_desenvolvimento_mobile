import unittest
import sys

sys.path.append("..")
from bot_ask import get_answer


class TestAskSimpleBot(unittest.TestCase):
    def test_get_answer(self):

        # testando: ele deverá responder "Certamente" se alguém fizer uma pergunta a ele.
        self.assertEqual(get_answer("Gostaria de sair?"), "Certamente.")
        self.assertEqual(get_answer("Você é de Marte?"), "Certamente.")
        self.assertEqual(get_answer("Você é um Extra Terrestre?"), "Certamente.")
        self.assertEqual(get_answer("Sabe que horas são?"), "Certamente.")
        self.assertEqual(get_answer("Dúvidas?"), "Certamente.")

        #  testando: se alguém gritar com ele (usando alguma palavra com todas as suas letras em maiúsculas), ele deverá responder "Opa! Calma aí!".
        self.assertEqual(get_answer("CORRA!"), "Opa! Calma aí!")
        self.assertEqual(get_answer("Você é um ET."), "Opa! Calma aí!")
        self.assertEqual(get_answer("PULA o muro."), "Opa! Calma aí!")
        self.assertEqual(get_answer("FAÇA ISSO DIREITO!"), "Opa! Calma aí!")

        #  testando: ele responde "Relaxa, eu sei o que faço!" se alguém gritar com ele numa pergunta.
        self.assertEqual(get_answer("CORRA?"), "Relaxa, eu sei o que faço!")
        self.assertEqual(
            get_answer("Quer comprar um CARRO?"), "Relaxa, eu sei o que faço!"
        )
        self.assertEqual(get_answer("Sabe ESCREVER?"), "Relaxa, eu sei o que faço!")
        self.assertEqual(
            get_answer("Tem DINHEIRO para emprestar?"), "Relaxa, eu sei o que faço!"
        )

        #  testando: caso alguém fale algo que use a palavra "eu" (com ou sem maiúsculas), ele responde "A responsabilidade é sua".
        self.assertEqual(get_answer("Eu sei escrever."), "A responsabilidade é sua.")
        self.assertEqual(get_answer("Eu quero chocolate."), "A responsabilidade é sua.")
        self.assertEqual(
            get_answer("Eu tenho que correr!"), "A responsabilidade é sua."
        )

        #  testando: se alguém se dirigir a ele sem dizer nenhuma palavra ele deverá responder "Não me incomode".
        self.assertEqual(get_answer(""), "Não me incomode.")
        self.assertEqual(get_answer("  "), "Não me incomode.")

        #  testando: ele diz "Tudo bem, como quiser" caso alguém fale qualquer outra coisa com ele.
        self.assertEqual(get_answer("Quero pular corda."), "Tudo bem, como quiser.")
        self.assertEqual(get_answer("Você deve ir a lua."), "Tudo bem, como quiser.")
        self.assertEqual(get_answer("Quero ir a lua também."), "Tudo bem, como quiser.")

        self.assertEqual(get_answer("Some 3 e 4."), "Tudo bem, como quiser.")
        self.assertEqual(
            get_answer("Tudo bem, subtraia 5 e 7."), "Tudo bem, como quiser."
        )
        self.assertEqual(get_answer("Fazer café. Agir"), "Tudo bem, como quiser.")
        self.assertEqual(get_answer("Subir escadas. Agir"), "Tudo bem, como quiser.")


if __name__ == "__main__":
    unittest.main()

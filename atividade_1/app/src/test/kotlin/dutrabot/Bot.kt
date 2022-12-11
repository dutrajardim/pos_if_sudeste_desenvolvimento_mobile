package dutrabot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import kotlin.test.*

class BotTest {

    @Test
    fun testBot() {

        // lendo arquivo de modelo de predição
        val jsonMapper = jacksonObjectMapper()
        val model = jsonMapper.readValue<NNModel>(File("data/model.json"))

        // inicializando bot
        val marciano = Bot(model)

        // preparando funções auxiliares para testes
        val message = { text: String, exp: String -> "should return '$exp' to '$text'" }
        val check = { text: String, exp: String ->
            assertEquals(exp, marciano.reply(text), message(text, exp))
        }

        // testando: ele deverá responder "Certamente" se alguém fizer uma pergunta a ele.
        check("Gostaria de sair?", "Certamente.")
        check("Você é de Marte?", "Certamente.")
        check("Você é um Extra Terrestre?", "Certamente.")
        check("Sabe que horas são?", "Certamente.")
        check("Dúvidas?", "Certamente.")

        // testando: se alguém gritar com ele (usando alguma palavra com todas as suas letras em maiúsculas),
        // ele deverá responder "Opa! Calma aí!".
        check("CORRA!", "Opa! Calma aí!")
        check("Você é um ET.", "Opa! Calma aí!")
        check("PULA o muro.", "Opa! Calma aí!")
        check("FAÇA ISSO DIREITO!", "Opa! Calma aí!")

        // testando: ele responde "Relaxa, eu sei o que faço!" se alguém gritar
        // com ele numa pergunta.
        check("CORRA?", "Relaxa, eu sei o que faço!")
        check("Quer comprar um CARRO?", "Relaxa, eu sei o que faço!")
        check("Sabe ESCREVER?", "Relaxa, eu sei o que faço!")
        check("Tem DINHEIRO para emprestar?", "Relaxa, eu sei o que faço!")

        // testando: caso alguém fale algo que use a palavra "eu" (com ou sem maiúsculas), ele
        // responde "A responsabilidade é sua".
        check("Eu sei escrever.", "A responsabilidade é sua.")
        check("Eu quero chocolate.", "A responsabilidade é sua.")
        check("Eu tenho que correr!", "A responsabilidade é sua.")

        // testando: se alguém se dirigir a ele sem dizer nenhuma palavra ele deverá responder "Não me
        // incomode".
        check("", "Não me incomode.")
        check("  ", "Não me incomode.")

        // testando: ele diz "Tudo bem, como quiser" caso alguém fale qualquer outra coisa com ele.
        check("Quero pular corda.", "Tudo bem, como quiser.")
        check("Você deve ir a lua.", "Tudo bem, como quiser.")
        check("Quero ir a lua também.", "Tudo bem, como quiser.")

        check("Some 3 e 4.", "Tudo bem, como quiser.")
        check("Tudo bem, subtraia 5 e 7.", "Tudo bem, como quiser.")
        check("Fazer café. Agir", "Tudo bem, como quiser.")
        check("Subir escadas. Agir", "Tudo bem, como quiser.")
    }
}
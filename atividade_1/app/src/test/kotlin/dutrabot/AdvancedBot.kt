package dutrabot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import kotlin.test.*

class AdvancedBotTest {

    @Test
    fun testAdvancedBot() {
        // lendo arquivo de modelo de predição
        val jsonMapper = jacksonObjectMapper()
        val model = jsonMapper.readValue<NNModel>(File("data/model.json"))

        // inicializando bot
        val marciano = AdvancedBot(model)

        // preparando funções auxiliares para testes
        val message = { text: String, exp: String -> "should return '$exp' to '$text'" }
        val check = { text: String, exp: String ->
            assertEquals(exp, marciano.reply(text), message(text, exp))
        }

        // testando: ele deverá responder "Certamente" se alguém fizer uma pergunta a ele.
        check("Gostaria de sair?", "Certamente.")

        // testando: se alguém gritar com ele (usando alguma palavra com todas as suas letras em maiúsculas),
        // ele deverá responder "Opa! Calma aí!".
        check("CORRA!", "Opa! Calma aí!")

        // testando: ele responde "Relaxa, eu sei o que faço!" se alguém gritar
        // com ele numa pergunta.
        check("CORRA?", "Relaxa, eu sei o que faço!")

        // testando: caso alguém fale algo que use a palavra "eu" (com ou sem maiúsculas), ele
        // responde "A responsabilidade é sua".
        check("Eu sei escrever.", "A responsabilidade é sua.")

        // testando: se alguém se dirigir a ele sem dizer nenhuma palavra ele deverá responder "Não me
        // incomode".
        check("", "Não me incomode.")

        // testando: ele diz "Tudo bem, como quiser" caso alguém fale qualquer outra coisa com ele.
        check("Quero pular corda.", "Tudo bem, como quiser.")

        // O robô poderá receber as seguintes operações como parâmetro de responda: "some",
        // "subtraia", "multiplique", "divida"
        // Os operandos das operações deverão ser passados como parâmetros extras da função responda()
        // O robô deverá responder: "Essa eu sei", seguida do resultado
        check("Some 3 e 4.", "Essa eu sei. A resposta é '7.0'.")
        check("Tudo bem, subtraia 5 e 7.", "Essa eu sei. A resposta é '-2.0'.")

        check("Fazer café. Agir", "Tudo bem, como quiser.")
        check("Subir escadas. Agir", "Tudo bem, como quiser.")
    }
}
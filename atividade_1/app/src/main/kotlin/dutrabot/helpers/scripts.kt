package dutrabot.helpers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dutrabot.Bot
import dutrabot.NNModel
import dutrabot.PremiumBot
import java.io.File

/**
 * Função responsável por ler o arquivo de perguntas
 * e gerar um arquivo de vetores de análises
 * realizadas nas perguntas. Este documento gerado
 * é utilizado para treino do robô.
 */
fun mapQuestions() {
    val writer = File("data/mapped.csv").bufferedWriter()

    File("data/perguntas.csv").bufferedReader().lineSequence().filter { it.isNotBlank() }.forEach { row ->

        // para cada linha do arquivo é esperado duas colunas de texto separadas por ponto e vírgula
        var (question, answer) = row.split(';', ignoreCase = false, limit = 2)
        // removendo aspas duplas
        question = question.removeSurrounding("\"")
        answer = answer.removeSurrounding("\"")

        // mapeando o texto para vetor
        val mappedQuestion = mapQuestion(question).map { if (it) 1 else 0 }
        // mapeando resposta
        val response = responses[answer]

        // salvando dados mapeados
        val line = mappedQuestion.joinToString(separator = ";")
        writer.write("${line};${response}")

        writer.newLine()
    }

    writer.flush()
}

/**
 * Função responsável por inicializar o robô e controlar
 * a interação com o utilizador da aplicação
 */
fun startBot() {

    // lendo modelo de aprendizagem
    val jsonMapper = jacksonObjectMapper()
    val model = jsonMapper.readValue<NNModel>(File("data/model.json"))
    // iniciando o robô de luxo
    val marciano = PremiumBot(model)

    // mantém a interação até ser digitado a palavra FIM
    while (true) {
        println("Faça uma pergunta: (FIM para terminar)")

        // lendo a entrada do utilizador
        val question = readlnOrNull() ?: ""
        println("Pergunta: $question")
        if (question == "FIM") break

        // retornando a resposta do robô
        val response = marciano.reply(question)
        println("Resposta: $response")
    }
}

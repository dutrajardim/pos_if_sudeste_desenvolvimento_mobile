package dutrabot.helpers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dutrabot.Bot
import dutrabot.NNModel
import dutrabot.PremiumBot
import java.io.File


fun mapQuestions() {
    val writer = File("data/mapped.csv").bufferedWriter()

    File("data/perguntas.csv").bufferedReader().lineSequence().filter { it.isNotBlank() }.forEach { row ->
        var (question, answer) = row.split(';', ignoreCase = false, limit = 2)
        question = question.removeSurrounding("\"")
        answer = answer.removeSurrounding("\"")

        val mappedQuestion = mapQuestion(question).map { if (it) 1 else 0 }
        val response = responses[answer]

        val line = mappedQuestion.joinToString(separator = ";")
        writer.write("${line};${response}")

        writer.newLine()
    }

    writer.flush()
}

fun startBot() {
    val jsonMapper = jacksonObjectMapper()
    val model = jsonMapper.readValue<NNModel>(File("data/model.json"))
    val marciano = PremiumBot(model)

    while (true) {
        println("Faça uma pergunta: (FIM para terminar)")
        val question = readlnOrNull() ?: ""
        println("Pergunta: $question")
        if (question == "FIM") break

        val response = marciano.reply(question)
        println("Resposta: $response")
    }
}

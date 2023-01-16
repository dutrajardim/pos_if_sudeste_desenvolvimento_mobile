import {
    checkMethod,
    scanTable,
    errorHandler
} from './helpers.mjs'

export async function handler(event) {
    return await errorHandler(async () => {

        // verificando se a requisição é válida
        checkMethod("GET", event.httpMethod)

        // recuperando dados na base de dados
        const trainingTableName = process.env.TRAINING_TABLE_NAME
        const data = await scanTable({ TableName: trainingTableName })

        // formatando e retornando resposta
        return {
            statusCode: 200,
            body: JSON.stringify({ data: data })
        }
    })
}
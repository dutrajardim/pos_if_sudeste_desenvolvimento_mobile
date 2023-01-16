import {
    checkRequiredParams,
    getUserBotChatName,
    errorHandler,
    checkMethod,
    putItem,
} from './helpers.mjs'

export async function handler(event) {
    return await errorHandler(async () => {

        // verificando se a requisição é válida
        checkMethod("POST", event.httpMethod)
        const inputParams = JSON.parse(event.body)
        checkRequiredParams(["question"], inputParams)
        checkRequiredParams(["tag"], event.pathParameters)

        // recuperando dados na requisição
        const { question } = inputParams
        const { tag } = event.pathParameters
        const { "cognito:username": username } = event.requestContext.authorizer.claims

        // modelando dados
        const data = getMessageData({ username, tag, question })

        // persistindo os dados
        const messagesTableName = process.env.MESSAGES_TABLE_NAME
        await putItem({ TableName: messagesTableName, Item: data })

        // formatando e retornando resposta
        return {
            "statusCode": 200,
            "body": JSON.stringify({ data })
        }
    })
}



function getMessageData({ username, tag, question }) {
    return {
        CreatedAt: Date.now(),
        Chat: getUserBotChatName({ username, tag }),
        Type: "USER_TO_BOT_MESSAGE",
        From: username,
        To: tag,
        Text: question
    }
}
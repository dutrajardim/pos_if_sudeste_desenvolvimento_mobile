import {
    checkRequiredParams,
    getUserBotChatName,
    errorHandler,
    checkMethod,
    queryItems
} from "./helpers.mjs";

export async function handler(event) {
    return await errorHandler(async () => {

        // verificando se a requisição é válida
        checkMethod("GET", event.httpMethod)
        checkRequiredParams(["tag"], event.pathParameters)

        // recuperando dados na requisição
        const { tag } = event.pathParameters
        const { "cognito:username": username } = event.requestContext.authorizer.claims

        // recuperando dados na base de dados
        const chat = getUserBotChatName({ username, tag })
        const messagesTableName = process.env.MESSAGES_TABLE_NAME
        const data = await queryItems({
            TableName: messagesTableName,
            ExpressionAttributeValues: { ":chat": chat },
            KeyConditionExpression: "Chat = :chat"
        })

        // formatando e retornando resposta
        return {
            "statusCode": 200,
            "body": JSON.stringify({ data })
        }
    })
}
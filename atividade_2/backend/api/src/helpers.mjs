import { DynamoDBClient } from '@aws-sdk/client-dynamodb';
import { DynamoDBDocumentClient, PutCommand, ScanCommand, QueryCommand } from '@aws-sdk/lib-dynamodb';

export function getDynamoDBDocumentClient() {
    const dynamoConfig = {}
    const endpoint = process.env.DYNAMOBDB_ENDPOINT

    if (endpoint && endpoint.startsWith('http'))
        dynamoConfig["endpoint"] = process.env.DYNAMOBDB_ENDPOINT

    const client = new DynamoDBClient(dynamoConfig)
    const ddbDocClient = DynamoDBDocumentClient.from(client)

    return ddbDocClient
}

export class RestApiException extends Error {
    constructor(message, code, errors) {
        super(message)
        this.code = code
        this.errors = errors ?? [message]
    }
}

export function checkRequiredParams(requiredParams, inputParams = {}) {
    const messageMapper = (param) => `The ${param} paramter is required!`
    const paramFilter = param => !(param in inputParams)
    const errors = requiredParams.filter(paramFilter).map(messageMapper)
    if (errors.length)
        throw new RestApiException("Invalid request!", 400, errors)
}

export function checkMethod(requiredMethod, inputMethod) {
    if (requiredMethod !== inputMethod) {
        const message = `This path only accept ${requiredMethod}, you tried: ${inputMethod}`
        throw new RestApiException(message, 400, [message])
    }
}

export async function errorHandler(callback) {
    try {
        return await callback()
    } catch (err) {
        if (err instanceof RestApiException)
            return {
                statusCode: err.code,
                body: JSON.stringify({
                    message: err.message,
                    errors: err.errors
                })
            }

        return {
            statusCode: 500,
            body: JSON.stringify({
                message: "Internal Server Error",
                errors: ["Sorry! It's not you."]
            })
        }
    }
}

export async function putItem(data) {
    try {

        const ddbDocClient = getDynamoDBDocumentClient()
        await ddbDocClient.send(new PutCommand(data))

    } catch (err) {
        const message = "Internal Server Error"
        const errors = ["An error occurred when trying to persist data."]
        throw new RestApiException(message, 500, errors)
    }
}

export async function scanTable(data) {
    try {

        const ddbDocClient = getDynamoDBDocumentClient()
        const response = await ddbDocClient.send(new ScanCommand(data))

        return response.Items || []
    } catch (err) {
        const message = "Internal Server Error"
        const errors = ["An error occurred when trying to get data."]
        throw new RestApiException(message, 500, errors)
    }
}

export async function queryItems(data) {
    try {
        const ddbDocClient = getDynamoDBDocumentClient()
        const response = await ddbDocClient.send(new QueryCommand(data))

        return response.Items || []
    } catch (err) {
        const message = "Internal Server Error"
        const errors = ["An error occurred when trying to get data."]
        throw new RestApiException(message, 500, errors)
    }
}

export function getUserBotChatName({ username, tag }) {
    return [`${username}USER`, `${tag}BOT`].sort().join("#")
}
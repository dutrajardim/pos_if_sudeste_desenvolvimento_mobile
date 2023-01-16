import json
import boto3
import os
import joblib
import helpers
import re

from io import BytesIO
from helpers import RestApiException
from datetime import datetime


def get_model_data(tag: str) -> dict:

    dyn_resource = helpers.get_dynamodb_resource()

    training_table_name = os.getenv("TRAINING_TABLE_NAME")
    training_table = dyn_resource.Table(training_table_name)

    response = training_table.get_item(Key={"Tag": tag})
    return response["Item"]


def get_answer(question, model_key):
    """Get answer"""

    s3 = boto3.client("s3")
    bucket_name = os.getenv("BUCKET_NAME")

    with BytesIO() as data:
        s3.download_fileobj(Bucket=bucket_name, Key=model_key, Fileobj=data)
        data.seek(0)
        clf = joblib.load(data)

        answer_idx = clf.predict([helpers.map_question(question)])[0]
        answer = helpers.responses_list[answer_idx]

        if answer == "Essa eu sei.":
            operation = next(
                operation
                for operation in helpers.known_operations
                if operation in question.lower()
            )
            numbers = [int(s) for s in re.findall(r"\b\d+\b", question)]
            result = helpers.known_operations[operation](numbers)
            answer = f"{answer} A resposta é {result}."

        if answer == "É para já!":
            if "café" in question.lower():
                answer = f"{answer} Fazendo café..."
            if "subir" in question.lower():
                answer = f"{answer} Subindo..."

        return answer


def save_message(data: dict) -> None:
    dyn_resource = helpers.get_dynamodb_resource()

    messages_table_name = os.getenv("MESSAGES_TABLE_NAME")
    messages_table = dyn_resource.Table(messages_table_name)

    response = messages_table.put_item(Item=data)


def handler(event: dict, context: dict) -> None:

    for record in event["Records"]:
        if record["eventName"] == "INSERT":
            item = record["dynamodb"]["NewImage"]

            if item["Type"]["S"] == "USER_TO_BOT_MESSAGE":
                model_data = get_model_data(item["To"]["S"])
                answer = get_answer(item["Text"]["S"], model_data["ModelKey"])

                message_from = f"{item['To']['S']}BOT"
                message_to = f"{item['From']['S']}USER"

                save_message(
                    {
                        "CreatedAt": int(datetime.utcnow().timestamp() * 1000),
                        "Chat": "#".join(sorted([message_from, message_to])),
                        "Type": "BOT_TO_USER_MESSAGE",
                        "From": item["To"]["S"],
                        "To": item["From"]["S"],
                        "Text": answer,
                    }
                )

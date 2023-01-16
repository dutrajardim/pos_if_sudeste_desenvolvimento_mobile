import os
import csv
import json
import joblib
import helpers
import tempfile
import boto3
import uuid
from sklearn import tree
from typing import List
from helpers import RestApiException


def get_training_data(s3_key: str):

    try:

        s3 = boto3.client("s3")
        bucket_name = os.getenv("BUCKET_NAME")

        obj = s3.get_object(Bucket=bucket_name, Key=s3_key)
        data = obj["Body"].read().decode("utf-8")
        records = csv.reader(data.splitlines(), delimiter=";")

        X, Y = [], []

        for row in records:
            X.append(helpers.map_question(row[0]))
            Y.append(helpers.responses_dict[row[1]])

        return X, Y

    except Exception as e:
        message = "Internal Server Error"
        raise RestApiException(
            message, 500, ["An error occurred when trying to get traning data."]
        )


def save_model(model, model_s3_key):

    try:

        s3 = boto3.client("s3")
        bucket_name = os.getenv("BUCKET_NAME")

        with tempfile.TemporaryFile() as tf:
            joblib.dump(model, tf)
            tf.seek(0)
            s3.put_object(Body=tf.read(), Bucket=bucket_name, Key=model_s3_key)

    except Exception as e:
        message = "Internal Server Error"
        raise RestApiException(message, 500, ["An error occurred when saving model."])


def save_data(data):

    try:

        dyn_resource = helpers.get_dynamodb_resource()

        training_table_name = os.getenv("TRAINING_TABLE_NAME")
        training_table = dyn_resource.Table(training_table_name)

        response = training_table.put_item(Item=data)

    except Exception as e:
        message = "Internal Server Error"
        raise RestApiException(
            message, 500, ["An error occurred when persisting data."]
        )


def handler(event: dict, context: dict) -> dict:

    try:

        helpers.check_method(event["httpMethod"], "POST")
        input_data = json.loads(event["body"])
        helpers.check_required_params(["tag", "trainingDataKey"], input_data)

        data = {
            "Tag": input_data["tag"],
            "TrainingDataKey": input_data["trainingDataKey"],
            "ModelKey": f"models/{uuid.uuid4()}.jblib",
        }

        X, Y = get_training_data(data["TrainingDataKey"])
        clf = tree.DecisionTreeClassifier()
        clf.fit(X, Y)

        save_model(clf, data["ModelKey"])
        save_data(data)

        return {"statusCode": 200, "body": json.dumps({"data": data})}

    except RestApiException as e:
        return {
            "statusCode": e.code,
            "body": json.dumps({"message": str(e), "errors": e.errors}),
        }

import re
import os
import boto3

from functools import reduce
from typing import List

# BOT HELPERS

known_operations = {
    "some": lambda list: reduce(lambda acc, x: acc + x, list, 0),
    "subtraia": lambda list: reduce(lambda acc, x: acc + x, list, 0),
    "multiplique": lambda list: reduce(lambda acc, x: acc * x, list, 1),
    "divida": lambda list: reduce(lambda acc, x: acc / x, list, 1),
}

responses_list = [
    "Certamente.",
    "Opa! Calma aí!",
    "Relaxa, eu sei o que faço!",
    "A responsabilidade é sua.",
    "Não me incomode.",
    "Tudo bem, como quiser.",
    "Essa eu sei.",
    "É para já!",
]

responses_dict = {v: k for k, v in enumerate(responses_list)}


def _clear(word: str) -> str:
    """
    This function function remove special chars and numbers from a string
    and return the lower case versions of the string
    """
    return re.sub("[^A-Za-z]+", "", word.lower())


def is_screaming(text: str) -> bool:
    """Check if any word in the phrase, given as argument, is in upper case"""
    return any([word.isupper() for word in text.split() if len(word) > 1])


def is_questioning(text: str) -> bool:
    """Check if the phrase, given as argument, is ended with question mark"""
    striped_text = text.strip()
    return len(striped_text) and striped_text[-1] == "?"


def is_saying_nothing(text: str) -> bool:
    """Check if the phrase, given as argument, is empty"""
    return not text.strip()


def has_known_operation(text: str) -> bool:
    """Check if in the phares, given as argument, has a known operation"""
    return any([word.lower() in known_operations for word in text.split()])


def has_I(text: str) -> bool:
    """Check if in the phrase, given as argument, has the word I"""
    return any([_clear(word) == "eu" for word in text.split()])


def has_call_to_action(text: str) -> bool:
    """Check in in the phrase, given as argument, has the word 'agir'"""
    return any([_clear(word) == "agir" for word in text.split()])


def map_question(text: str) -> List[bool]:
    """
    This function is responsible for mapping a phrase to a
    list of boolean values. Each boolean value is related
    to a statement about the phrase as 'is screaming',
    'has the word I'...
    """
    return [
        is_questioning(text),
        is_screaming(text),
        is_saying_nothing(text),
        has_I(text),
        has_known_operation(text),
        has_call_to_action(text),
    ]


# API HELPERS


class RestApiException(Exception):
    def __init__(self, message, code, errors):
        super().__init__(message)
        self.code = code
        self.errors = errors


def check_method(event_method, required_method):
    """check required method"""
    if event_method != required_method:
        message = (
            f"getMethod only accept {required_method} method, you tried: {event_method}"
        )
        raise RestApiException(message, 400, [message])


def check_required_params(required_params, input_params={}):
    """check required params"""
    get_message = lambda param: f"The {param} parameter is required!"
    errors = [
        get_message(param) for param in required_params if param not in input_params
    ]
    if len(errors) > 0:
        message = "Invalid request!"
        raise RestApiException(message, 400, errors)


def get_user_data(requestContext):
    """Get User data"""
    claims = requestContext["authorizer"]["claims"]
    return {
        "email": claims["email"],
        "username": claims["cognito:username"],
        "name": claims["name"],
    }


# AWS resources


def get_dynamodb_resource():
    dynamodb_endpoint = os.getenv("DYNAMOBDB_ENDPOINT")
    config = {}

    if dynamodb_endpoint:
        config["endpoint_url"] = dynamodb_endpoint

    return boto3.resource("dynamodb", **config)

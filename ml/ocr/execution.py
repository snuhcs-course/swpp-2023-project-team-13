import json
import os
import platform
import re
import time
import uuid

import cv2
import numpy as np
import requests
from dotenv import load_dotenv
from matplotlib import pyplot as plt
from PIL import Image, ImageDraw, ImageFont

load_dotenv()

api_url = os.environ.get("api_url")
secret_key = os.environ.get("secret_key")


# path = "assets/receipt.jpeg"


def ocr_receipt(path):
    files = [("file", open(path, "rb"))]

    request_json = {
        "images": [{"format": "jpg", "name": "demo"}],
        "requestId": str(uuid.uuid4()),
        "version": "V2",
        "timestamp": int(round(time.time() * 1000)),
    }

    payload = {"message": json.dumps(request_json).encode("UTF-8")}

    headers = {
        "X-OCR-SECRET": secret_key,
    }

    response = requests.request("POST", api_url, headers=headers, data=payload, files=files)

    response_body = json.loads(response.text)

    images = response_body["images"]
    images_receipt = images[0].get("receipt")

    receipt_title = images_receipt["result"]["storeInfo"]["name"]["text"]
    receipt_address = images_receipt["result"]["storeInfo"]["addresses"][0]["text"]
    receipt_date = images_receipt["result"]["paymentInfo"]["date"]["text"]

    sub_results = images_receipt["result"]["subResults"]
    receipt_menu = []
    for sub_result in sub_results:
        items = sub_result.get("items", [])
        for item in items:
            menu_name = item.get("name", {}).get("text", "")
            receipt_menu.append(menu_name)

    # receipt_price = int(
    #     float(
    #         re.sub(
    #             r"[^\uAC00-\uD7A30-9a-zA-Z\s]",
    #             "",
    #             images_receipt["result"]["totalPrice"]["price"]["text"],
    #         )
    #     )
    # )

    receipt_data = {
        "title": receipt_title,
        "address": receipt_address,
        "date": receipt_date,
        "menu": receipt_menu,
        # "price": receipt_price,
    }

    return receipt_data


# print(ocr_receipt(path))

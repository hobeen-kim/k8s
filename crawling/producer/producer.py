import os
from typing import List

from kafka import KafkaProducer
import json
from dotenv import load_dotenv

from entity.articleData import ArticleData

profile = os.getenv("PROFILE", "dev")
load_dotenv(dotenv_path=f".env.{profile}")


class MessageProducer:
    broker = ""
    topic = ""
    producer = None

    def __init__(self, broker, topic):
        self.broker = broker
        self.topic = topic
        self.producer = KafkaProducer(bootstrap_servers=self.broker,
                                      value_serializer=lambda x: json.dumps(x).encode('utf-8'),
                                      acks=0,
                                      api_version=(3,9,0),
                                      retries=3
                                      )

    def send_message(self, msg):
        try:
            future = self.producer.send(self.topic, msg)
            self.producer.flush()   # 비우는 작업
            future.get(timeout=60)
            return {'status_code': 200, 'error': None}
        except Exception as e:
            print("error:::::",e)
            return e


async def send_data(data: List[ArticleData]):

    topic = os.getenv("TOPIC", "raw_article")
    broker = os.getenv("BROKER", "localhost:9092")

    message_producer: MessageProducer = MessageProducer(broker, topic)

    for article in data:
        res = message_producer.send_message(article.json())
        print(f"{res}, {topic}, {broker}")
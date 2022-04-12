from kafka import KafkaProducer
from kafka.errors import KafkaError
from datetime import datetime
from time import sleep
from os import getenv
import logging
import json


logging.basicConfig(level=logging.INFO)

n_connect_retry = 20

while n_connect_retry > 0:
    try:
        producer = KafkaProducer(
            bootstrap_servers=["kafka-in1-url:9092"], retries=3,
            value_serializer=lambda m: json.dumps(m).encode("ascii")
        )
        n_connect_retry = 0
    except KafkaError:
        n_connect_retry -= 1
        if n_connect_retry == 0:
            logging.error("FAILED to connect to Kafka broker")
        sleep(1.5)

# launch a limited number of messages
for _ in range(30):
    message = {"time": datetime.utcnow().isoformat() + "Z"}
    producer.send("json-time-topic", message)
    logging.info(f"sent message {message}")
    sleep(3)
producer.flush()

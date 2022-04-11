import json
from kafka import KafkaConsumer
from kafka.errors import KafkaError
from time import sleep
import logging

logging.basicConfig(level=logging.INFO)

n_connect_retry = 20

while n_connect_retry > 0:
    try:
        json_consumer = KafkaConsumer("json-time-topic",
                                      bootstrap_servers=["kafka-in1-url:9092"],
                                      value_deserializer=lambda m: json.loads(m.decode('ascii')),
                                      enable_auto_commit=False,
                                      auto_offset_reset='earliest',
                                      group_id='json-consumer-group')
        n_connect_retry = 0
    except KafkaError:
        n_connect_retry -= 1
        if n_connect_retry == 0:
            logging.error("FAILED to connect to Kafka broker")
        sleep(1.5)

# this loop runs forever
for message in json_consumer:
    logging.info(f"received message {message}")

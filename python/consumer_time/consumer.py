import json
from kafka import KafkaConsumer
#import logging
#logging.basicConfig(level=logging.DEBUG)

json_consumer = KafkaConsumer("json-time-topic",
                              bootstrap_servers=["127.0.0.1:29092"],
                              value_deserializer=lambda m: json.loads(m.decode('ascii')),
                              enable_auto_commit=False,
                              auto_offset_reset='earliest',
                              group_id='json-consumer-group')

# this loop runs forever
for message in json_consumer:
    print(message)

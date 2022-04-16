# kafka-streams-play

Play with kafka to make some kafka streams. The kafka server is a bitnami docker image.

There is a docker-compose.yml in services, containing:
  - a kafka server
  - a zookeeper server (for kafka)
  - a python producer (publishing to topic 'json-time-topic')
  - a kafka stream in a scala app (pulling messages from 'json-time-topic', transforming, and sending to 'json-edited-time-topic')
  - a python consumer (subscribing to topic 'json-edited-time-topic')

The demo can be run with
```
# in services
docker-compose build
docker-compose up
```
The producing and consuming of messages should be visible in the docker logs. It may take a couple of minutes for services to settle down, and previous messages to be flushed.

On docker-compose up, containers and volumes will be created. After services have been stopped, the volumes can be cleared down using
```
docker volume prune
```

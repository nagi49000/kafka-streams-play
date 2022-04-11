# kafka-play

Play with kafka to make some kafka streams. The kafka server is a bitnami docker image.

There is a docker-compose.yml in services, containing:
  - a kafka server
  - a zookeeper server (for kafka)
  - a python producer
  - a python consumer

The demo can be run with
```
# in services
docker-compose build
docker-compose up
```
The producing and consuming of messages should be visible in the log.
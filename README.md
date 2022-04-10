# kafka-play

Play with kafka to make some simple data pipelines

The kafka server is a bitnami docker image, and can be launched with docker-compose.

Simple producers and consumers in python have been built. The environment for them can be created using conda
```
conda env create -f environment.yml
```

The kafka consumer will run forever, and can be run in a terminal
```
python consumer.py
```

The kafka producer injects a small number of messages over time, and then finishes. It can be run in the terminal
```
python producer.py
```
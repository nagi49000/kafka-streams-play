FROM python:alpine

WORKDIR /app

COPY requirements.txt ./
RUN pip install -r requirements.txt
COPY consumer.py ./

CMD ["python", "consumer.py"]
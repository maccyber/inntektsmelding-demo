docker exec -it `basename "$PWD"`_kafka_1 kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test

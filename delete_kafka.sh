docker exec -it `basename "$PWD"`_kafka_1 kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic test

# {"@id": "cb1c0c0d-5862-4334-8549-46885c9beae2", "@event_name": "InntektsmeldingInformasjon", "@behov": ["VirksomhetsInformasjon", "Arbeidsforhold"], "virksomhetsNummer": "874568112", "fnr": "33112211221"}
echo 'Forslag til melding: {"@id": "cb1c0c0d-5862-4334-8549-46885c9beae2", "@event_name": "InntektsmeldingInformasjon", "@behov": ["VirksomhetsInformasjon", "Arbeidsforhold"], "virksomhetsNummer": "874568112", "fnr": "33112211221"}'
docker exec -it `basename "$PWD"`_kafka_1 kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic test

#!/bin/zsh
# 카프카 설치
wget https://downloads.apache.org/kafka/3.9.0/kafka_2.13-3.9.0.tgz -P ~/kafka

# 압축 해제
tar -xvf ~/kafka/kafka_2.13-3.9.0.tgz -C ~/kafka

# zookeeper 구동
~/kafka/kafka_2.13-3.9.0/bin/zookeeper-server-start.sh -daemon ~/kafka/kafka_2.13-3.9.0/config/zookeeper.properties

# kafka 구동
~/kafka/kafka_2.13-3.9.0/bin/kafka-server-start.sh -daemon ~/kafka/kafka_2.13-3.9.0/config/server.properties

# 토픽 생성
~/kafka/kafka_2.13-3.9.0/bin/kafka-topics.sh --create --topic raw_news --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3

# 토픽 확인
~/kafka/kafka_2.13-3.9.0/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
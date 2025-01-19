#!/bin/zsh
# 카프카 설치
wget https://downloads.apache.org/kafka/3.9.0/kafka_2.13-3.9.0.tgz -P ~/kafka

# 압축 해제
tar -xvf ~/kafka/kafka_2.13-3.9.0.tgz -C ~/kafka

# 카프카 설정 (listener 추가)
# shellcheck disable=SC2129
echo "listeners=LOCAL://localhost:9092,DOCKER://localhost:29092" >> ~/kafka/kafka_2.13-3.9.0/config/server.properties
echo "advertised.listeners=LOCAL://localhost:9092,DOCKER://host.docker.internal:29092" >> ~/kafka/kafka_2.13-3.9.0/config/server.properties
echo "listener.security.protocol.map=LOCAL:PLAINTEXT,DOCKER:PLAINTEXT" >> ~/kafka/kafka_2.13-3.9.0/config/server.properties
echo "inter.broker.listener.name=LOCAL" >> ~/kafka/kafka_2.13-3.9.0/config/server.properties

# zookeeper 구동
~/kafka/kafka_2.13-3.9.0/bin/zookeeper-server-start.sh -daemon ~/kafka/kafka_2.13-3.9.0/config/zookeeper.properties

# kafka 구동
~/kafka/kafka_2.13-3.9.0/bin/kafka-server-start.sh -daemon ~/kafka/kafka_2.13-3.9.0/config/server.properties

# 토픽 생성
~/kafka/kafka_2.13-3.9.0/bin/kafka-topics.sh --create --topic raw-article --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3
~/kafka/kafka_2.13-3.9.0/bin/kafka-topics.sh --create --topic refined-article --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3
## dev 용 토픽 생성
~/kafka/kafka_2.13-3.9.0/bin/kafka-topics.sh --create --topic raw-article-dev --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3
~/kafka/kafka_2.13-3.9.0/bin/kafka-topics.sh --create --topic refined-article-dev --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3

# 토픽 확인
~/kafka/kafka_2.13-3.9.0/bin/kafka-topics.sh --list --bootstrap-server localhost:9092

# 로그 확인
# tail -f ~/kafka/kafka_2.13-3.9.0/logs/server.log
시작하세요! 도커/쿠버네티스 책을 읽고 구현한 k8s 로컬 실습

# 0. 사용 기술
 - kotlin/spring, vue.js
 - websocket(stomp)
 - kafka
 - kubenetes

# 1. 디렉토리
- `kafka` : kafka 설치 스크립트
- `kind` : kind 클러스터 생성 스크립트
- `k8sfiles` : k8s 리소스 파일
- `crawling` : 뉴스 사이트에서 뉴스를 읽어서 kafka 토픽에 전송하는 서비스
- `k8s-java-consumer` : kafka 토픽에서 뉴스를 읽어서 데이터를 정제하고 DB에 저장하는 서비스

# 1. 실행 환경
- macOS
- docker 설치
- 명령어 실행 위치 : k8s 디렉토리 (깃저장소 루트)

## 1.1. kind 로 클러스터 배포
1. kind, kubectl 설치
- `brew install kind`
- `brew install kubectl`

2. kind 로 클러스터 생성
- `kind/kind-start.sh` 실행
  - kind 로 클러스터 생성
- `kind/node/kind-nodes.sh` 실행
  - kind 클러스터 노드 생성 (worker 3개)

## 1.2. kafka 설치
- `kafka/install-kafka.sh` 실행
    - zookeeper 실행
    - kafka 실행 (localhost:9092, 29092) - 로컬용, 도커용 리스너
    - raw-article 토픽 생성
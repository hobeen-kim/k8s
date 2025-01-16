시작하세요! 도커/쿠버네티스 책을 읽고 구현한 k8s 로컬 실습

# 0. 실행 환경
- macOS
- docker 설치
- 명령어 실행 위치 : k8s 디렉토리 (깃저장소 루트)

# 1. kind 로 클러스터 배포
1. kind, kubectl 설치
- `brew install kind`
- `brew install kubectl`

2. kind 로 클러스터 생성
- `kind/kind-start.sh` 실행
  - kind 로 클러스터 생성
- `kind/node/kind-nodes.sh` 실행
  - kind 클러스터 노드 생성 (worker 3개)
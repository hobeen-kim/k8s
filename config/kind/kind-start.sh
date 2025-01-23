#!/bin/zsh
echo "Starting kind cluster"

# 클러스터 배포
kind create cluster

# 클러스터 확인
kind get clusters
kind get nodes

# 노드 정보 확인
kubectl get node -o wide

# 파드 정보 확인
kubectl get pod -A

# 도커 컨테이너 확인
docker ps
#!/bin/zsh
# 멀티 노드 클러스터 생성
kind create cluster --config node-info.yaml --name myk8s

# 노드 확인
kind get nodes --name myk8s
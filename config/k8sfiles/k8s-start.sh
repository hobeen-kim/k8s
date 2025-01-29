#!/bin/zsh
kubectl apply -f mongodb/mongodb-statefulset.yaml
kubectl apply -f article-crawling-server/
kubectl apply -f article-refine-server/
kubectl apply -f article-was-server/

# 20초 대기
sleep 20

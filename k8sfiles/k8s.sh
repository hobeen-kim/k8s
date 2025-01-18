#!/bin/zsh
kubectl apply -f crawling.yaml

kubectl apply -f mongodb/mongodb-secret.yaml
kubectl apply -f mongodb/mongodb-configmap.yaml
kubectl apply -f mongodb/mongodb-service.yaml
kubectl apply -f mongodb/mongodb-statefulset.yaml
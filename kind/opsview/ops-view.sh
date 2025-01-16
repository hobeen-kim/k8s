#!/bin/zsh
# kube-ops-view
helm repo add geek-cookbook https://geek-cookbook.github.io/charts/
helm install kube-ops-view geek-cookbook/kube-ops-view --version 1.2.2 --set service.main.type=NodePort,service.main.ports.http.nodePort=31000 --set env.TZ="Asia/Seoul" --namespace kube-system

# 설치 확인
kubectl get deploy,pod,svc,ep -n kube-system -l app.kubernetes.io/instance=kube-ops-view

# kube-ops-view 접속 URL 확인 (1.5 배율)
echo -e "KUBE-OPS-VIEW URL = http://localhost:31000/#scale=1.5"
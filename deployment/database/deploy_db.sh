#!/bin/bash

# Namespace 존재 여부 확인 후 생성
if ! kubectl get namespace lifesub-ns &> /dev/null; then
    kubectl create namespace lifesub-ns
fi

# Namespace 전환
kubens lifesub-ns

# 각 서비스별 설정 및 배포
for service in member mysub recommend; do
    # values 파일 생성
    cat << EOF > values-${service}.yaml
# PostgreSQL 아키텍처 설정
architecture: standalone
# 글로벌 설정
global:
  postgresql:
    auth:
      postgresPassword: "Passw0rd"
      replicationPassword: "Passw0rd"
      database: "${service}"
      username: "admin"
      password: "Passw0rd"
  storageClass: "managed"

# Primary 설정
primary:
  persistence:
    enabled: true
    storageClass: "managed"
    size: 10Gi

  resources:
    limits:
      memory: "1Gi"
      cpu: "1"
    requests:
      memory: "0.5Gi"
      cpu: "0.5"

# 네트워크 설정
service:
  type: ClusterIP
  ports:
    postgresql: 5432
# 보안 설정
securityContext:
  enabled: true
  fsGroup: 1001
  runAsUser: 1001
EOF

    # Service 파일 생성
    cat << EOF > svc-${service}.yaml
apiVersion: v1
kind: Service
metadata:
  name: ${service}-external
spec:
  ports:
  - name: tcp-postgresql
    port: 5432
    protocol: TCP
    targetPort: tcp-postgresql
  selector:
    app.kubernetes.io/component: primary
    app.kubernetes.io/instance: ${service}
  sessionAffinity: None
  type: LoadBalancer
EOF

    # Helm으로 PostgreSQL 설치
    helm upgrade -i ${service} -f values-${service}.yaml bitnami/postgresql --version 14.3.2

    # 외부 서비스 생성
    kubectl apply -f svc-${service}.yaml
done
#!/bin/bash

# 사용법 함수 정의
usage() {
    echo "Usage: $0 <namespace>"
    echo "Example: $0 myapp-ns"
    echo "This script creates PostgreSQL databases for member, mysub, and recommend services in the specified namespace."
    exit 1
}

# 파라미터 체크
if [ $# -ne 1 ]; then
    usage
fi

NAMESPACE=$1

# Namespace 존재 여부 확인 후 생성
if ! kubectl get namespace ${NAMESPACE} &> /dev/null; then
    echo "Creating namespace: ${NAMESPACE}"
    kubectl create namespace ${NAMESPACE}
fi

# Namespace 전환
echo "Switching to namespace: ${NAMESPACE}"
kubens ${NAMESPACE}

# 각 서비스별 설치
for service in member mysub recommend; do
    echo "Installing PostgreSQL for ${service} service..."

    # Helm으로 PostgreSQL 설치 - heredoc으로 직접 values 전달
    helm upgrade -i ${service} bitnami/postgresql --version 14.3.2 --values - <<EOF
architecture: standalone
global:
  postgresql:
    auth:
      postgresPassword: "Passw0rd"
      replicationPassword: "Passw0rd"
      database: "${service}"
      username: "admin"
      password: "Passw0rd"
  storageClass: "managed"
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
service:
  type: ClusterIP
  ports:
    postgresql: 5432
securityContext:
  enabled: true
  fsGroup: 1001
  runAsUser: 1001
EOF

done

echo "Installation completed successfully in namespace: ${NAMESPACE}"
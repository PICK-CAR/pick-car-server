# 파싱할 환경 변수 가져오기
IFS=';' read -r -a env_vars <<< "$COMBINED_ENV"

# 각 환경 변수를 파싱하고 export
for env_var in "${env_vars[@]}"; do
    export "$env_var"
done
# DIM0863-ws

curl -X GET -H "content-Type: application/json"  http://localhost:8080/api/v1/status | json_pp 

curl -i -X POST -H "content-Type: application/json" -d '{"login":"luksrn", "chaveiro":"KEYCHAIN_001"}' http://localhost:8080/api/v1/chaveiro

curl -i -X POST -H "content-Type: application/json" -d '{"login":"luksrn", "chave": 2}' http://localhost:8080/api/v1/portao

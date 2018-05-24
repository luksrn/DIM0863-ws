# DIM0863-ws

curl -X GET -H "content-Type: application/json"  http://192.168.1.100:8080/api/v1/status/luksrn | json_pp 

curl -i -X POST -H "content-Type: application/json" -d '{"login":"luksrn", "chaveiro":"KEYCHAIN_001"}' http://192.168.1.100:8080/api/v1/chaveiro

curl -i -X POST -H "content-Type: application/json" -d '{"login":"luksrn", "chave": 2}' http://192.168.1.100:8080/api/v1/portao

curl -i -X POST -H "content-Type: application/json" -d '{"login":"luksrn", "chave": 2}' http://192.168.1.100:8080/api/v1/sensor-portao

curl -i -X POST -H "content-Type: application/json" -d 'Teste' http://192.168.1.100:8080/api/v1/leitor-rfid/change

curl -i -X POST -H "content-Type: application/json" -d 'Teste' http://192.168.1.100:8080/api/v1/sensor-portao/change



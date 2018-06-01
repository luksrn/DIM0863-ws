# DIM0863-ws

#### Consultar status do usuário
```
curl -X GET -H "Content-Type: application/json"  http://localhost:8080/api/v1/status/luksrn | json_pp 
```

#### Simular leitura da chave
```
curl -i -X POST -H "Content-Type: application/json" -d '{"login":"luksrn", "chaveiro":"KEYCHAIN_001"}' http://localhost:8080/api/v1/chaveiro
```

#### Simular identificação no leitor RFID do portão
```
curl -i -X POST -H "Content-Type: application/json" -d '{"login":"luksrn", "chave": 2}' http://localhost:8080/api/v1/portao
```

#### Simular identificação no sensor de distância do portão
```
curl -i -X POST -H "Content-Type: application/json" -d '{"login":"luksrn", "chave": 2}' http://localhost:8080/api/v1/sensor-portao
```

#### Enviar última posição do usuário
```
curl -i -X POST -H "Content-Type: application/json" -d '{"login":"luksrn", "localizacao": {"data": "2018-05-31 23:05:41.001", "latitude": 1.213213, "longitude": 2.12312 }}' http://localhost:8080/api/v1/localizacao
```

#### Consultar posições do usuário
```
curl -X GET -H "Content-Type: application/json"  http://localhost:8080/api/v1/localizacao/luksrn | json_pp 
```
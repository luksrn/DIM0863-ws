# UFRN Drivers

#### Consultar status do usuário
```
curl -X GET -H "Content-Type: application/json"  http://localhost:8080/api/v1/status/luksrn | json_pp 
```

#### Simular leitura da chave
```
curl -i -X POST -H "Content-Type: application/json" -d '{"login":"luksrn", "chaveiro":"KEYCHAIN_001"}' http://localhost:8080/api/v1/keychain
```

#### Simular interação com o portão
```
curl -i -X POST -H "Content-Type: application/json" -d '{"login":"luksrn", "chave": 2}' http://localhost:8080/api/v1/gate
```

#### Simular identificação no leitor RFID do portão
```
curl -i -X POST -H "Content-Type: application/json" -d '{"contextResponses":[{"contextElement":{"attributes":[{"value":"000000000000000000007546"}]}}]}' http://localhost:8080/api/v1/gate/rfid/change
```


### Localização do Usuário

#### Enviar localização do usuário
```
curl -i -X POST -H "Content-Type: application/json" -d '{"login":"luksrn", "location": {"date": "2018-05-31 23:05:41.000", "lat": -5.7999146, "lon": -35.2922851 }}' http://localhost:8080/api/v1/location
```

#### Consultar localizações do usuário
```
curl -X GET -H "Content-Type: application/json"  http://localhost:8080/api/v1/location/luksrn | json_pp
```


### Dados do Veículo

#### Enviar dados do veículo
```
curl -i -X POST -H "Content-Type: application/json" -d '{"licensePlate":"ABC1234", "carInfo": {"date": "2018-05-31 23:05:41.000", "speed": 120, "rpm": 5200 }}' http://localhost:8080/api/v1/car/data
```

#### Consultar dados do veículo
```
curl -X GET -H "Content-Type: application/json"  http://localhost:8080/api/v1/car/data/ABC1234 | json_pp
```


### Firebase

#### Enviar token Firebase atualizado do dispositivo
```
curl -i -X POST -H "Content-Type: application/json" -d '{"login":"luksrn", "token":"abc123def456xyz890"}' http://localhost:8080/api/v1/firebase/update-token
```

#### Iniciar coleta de dados no dispositivo de um usuário específico
```
curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/api/v1/firebase/notify/luksrn/start
```

#### Parar coleta de dados no dispositivo de um usuário específico
```
curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/api/v1/firebase/notify/luksrn/stop
```


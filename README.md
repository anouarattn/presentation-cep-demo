# presentation-cep-demo


l'exemple traiter dans la demo est celui presenté dans la page 11 du fichier PREZ CEP.pptx

On veut detecter la cas fraude bancaire lorsque une personne fait une transaction avec sa carte et juste après dans un interval de 30 min une autre personne qui se trouve a une distance de plus de 100 km va utiliser les informations de cette meme carte pour faire une transaction, la personne a qui appartient la carte ne peux pas se deplacer a plus de 100 km dans un interval de 30 min, les deux exemples avec apache flink cep et tibco be permettent de detecter ce cas. on filtre les transactions qui sont derierre un proxy.

### Prérequis

- jdk 8
- Docker ( avec le docker compose)
- Tibco businessEvents 5.5

####  Etapes pour lancer les demos

0. dans le fichier hosts mettre ces valeurs 
127.0.0.1     kafka1
127.0.0.1     zoo1
127.0.0.1     kafka-schema-registry
127.0.0.1     kafka-schema-registry-ui
127.0.0.1     kafka-rest-proxy
127.0.0.1     kafka-topics-ui
127.0.0.1     kafka-connect-ui
127.0.0.1     zoonavigator-web
127.0.0.1     zoonavigator-api

1.  starter le cluster kafka zookeeper en backgroun avec la commande 
```bash
docker-compose -f zk-single-kafka-single.yml up -d
```
1.  lancer un bash sur la machine kafka kafka1 avec la commande
```bash
docker exec -it -u cp-kafka kafka-docker_kafka1_1 /bin/bash
```
1. creer un topic kafka avec la commande 
```bash
/usr/bin/kafka-topics --create --zookeeper zoo1:2181 --replication-factor 1 --partitions 2 --topic T.CustomerTransaction
```
1. lancer l'un des projet 
flinkcep-cep-demo-fraud-detector
tibcobe-cep-demo-fraud-detector

1. lancer le producer des messages kafka
transactionMessagesProducer

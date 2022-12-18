# Introduction

Projet simulant une prise de pari en live sur un événement sportif

## Lancement
Afin de lancer le projet il est nécessaire d'installer les dépendances du projet et de générer
l'archive JAR du projet pour cela il est possible de lancer la commande
``
mvn clean install
``

Le jar sera alors généré à l'emplacement /target/unibet-live-test-0.0.1.jar

Il est ensuite possible d'éxécuter le projet à l'aide de java 17 en éxécutant la commande : 

``
java -Xms128M -Xmx256M -jar /target/unibet-live-test-0.0.1.jar
``

ou en utilisant votre IDE favori et en lançant la classe 'UnibetLiveTestApplication'

Le projet est alors disponible à l'url : http://localhost:8887

## Fichier de configuration
Le fichier de configuration disponible dans /src/resources/application.properties
permet de configurer le délai d'éxécution du batch de paiement des paris via la clé :
market.batch.rate=${VALEUR} où VALEUR est une durée en ms.

## Swagger 
Un swagger est disponible à l'URL : http://localhost:8887/swagger-ui/index.html
afin de faciliter l'exploration des API Rest disponibles.

## Postman 
Une collection Postman prête à l'emploi (version 2.1) est disponible sous le nom :
UniLiveTest.postman_collection.json


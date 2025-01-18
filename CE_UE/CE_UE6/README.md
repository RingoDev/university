## Schnellstart Vorraussetzung

* Java JDK 15+
* docker
* docker-compose

Terminal in diesen Ordner navigieren.

Das komplette Netzwerk kann gestartet werden mit `docker-compose up`.

Die Website kann dann über die Adresse http://localhost:3000 aufgerufen werden.

Der Terminalclient kann via Terminalbefehl `java -jar client/out/artifacts/client_jar/client.jar` aufgerufen werden.

Die FiBu Datenbank Datei befindet sich in factory/fibu/order.txt

Die Server Datenbank kann eingesehen werden unter http://localhost:8080/h2-console mit den Daten:

* username: "sa" 
* passwort ""
* JDBC Url: `jdbc:h2:/data/producer`

### Aufsetzen des Systems

Jede der vier Anwendungen sollte auf einem bestimmten Port gestartet werden.

* FleetSystem: 5000
* Routes: 5001
* schedule: 5002
* tickets: 5003

Um die Anwendungen laufen zu können sollte zuerst:

* ein venv in jeder der Anwendungen angelegt werden -> In Pycharm einen Python Interpreter (3.8+) mit VirtualEnv
  konfigurieren
* die dependencies in der requirements.txt installiert werden -> entweder Datei in PyCharm öffnen und das PopUp
  akzeptieren oder mit dem Befehl `pip install -r requirements.txt`

Daraufhin sollte jede Anwendung einzeln mit dem Befehl `flask app.py -p <port>` gestartet werden können (<port> muss
durch den jeweiligen Port ersetzt werden).

### Vorgegebene Daten

Die Anwendungen wurden mit Daten vorbefüllt. In jedem System wurde ein Admin Benutzer angelegt:

* username: admin
* password: 12345

Es wurde auch ein nicht privilegierter Benutzer angelegt:

* username: user
* password: 12345

Bei der Verwendung aller Systeme sollten Daten im Flotten und Streckensystem nicht gelöscht werden, da es sonst zu
Fehlern im Fahrplan und TicketSystemen kommen wird.

### Testen der Systeme in Isolation

Wenn das FahrplanInformationssystem mit Dummy Daten verwendet werden soll, dann muss die variable "useRestApis" in der
Datei "fetch.py" auf `False` gesetzt werden. Analog dazu kann im Ticket System die variable "dynamic" auf den `False`
gesetzt werden, um mit dummy Daten zu arbeiten.

Da die Systeme jedoch mit dynamischen Daten befüllt wurden und abhängigkeiten auf diese haben, müssten hierfür zuerst
alle Daten bis auf die User Daten aus diesen Systemen (Fahrplan und Ticket) gelöscht werden.
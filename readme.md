# Sync app
## Dit is een Java spring applicatie ten behoeve van het syncen van een elasticsearch index.
###### De applicatie heeft een drietal manieren van indexeren

* Inlezen CSV;
* Op basis van Kenteken reeksen gegevens ophalen bij de RDW en in de index plaatsen;
* Het laatst uitgegeven kenteken ophalen en deze en omliggende range in de index plaatsen;

![alt text](scheme.jpg "Title")


Functionaliteit | Omschrijving
------------ | -------------
CSV | Zet in application.properties app.csv=Y 
Reeksen | app.csv=N en zet de startreeks app.startplate=DBBBAA



DELETE rdw

PUT rdw

PUT rdw/_mapping 
{
"properties": {
        "Kenteken": { "type": "text" },
        "Merk": { "type": "text" },
        "Handelsbenaming": { "type": "text" },
        "EersteKleur": { "type": "text" },
        "TweedeKleur": { "type": "text" },
        "DatumEersteToelating": { "type": "text" },
        "Voertuigsoort": { "type": "text" },
        "Inrichting": { "type": "text" },
        "message": { "type": "text" }
      }
}

DELETE zoektermen

PUT zoektermen

PUT zoektermen/_mapping 
{
"properties": {
        "zoekterm": { "type": "text" },
        "herkomst": { "type": "text" }
      }
}


PUT /zoektermen/_doc/bmw
{
  "zoekterm": "bmw",
  "herkomst": "handmatig"
}

PUT /zoektermen/_doc/groen
{
  "zoekterm": "groen",
  "herkomst": "handmatig"
}

GET zoektermen/_search
{
    "query": {
        "match_all": {}
    }
}

GET rdw/_search
{
    "query": {
        "match_all": {}
    }
}






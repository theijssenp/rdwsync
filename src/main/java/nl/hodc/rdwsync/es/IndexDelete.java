package nl.hodc.rdwsync.es;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class IndexDelete {
    public static void deleteRdwEntry(String kenteken, String esserverip) {
        HttpHeaders indexHeader = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        indexHeader.add("Content-Type", "application/json");
        String jsonString = "{\"query\": {\"match\": {\"Kenteken\": \" " + kenteken + "\"     }    }  }";
        HttpEntity<String> entityIndex = new HttpEntity<String>(jsonString, indexHeader);
        String indexURL = esserverip + "/rdw/_delete_by_query";
        // System.out.println(indexURL);
        ResponseEntity<String> responseIndex = restTemplate.exchange(indexURL, HttpMethod.POST, entityIndex,
                String.class);
        if (responseIndex.getStatusCode() != HttpStatus.OK) {
            System.out.println("ERROR - Status request niet OK bij index delete actie " + kenteken);
        }
    }

}

package nl.hodc.rdwsync;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class IndexPost {

    public static void postRdwEntry(String jsonString, String kenteken , String esserverip) {
        HttpHeaders indexHeader = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        indexHeader.add("Content-Type", "application/json");

        HttpEntity<String> entityIndex = new HttpEntity<String>(jsonString, indexHeader);
        String indexURL = esserverip + "/rdw/_doc/" + kenteken;
        ResponseEntity<String> responseIndex = restTemplate.exchange(indexURL, HttpMethod.POST, entityIndex,
                String.class);
        if (responseIndex.getStatusCode() != HttpStatus.CREATED ) {
            System.out.println("INFO - Status request niet CREATED bij index bijwerken " + kenteken);
        }
    }

}

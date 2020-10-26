package nl.hodc.rdwsync;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class IndexPost {

    public static void postRdwEntry(String jsonString, String kenteken) {
        HttpHeaders indexHeader = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        indexHeader.add("Content-Type", "application/json");

        HttpEntity<String> entityIndex = new HttpEntity<String>(jsonString, indexHeader);
        String indexURL = "http://192.168.1.75:9200/rdw/kentekens/"+kenteken;
        ResponseEntity<String> responseIndex = restTemplate.exchange(indexURL, HttpMethod.POST, entityIndex,
                String.class);
        System.out.println("POST RESPONS: "+responseIndex.getBody());
    }

}

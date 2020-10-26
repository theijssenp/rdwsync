package nl.hodc.rdwsync;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
public class AppProperties {

    private String startplate;

    public String getStartplate() {
        return startplate;
    }

    public void setStartplate(String startplate) {
        this.startplate = startplate;
    }

}
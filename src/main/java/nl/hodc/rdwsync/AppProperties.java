package nl.hodc.rdwsync;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
public class AppProperties {

    private String startplate;
    private String uitsluiten;

    public AppProperties() {
    }

    public AppProperties(String startplate, String uitsluiten) {
        this.startplate = startplate;
        this.uitsluiten = uitsluiten;
    }

    public String getUitsluiten() {
        return this.uitsluiten;
    }

    public void setUitsluiten(String uitsluiten) {
        this.uitsluiten = uitsluiten;
    }

    public AppProperties startplate(String startplate) {
        this.startplate = startplate;
        return this;
    }

    public AppProperties uitsluiten(String uitsluiten) {
        this.uitsluiten = uitsluiten;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AppProperties)) {
            return false;
        }
        AppProperties appProperties = (AppProperties) o;
        return Objects.equals(startplate, appProperties.startplate) && Objects.equals(uitsluiten, appProperties.uitsluiten);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startplate, uitsluiten);
    }

    @Override
    public String toString() {
        return "{" +
            " startplate='" + getStartplate() + "'" +
            ", uitsluiten='" + getUitsluiten() + "'" +
            "}";
    }

    public String getStartplate() {
        return startplate;
    }

    public void setStartplate(String startplate) {
        this.startplate = startplate;
    }



}
package gr.ypes.qnationality.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service")
public class StorageProperties {

    private String location = this.getClass().getClassLoader().getResource("static").getPath();

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
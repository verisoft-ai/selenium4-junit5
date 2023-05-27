package co.verisoft.fw.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Getter
@Component
public class SeleniumProperties {

    @Value("${driver.url:null}")
    private String driverUrl;

    @Bean
    URL driverUrl(){

        try{
            URL url = new URL(driverUrl);
            return url;
        }
        catch (MalformedURLException ex){
            return null;
        }
    }
}

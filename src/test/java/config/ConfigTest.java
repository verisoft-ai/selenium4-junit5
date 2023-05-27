package config;

import co.verisoft.fw.config.EnvConfig;
import co.verisoft.fw.config.SeleniumProperties;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class ConfigTest extends BaseTest {


    @Autowired
    @Nullable URL url;

    @Autowired
    private SeleniumProperties seleniumProperties;

    @Test
    public void test() throws MalformedURLException {
        if (url == null){
            Assertions.assertEquals(seleniumProperties.getDriverUrl(), "null", "should both be null");
        }
        else{
            String fromURL = url.getProtocol() + "://" + url.getHost()+ ":"+ url.getPort() + url.getPath();
            String fromProperties = seleniumProperties.getDriverUrl();
            Assertions.assertEquals(fromURL, fromProperties, "Both urls should be identical");
        }



    }

}

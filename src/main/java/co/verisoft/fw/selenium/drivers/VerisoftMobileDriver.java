package co.verisoft.fw.selenium.drivers;

import org.openqa.selenium.Capabilities;

import java.net.URL;

public class VerisoftMobileDriver extends VerisoftDriver {

    public VerisoftMobileDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public VerisoftMobileDriver(URL remoteAddress, Capabilities capabilities) {
        super(remoteAddress, capabilities);
    }
}

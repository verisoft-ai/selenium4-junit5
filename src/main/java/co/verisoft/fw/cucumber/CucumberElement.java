package co.verisoft.fw.cucumber;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class CucumberElement {
    private final String name;
}

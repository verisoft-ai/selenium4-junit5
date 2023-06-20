package co.verisoft.fw.objectrepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Locator implements Comparable{
    private String type;
    private String value;
    private int grade;

    @Override
    public int compareTo(@NotNull Object o) {
        return  ((Locator) o).getGrade() - this.grade;
    }
}

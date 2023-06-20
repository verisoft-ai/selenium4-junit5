package co.verisoft.fw.objectrepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ObjectRepository {
    private List<LocatorObject> objectsRepository;
}

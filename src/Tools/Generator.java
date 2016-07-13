package Tools;

import java.util.LinkedHashMap;

/**
 * Created by Phuongt994 on 10/07/2016.
 */
public class Generator {
    private LinkedHashMap<Integer, Float[]> LR;
    public Generator(LinkedHashMap<Integer, Float[]> LR) {
        this.LR = LR;
        aPriori(this.LR);

    }

    private void aPriori(LinkedHashMap<Integer, Float[]> LR) {

    }
}

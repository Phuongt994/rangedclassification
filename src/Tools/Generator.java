package Tools;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Phuongt994 on 10/07/2016.
 */
public class Generator {
    private LinkedHashMap<Integer, LinkedList<Float[]>> LR;
    private String classTag;
    public Generator(String classTag, LinkedHashMap<Integer, LinkedList<Float[]>> LR) {
        this.LR = LR;
        this.classTag = classTag;
        System.out.println("Generator started for " + classTag);
        System.out.println("LR entries: ");
        for (Object key : LR.keySet()) {
            System.out.println((Integer) key);
            for (Float[] fl : LR.get(key)) {
                System.out.println(Arrays.asList(fl));
            };
        }
        aPriori(this.LR);
    }

    private void aPriori(LinkedHashMap<Integer, LinkedList<Float[]>> LR) {
        for (Float[] range : LR.entrySet())
                }
            }
        }
    }
}

package Tools;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Phuongt994 on 10/07/2016.
 */
public class Generator {
    private LinkedList<LinkedList<Float>> CR;
    private String classTag;
    public Generator(String classTag, LinkedList<LinkedList<Float>> CR) {
        this.CR = CR;
        this.classTag = classTag;
        System.out.println("Generator started for " + classTag);
        //System.out.println("CR entries");
        aPriori(this.CR);
    }

    private void aPriori(LinkedList<LinkedList<Float>> CR) {
        for (int i = 0; i < CR.size(); i++) {
            LinkedList CRG = new LinkedList<LinkedList<LinkedList<Float>>>();
            LinkedList<Float> target = CR.pop();
            for (int j = 0; j < CR.size(); j++) {
                LinkedList temp = new LinkedList<LinkedList<Float>>();
                if (Math.round(target.getLast()) != Math.round(CR.get(j).getLast())) {
                    temp.add(target);
                    temp.add(CR.get(j));
                    CRG.add(temp);
                }
            }
            System.out.println("CR entries after REGEN: ");
            for (int k = 0; k < CRG.size(); k++) {
                System.out.println((CRG.get(k)).toString());
            }
        }
    }
}

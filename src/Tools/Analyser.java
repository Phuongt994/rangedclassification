package Tools;

import java.util.*;

/**
 * Created by phuongt994 on 28/06/2016.
 */
public class Analyser {
    private HashMap classMap;
    private LinkedList tupleVal;

    public Analyser(HashMap classMap) {
        this.classMap = new HashMap<>(classMap);
        maxMin(this.classMap);
    }

    private void maxMin(HashMap<String, LinkedList<LinkedList>> map) {
//        map.keySet().forEach(k -> {
//            for (int i = 1; i < 5; i++) {
//                System.out.println(map.get(k).get(i).getClass());
//                tupleVal= new LinkedList<Float>();
//                tupleVal.addAll(map.get(k).get(i).get(1));
//                System.out.println("Analyser tuple value attr" + i + " for class " + k + " is: " + tupleVal);
//                Collections.sort(tupleVal);
//                System.out.println("Max of attr " + i + "is: " + tupleVal.get(tupleVal.size() - 1));
//                System.out.println("Min of attr " + i + "is: " + tupleVal.get(0));
            }
        });
    }

    private void maxSum(LinkedList<List> list) {

    }

}

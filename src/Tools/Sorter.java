package Tools;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by phuongt994 on 17/06/2016.
 */

public class Sorter {
    private LinkedList<LinkedList> tuple;
    private HashSet classes;
    private HashMap classMap;

    public Sorter(LinkedList tuple, HashSet classes) {
        // {?} classes may need refactor from set to list
        this.tuple = tuple;
        this.classes = classes;

        // make a dictionary for class tags
        classMap = new HashMap<String, LinkedList<LinkedList>>();


        // run sort by class tag
        tupleSortByClass();

        // analyser initialised
        new Analyser(classMap);

    }

    private void tupleSortByClass() {
        this.classes.stream().forEach(c -> {
            classMap.put(c, null);
        });

        // for each class as key in Map
        classMap.keySet().stream().forEach(c -> {
            LinkedList temp = new LinkedList<List>();
            // for each tuple
            for (int i = 0; i < tuple.size(); i++) {
                int order = i;
                // if tuple's class == class name
                // add to temp tuple list
                if (tuple.get(order).get(5).equals(c)) {
                    temp.add(tuple.get(order));
                }
                // append temp list to relevant class key in Map
                classMap.put(c, temp);
            }
        });
        System.out.println("Map of class-based tuples: " + classMap.entrySet());
        reportMap();
    }

    private void reportMap() {
        System.out.println("\nReport: ");
        classMap.keySet().stream().forEach(k -> {
            System.out.println("Class: " + k + "\nTuples: " + classMap.get(k));
        });
    }

}
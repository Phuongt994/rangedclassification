package Tools;

import java.util.*;

/**
 * Created by phuongt994 on 28/06/2016.
 */
public class Analyser {
    private HashMap<String, LinkedList<LinkedList>> classMap;
    private LinkedList<LinkedList> tuple;
    // LinkedHashMap to preserve order of insertion
    private LinkedHashMap<Float[], LinkedList<LinkedList>> attrSorted;
    private LinkedList attrVal;

    public Analyser(LinkedList<LinkedList> tuple, HashMap classMap) {
        this.tuple = new LinkedList<>();
        this.tuple = tuple;
        this.classMap = new HashMap<>(classMap);

//        this.classMap=(HashMap)classMap.clone();
        maxMin(this.classMap);
    }

    private void maxMin(HashMap<String, LinkedList<LinkedList>> map) {

        map.keySet().forEach(k -> {
            String classTag = k;
            // keep track of attribute quantity
            // to be put in report()
            int attrCount = 0;

            // sorted attr list for EACH class
            attrSorted = new LinkedHashMap<>();

            // for each attribute
            // starts at 1 end at size - 1
            for (int i = 1; i < map.get(k).get(0).size() - 1; i++) {
                int order = i;
                attrVal = new LinkedList<Float>();
                map.get(k).stream().forEach(t -> {
                    attrVal.add(t.get(order));
                });

                System.out.println("Class " + k + " : " + "List of attr number " + i + " : " + attrVal);
                attrCount++;

                // use sort() to get max and min value of range
                Collections.sort(attrVal);
                Float[] attrRange = new Float[2];
                attrRange[0] = (Float) attrVal.get(0);
                attrRange[1] = (Float) attrVal.get(attrVal.size() - 1);
                System.out.println("Range of attr number " + i + " : " + attrRange[0] + " " + attrRange[1]);

                // find tuples that are in range
                // from general tuple list not hashmap tuple
                LinkedList tupleAList = new LinkedList<LinkedList>();
                tuple.stream().forEach(t -> {
                    if (attrRange[0] <= (Float) t.get(order) && (Float) t.get(order) <= attrRange[1]) {
                        tupleAList.add(t);
                    }
                });

                // include class tag with tuple list NOT RELEVANT
                // tupleAList.add(0, k);

                // append into hashmap!
                attrSorted.put(attrRange, tupleAList);
            }
            System.out.println("Attr count : " + attrCount);
            maxSum(classTag, attrSorted);
        });
    }

    private void maxSum(String classTag, LinkedHashMap<Float[], LinkedList<LinkedList>> attrMap) {
        /***
         * DOES NOT WORK YET
         */
        System.out.println("---------------------------------");
        System.out.println("For class " + classTag);

        // 1 or -1 transform of class in each tuple
        attrMap.keySet().stream().forEach(cr -> {
            attrMap.get(cr).stream().forEach(tuple -> {
                System.out.println(tuple.get(tuple.size() -1));
                if (tuple.get(tuple.size() - 1).equals(classTag)) {
                    tuple.set(tuple.size() - 1, 1);
                } else {
                    tuple.set(tuple.size() - 1, -1);
                }
            });

            // append 1 and -1 into an array for max sum solution
            LinkedList biTuple = new LinkedList<Integer>();
            attrMap.get(cr).stream().forEach(tuple -> {
                biTuple.add(tuple.get(tuple.size() -1));
            });
            System.out.println(biTuple);
        });




    }

//            System.out.print("For class: " + attrMap.get(k).get(0));
//            System.out.println(" range is : " + k[0] + " " + k[1]);
//
//            // cannot use stream() since starting point = 1 (ignore class tag)
//            for (int i = 1; i < attrMap.get(k).size(); i++) {
//                String classTag = (String) attrMap.get(k).get(i).get(attrMap.get(k).get(i).size() - 1);
//                if (classTag.equals(k)) {
//                    attrMap.get(k).get(i).set(attrMap.get(k).get(i).size() - 1, "1");
//                } else {
//                    attrMap.get(k).get(i).set(attrMap.get(k).get(i).size() - 1, "-1");
//                }
//            }
//            System.out.println("Class " + attrMap.get(k).get(0) + " tuple after fix " + attrMap.get(k));
//        });


//                tupleList.stream().forEach(tuple -> {
//                    if (tuple.get(tuple.size() - 1).toString().equals(k)) {
//                        tuple.set(tuple.size() - 1, 1);
//                    } else {
//                        tuple.set(tuple.size() - 1, -1);
//                    }
//                });
//            });
//
//            System.out.println("Tuple post-fix: " + attrMap.get(k));

}

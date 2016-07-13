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
    private LinkedHashMap<Integer, Float[]> LR;

    public Analyser(LinkedList<LinkedList> tuple, HashMap classMap) {
        this.tuple = new LinkedList<>();
        this.tuple = tuple;
        this.classMap = new HashMap<>(classMap);

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
                System.out.println("------------------------");
                System.out.println("For class " + k + " : " + "List of attr number " + i + " : " + attrVal);
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

                System.out.println("TupleAList in hashMap: " + tupleAList);

                // append into hashmap!
                attrSorted.put(attrRange, tupleAList);
            }
            System.out.println("Attr count : " + attrCount);
            // create long range holder
            LinkedHashMap LR = new LinkedHashMap<>();
            biConvert(classTag, attrSorted, LR);
            System.out.println("Done 1 class - sending to generator\n");
            // LR as param
            // CALL GENERATOR INSTANCE HERE
        });
    }

    private void biConvert(String classTag, LinkedHashMap<Float[], LinkedList<LinkedList>> attrMap, LinkedHashMap<Integer, Float[]> LR) {
        System.out.println("---------------------------------");
        System.out.println("For class " + classTag);
        // 1 or -1 transform of class in each tuple

        // create a clone hashmap to avoid editing the original version
        LinkedHashMap<Float[], LinkedList<LinkedList>> tempMap = new LinkedHashMap<>(attrMap);


        List keys = new ArrayList(tempMap.keySet());
        for (int i = 0; i < keys.size(); i++) {
            int attrNo = i;

            // to control which attribute number it is (using i)
            Object keyObj = keys.get(i);


            Comparator<LinkedList> comp = (a, b)-> ((Float) a.get(attrNo+1)).compareTo((Float) b.get(attrNo+1));
            tempMap.get(keyObj).sort(comp);
            tempMap.put((Float[]) keyObj, tempMap.get(keyObj));

            System.out.println("Sorted map by attr no. : " + (attrNo+1) + "||| "  + tempMap.get(keyObj));

            // create a temp binary list
            LinkedList biTuple = new LinkedList<Integer>();
            tempMap.get(keyObj).stream().forEach(tuple -> {
                // append 1 and -1 into an array for max sum solution
                if (tuple.get(tuple.size() - 1).equals(classTag)) {
                    biTuple.add(1);
                } else {
                    biTuple.add(-1);
                }
            });

            System.out.println("Converted binary list: " + biTuple);
            maxSum(keyObj, tempMap, attrNo, biTuple, LR);
        };
    }

    /**
     * Altered max sum with iteration and additional decremental limit (NOT YET APPLIED)
     * Original method from Kadane's algorithm
     * @param biList a list of 1 or -1 for max sum calculation
     */
    private void maxSum(Object key, LinkedHashMap<Float[], LinkedList<LinkedList>> map, int attr, LinkedList<Integer> biList, LinkedHashMap<Integer, Float[]> LR) {
        System.out.println("Size of biList: " + biList.size());
        int curMax = -1;
        int finMax = -1;
        int posCurStart = 0;
        int[] posTracker = {0, posCurStart};

        for (int i = 0; i < biList.size() - 1; i++) {
            curMax+= biList.get(i);
            if (curMax > finMax) {
                finMax = curMax;
                posTracker[0] = posCurStart;
                posTracker[1] = i;
            } else if (curMax < -1) {
                curMax = -1;
                posCurStart = i+1;
            }
        }
        System.out.println("Max Sum is : " + finMax);
        System.out.println("Positions of array: " + posTracker[0] + " " + posTracker[1]);
        addCRange(key, map, attr, posTracker, biList, LR);
    }

    private void addCRange(Object key, LinkedHashMap<Float[], LinkedList<LinkedList>> map, int attr, int[] indRange, LinkedList<Integer> biList, LinkedHashMap<Integer, Float[]> LR) {
        // after maxsum called
        Float[] cRange = new Float[2];
        cRange[0] = (Float) map.get(key).get(indRange[0]).get(attr+1);
        cRange[1] = (Float) map.get(key).get(indRange[1]).get(attr+1);

        System.out.println("Range after maxsum: " + cRange[0] + " " + cRange[1]);
        minThresh(biList, cRange, indRange, LR, attr);
    }

    /***
     * Check for support & confidence (then density)
     *
     */
    private void minThresh(LinkedList<Integer> biList, Float[] cRange, int[] indRange, LinkedHashMap<Integer, Float[]> LR, int attr) {
        float support =(float) (indRange[1] - indRange[0]) / biList.size();
        System.out.println("Support: " + support);

        System.out.println("Confidence: " + 1 + " ?" + "\n-------------------");

        // suppose support > 60%
        // NEED CHECK FOR DENSITY
        LR.put(attr, cRange);
    }



}
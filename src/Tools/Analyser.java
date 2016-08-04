package Tools;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by phuongt994 on 28/06/2016.
 */
public class Analyser {
    private HashMap<String, LinkedList<LinkedList>> classMap;
    private LinkedList<LinkedList> tuple;
    // LinkedHashMap to preserve order of insertion
    private LinkedHashMap<Float[], LinkedList<LinkedList>> attrSorted;
    private LinkedList attrVal;
    private LinkedHashMap<Integer, LinkedList<Float[]>> LR;

    public Analyser(LinkedList<LinkedList> tuple, HashMap classMap) {
        this.tuple = new LinkedList<>();
        this.tuple = tuple;
        this.classMap = new HashMap<>(classMap);
        maxMin(this.classMap);
    }

    private void maxMin(HashMap<String, LinkedList<LinkedList>> map) {

        map.keySet().forEach(k -> {
            String classTag = k;
            int attrCount = 0;

            System.out.println("------------------------");
            System.out.println("For class " + classTag);

            attrSorted = new LinkedHashMap<>();

            // for each attribute
            for (int i = 1; i < map.get(k).get(0).size() -1; i++) {
                int order = i;
                attrVal = new LinkedList<Float>();
                map.get(k).stream().forEach(t -> {
                    attrVal.add(t.get(order));
                });
                System.out.println("List of attr number " + i + " : " + attrVal);
                attrCount++;

                Collections.sort(attrVal);
                Float[] attrRange = new Float[2];
                attrRange[0] = (Float) attrVal.getFirst();
                attrRange[1] = (Float) attrVal.getLast();
                System.out.println("Range of attr number " + i + " : " + attrRange[0] + " " + attrRange[1]);

                LinkedList tupleAList = new LinkedList<LinkedList>();
                tuple.stream().forEach(t -> {
                    if (attrRange[0] <= (Float) t.get(order) && (Float) t.get(order) <= attrRange[1]) {
                        tupleAList.add(t);
                    }
                });

                System.out.println("TupleAList in hashMap: " + tupleAList);
                attrSorted.put(attrRange, tupleAList);

            }
            System.out.println("Attr count : " + attrCount);
            LinkedHashMap LR = new LinkedHashMap<Integer, LinkedList<Float[]>>();
            LinkedList CR = new LinkedList<LinkedList<Float>>();
            biConvert(classTag, attrSorted, CR);

            System.out.println("Done 1 class - sending to generator\n");
            System.out.println("All entries ");
            for (int i = 0; i < CR.size(); i++) {
                System.out.println(((LinkedList<Float>) CR.get(i)).toString());
            }

            new Generator(classTag, CR);
        });
    }

    private void biConvert(String classTag, LinkedHashMap<Float[], LinkedList<LinkedList>> attrMap, LinkedList<LinkedList<Float>> CR) {
        System.out.println("\nbiConvert() for class " + classTag);

        // create a clone hashmap
        LinkedHashMap<Float[], LinkedList<LinkedList>> tempMap = new LinkedHashMap<>(attrMap);

        int order = 0;
        for (Object key : tempMap.keySet()) {
            ++order;
            int attrNo = order;
            Comparator<LinkedList> comp = (a, b)-> ((Float) a.get(attrNo)).compareTo((Float) b.get(attrNo));
            tempMap.get(key).sort(comp);
            System.out.println("Sorted map by attr no. : " + (attrNo) + " || "  + tempMap.get(key));

            // create a temp binary list
            LinkedList biTuple = new LinkedList<Integer>();
            tempMap.get(key).stream().forEach(tuple -> {
                // append 1 and -1 into an array for max sum solution
                if (tuple.get(tuple.size() - 1).equals(classTag)) {
                    biTuple.add(1);
                } else {
                    biTuple.add(-1);
                }
            });

            System.out.println("Converted binary list: " + biTuple);
            maxSum(key, tempMap, attrNo, biTuple, CR);
        };
    }

    /**
     * Altered max sum with iteration and additional decremental limit (NOT YET APPLIED)
     * Original method from Kadane's algorithm
     * @param biList a list of 1 or -1 for max sum calculation
     */
    private void maxSum(Object key, LinkedHashMap<Float[], LinkedList<LinkedList>> map, int attr, LinkedList<Integer> biList, LinkedList<LinkedList<Float>> CR) {
        System.out.println("Size of biList: " + biList.size());
        int curMax = 0;
        int[] curPos = {0, 0};
        int startInd = 0;
        boolean prevPos = false;
        LinkedList<int[]> allPos = new LinkedList<>();

        for (int i = 0; i < biList.size(); i++) {
            curMax += biList.get(i);
            if (curMax < 0) {
                //reset
                if (prevPos == true) {
                    allPos.add(curPos.clone());
                }
                startInd = i+1;
                curMax = 0;
                prevPos = false;

            //} else if (i == biList.size() -1) {
            //    allPos.add(curPos);
            //    break;
            } else {
                curPos[0] = startInd;
                curPos[1] = i;
                prevPos = true;
            }
        }

        //allPos = allPos.stream().distinct().collect(Collectors.toCollection(LinkedList<int[]>::new));

        System.out.println("All positions: ");
        allPos.stream().forEach(p -> {
            System.out.println(Arrays.toString(p));
        });

        minThresh(key, map, attr, allPos, biList, CR);
    }

    /***
     * Check for support & confidence (then density)
     *
     */
    private void minThresh(Object key, LinkedHashMap<Float[], LinkedList<LinkedList>> map, int attr, LinkedList<int[]> allRange, LinkedList<Integer> biList, LinkedList<LinkedList<Float>> CR) {
        LinkedList list = new LinkedList<Integer>();

        for (int[] range : allRange) {
            // eliminate 0,0
            float support = (float) (range[1] - range[0]) / biList.size();

            // for confidence
            for (int i = 0; i < range[1] - range[0]; i++) {
                list.add(biList.get(i));
            }

            int pos = 0;
            for (int j = 0; j < list.size(); j++) {
                if ((int) list.get(j) == 1) {
                    pos++;
                }
            }
            float confidence = (float) pos / list.size();

            System.out.println("Support: " + support);
            System.out.println("Confidence: " + confidence);

            if (support <  0.0) { // NO REJECTION
                System.out.println("Rejected");
            } else {
                LinkedList<Float> aRange = new LinkedList<>();
                aRange.add((Float) map.get(key).get(range[0]).get(attr));
                aRange.add((Float) map.get(key).get(range[1]).get(attr));
                aRange.addLast((float) attr);
                System.out.println("Converted aRange: " + Arrays.asList(aRange));
                CR.add(aRange);
            }
        }

        CR = (LinkedList<LinkedList<Float>>) CR.stream().distinct().collect(Collectors.toCollection(LinkedList<LinkedList<Float>>::new));
        if (CR.isEmpty()) {
            System.out.println("No ranges found.");
        } else {
            System.out.println("cRange for attr no " + attr + " is ready");
        }
    }
}
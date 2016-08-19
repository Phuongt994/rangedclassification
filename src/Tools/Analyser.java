package Tools;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by phuongt994 on 28/06/2016.
 */
public class Analyser {
	/***
	 * allTuple : map classified by class
	 * allTuple : all tuple 
	 * allAttributeMap : attribute-sorted map 
	 * attributeValue : attribute values
	 * LR : ??
	 */
    private HashMap<String, LinkedList<LinkedList>> allClassMap;
    private LinkedList<LinkedList> allTuple;
    private LinkedHashMap<Float[], LinkedList<LinkedList>> allAttributeMap;
    private LinkedList<Float> attributeValue;
    private LinkedHashMap<Integer, LinkedList<Float[]>> LR;

    public Analyser(LinkedList<LinkedList> allTuple, HashMap allClassMap) {
        this.allTuple = new LinkedList<>();
        this.allTuple = allTuple;
        this.allClassMap = new HashMap<>(allClassMap);
        maxMin(this.allClassMap);
    }
    
    private void maxMin(HashMap<String, LinkedList<LinkedList>> allClassMap) {
        allClassMap.keySet().forEach(k -> {
            String classTag = k;
            int attributeCount = 0;

            System.out.println("------------------------");
            System.out.println("For class " + classTag);

            allAttributeMap = new LinkedHashMap<>();

            // for each attribute
            for (int i = 1; i < allClassMap.get(k).get(0).size() - 1; i++) {
            	int attributeNumber = i;
                attributeValue = new LinkedList<>();
                allClassMap.get(k).stream().forEach(t -> {
                    attributeValue.add((Float) t.get(attributeNumber));
                });
                // System.out.println("List of attr number " + attributeNumber + " : " + attributeValue);
                attributeCount++;

                Collections.sort(attributeValue);
                Float[] attributeRange = new Float[2];
                attributeRange[0] = attributeValue.getFirst();
                attributeRange[1] = attributeValue.getLast();
                System.out.println("Range of attr number " + attributeNumber + " : " + attributeRange[0] + " " + attributeRange[1]);

                LinkedList attributeRangedTuple = new LinkedList<LinkedList>();
                allTuple.stream().forEach(t -> {
                    if (attributeRange[0] <= (Float) t.get(attributeNumber) && (Float) t.get(attributeNumber) <= attributeRange[1]) {
                        attributeRangedTuple.add(t);
                    }
                });

                // System.out.println("Attribute-range sorted tuples for attr no " + i + " is : " + attributeRangedTuple);
                // append to attribute map aM
                allAttributeMap.put(attributeRange, attributeRangedTuple);

            }
            System.out.println("Attr count : " + attributeCount);
            // System.out.println("Attr map values: " + allAttributeMap.entrySet());
            
            /***
             * LR? CR?
             */
            LinkedHashMap LR = new LinkedHashMap<Integer, LinkedList<Float[]>>();
            // LinkedList CR = new LinkedList<LinkedList<Float>>();
            binaryConvert(classTag, allAttributeMap, LR);

            System.out.println("Done 1 class - sending to generator\n");
            new Generator(classTag, LR);
        });
    }

    private void binaryConvert(String classTag, LinkedHashMap<Float[], LinkedList<LinkedList>> allAttributeMap, LinkedHashMap<Integer, LinkedList<Float[]>> LR) {
        System.out.println("\nbiConvert() for class " + classTag);

        // create a clone hashmap
        LinkedHashMap<Float[], LinkedList<LinkedList>> tempAttributeMap = new LinkedHashMap<>(allAttributeMap);

        int order = 0;
        for (Object key : tempAttributeMap.keySet()) {
            ++order;
            int attributeNumber = order;
            Comparator<LinkedList> comp = (a, b)-> ((Float) a.get(attributeNumber)).compareTo((Float) b.get(attributeNumber));
            tempAttributeMap.get(key).sort(comp);
            // System.out.println("Sorted map by attr no. : " + (attributeNumber) + " || "  + tempAttributeMap.get(key));

            // create a temp binary list
            LinkedList<Integer> binaryList = new LinkedList<>();
            tempAttributeMap.get(key).stream().forEach(tuple -> {
                // append 1 and -1 into an array for max sum solution
                if (tuple.getLast().equals(classTag)) {
                    binaryList.add(1);
                } else {
                    binaryList.add(-1);
                }
            });

            System.out.println("Converted binary list for attribute no. " + attributeNumber + ": " + binaryList);
            maxSum(key, tempAttributeMap, attributeNumber, binaryList, LR);
        };
    }

    /**
     * Original method from Kadane's algorithm
     * @param biList a list of 1 or -1 for max sum calculation
     */
    private void maxSum(Object key, LinkedHashMap<Float[], LinkedList<LinkedList>> tempAttributeMap, int attributeNumber, LinkedList<Integer> binaryList, LinkedHashMap<Integer, LinkedList<Float[]>> LR) {
        // System.out.println("Size of biList: " + binaryList.size() + " for attr number " + attributeNumber);
        int currentMax = 0;
        int[] currentPosition = {0, 0};
        boolean prevSumIsPositive = false;
        LinkedList<int[]> allPosition = new LinkedList<>();

        for (int i = 0; i < binaryList.size(); i++) {
            currentMax += binaryList.get(i);
            if (currentMax >= 0) {
            	currentPosition[1] = i;
            	prevSumIsPositive = true;
            } else {
            	if (prevSumIsPositive == true) {
            		currentPosition[1] = i;
            		boolean checker = checkThresh(currentPosition.clone(), binaryList);
                	if (checker == true) {
                		allPosition.add(currentPosition.clone());
                	}
            	}
            	currentPosition[0] = i+1;
            	currentPosition[1] = i+1;
            	currentMax = 0;
            	prevSumIsPositive = false;
            }
        }
        //allPos = allPos.stream().distinct().collect(Collectors.toCollection(LinkedList<int[]>::new));

        System.out.println("All positions for attribute number " + attributeNumber + " is :");
        // transform it back to float ranges
        LinkedList<Float[]> CR = new LinkedList<>();
        allPosition.stream().forEach(p -> {
            System.out.println(Arrays.toString(p));
            Float[] aFloatRange = new Float[2];
            aFloatRange[0] = (Float) tempAttributeMap.get(key).get(p[0]).get(attributeNumber);
            aFloatRange[1] = (Float) tempAttributeMap.get(key).get(p[1]).get(attributeNumber);
            CR.add(aFloatRange);
        }); 
        LR.put(attributeNumber, CR);
    }
    /***
     * Check for threshold within maxsum
     */
    private boolean checkThresh(int[] currentPosition, LinkedList<Integer> binaryList) {
    	// for support
    	float support = (float) (currentPosition[1] - currentPosition[0]) / binaryList.size();
    	
    	// for confidence
    	LinkedList tempList = new LinkedList<Integer>();
    	for (int i = 0; i < currentPosition[1] - currentPosition[0]; i++) {
            tempList.add(binaryList.get(i));
        }

        int positiveCount = 0;
        for (int j = 0; j < tempList.size(); j++) {
            if ((int) tempList.get(j) == 1) {
                positiveCount++;
            }
        }
        float confidence = (float) positiveCount / tempList.size();
        
        // conditions
        // support & confidence current accepted as positive (no rejects)
        if (support > 0.1) { 
        	if (confidence > 0.3) {
        		System.out.println("Range " + Arrays.toString(currentPosition) + " is accepted");
        		System.out.println("Support: " + support);
                System.out.println("Confidence: " + confidence);
        		return true;
        	} else {
        		return false;
        	}
        } else {
        	return false;
        }
    }
   
    private void getAllAttributeMap() {
    	
    }
}
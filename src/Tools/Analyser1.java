package Tools;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by phuongt994 on 28/06/2016.
 */
public class Analyser1 {
	
    private HashMap<String, LinkedList<LinkedList>> allClassMap;
    private LinkedList<LinkedList> allTuple;
    private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap;
    private LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap;
    private LinkedList<LinkedList<Float[]>> attributeRangeList;

    public Analyser1(LinkedList<LinkedList> allTuple, HashMap<String, LinkedList<LinkedList>> allClassMap) {
        this.allTuple = allTuple;
        this.allClassMap = allClassMap;
    }
    
    protected void maxMin(HashMap<String, LinkedList<LinkedList>> allClassMap) {
    	
    	this.allClassMap = allClassMap;
        this.allClassMap.keySet().forEach(k -> {
            String classTag = k;

            System.out.println("------------------------");
            System.out.println("For class " + classTag);
            
            attributeRangeMap = new LinkedHashMap<>();
            attributeTupleMap = new LinkedHashMap<>();

            // for each attribute
            for (int i = 1; i < allClassMap.get(k).get(0).size() - 1; i++) {
            	
            	LinkedList<Integer> attributeNumberList = new LinkedList<>();
            	int attributeNumber = i;
            	attributeNumberList.add(attributeNumber);
            	
                LinkedList<Float> attributeValue = new LinkedList<>();
                allClassMap.get(k).stream().forEach(t -> {
                    attributeValue.add((Float) t.get(attributeNumber));
                });

                Collections.sort(attributeValue);
                
                Float[] attributeRange = new Float[2];
                attributeRange[0] = attributeValue.getFirst();
                attributeRange[1] = attributeValue.getLast();
                System.out.println("Range of attr number " + attributeNumber + " : " + attributeRange[0] + " " + attributeRange[1]);
                LinkedList<Float[]> attributeRangeList = new LinkedList<>();
                attributeRangeList.add(attributeRange);
                

                LinkedList attributeRangeTupleList = new LinkedList<LinkedList>();
                allTuple.stream().forEach(t -> {
                    if (attributeRange[0] <= (Float) t.get(attributeNumber) && (Float) t.get(attributeNumber) <= attributeRange[1]) {
                        attributeRangeTupleList.add(t);
                    }
                });
                
                Comparator<LinkedList> comp = (a, b)-> ((Float) a.get(attributeNumber)).compareTo((Float) b.get(attributeNumber));
				attributeRangeTupleList.sort(comp);
				
                // append attributeRangeMap
				// convert rangelist to rangelist-list to fit class type
                LinkedList<LinkedList<Float[]>> attributeRangeList2 = new LinkedList<>();
                attributeRangeList2.add(attributeRangeList);
                attributeRangeMap.put(attributeNumberList, attributeRangeList2);
                // append attributeTupleMap
                attributeTupleMap.put(attributeRangeList2, attributeRangeTupleList);
            }
            
            System.out.println("attributeRangeMap" + attributeRangeMap.entrySet());
            System.out.println("attributeTupleMap" + attributeTupleMap.entrySet());
            
        
            System.out.println("Class " + classTag + " now sent to binaryConvert()");
            binaryConvert(classTag, attributeRangeMap, attributeTupleMap);
            
            System.out.println("attributeRangeMap" + attributeRangeMap.entrySet());
            System.out.println("attributeTupleMap" + attributeTupleMap.entrySet());
            System.out.println("Done 1 class - sending to generator\n");
            
            new Generator(classTag, attributeRangeMap, attributeTupleMap);
        });
    }
    
    /***
     * Do we need RangeMap?
     * This only has effects in 1st round
     * @param attributeRangeMap
     * @param attributeTupleMap
     */
    protected void binaryConvert(String classTag, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {

    	for (Object key : attributeRangeMap.keySet()) {

    		LinkedList<Integer> attributeNumberList = (LinkedList<Integer>) key;

    		System.out.println("For attribute " + attributeNumberList);

    		// for each range of attribute

    		LinkedList<LinkedList<Float[]>> rangeList = new LinkedList<>(attributeRangeMap.get(key));

    		// create a temp binary list
    		LinkedList<Integer> binaryList = new LinkedList<>();
    		attributeTupleMap.get(rangeList).stream().forEach(tuple -> {
    			// append 1 and -1 into an array for max sum solution
    			if (tuple.getLast().equals(classTag)) {
    				binaryList.add(1);
    			} else {
    				binaryList.add(-1);
    			}
    		});

    		maxSum(attributeNumberList, binaryList, attributeRangeMap, attributeTupleMap);

    	}
    }

    /**
     * Original method from Kadane's algorithm
     * @param biList a list of 1 or -1 for max sum calculation
     */
    protected void maxSum(LinkedList<Integer> attributeNumberList, LinkedList<Integer> binaryList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
    	System.out.println("attributeNumberList " + attributeNumberList);
    	System.out.println("binaryList" + binaryList);
    	
        int currentMax = 0;
        int[] currentPosition = {0, 0};
        boolean prevSumIsPositive = false;
        LinkedList<int[]> attributePosition = new LinkedList<>();

        for (int i = 0; i < binaryList.size(); i++) {
            currentMax += binaryList.get(i);
            if (currentMax >= 0) {
            	currentPosition[1] = i;
            	prevSumIsPositive = true;
            	if (i == binaryList.size() -1) {
            		attributePosition.add(currentPosition.clone());
            	}
            } else {
            	if (prevSumIsPositive == true) {
            		currentPosition[1] = i;
            		attributePosition.add(currentPosition.clone());
            	}
            	currentPosition[0] = i+1;
            	currentPosition[1] = i+1;
            	currentMax = 0;
            	prevSumIsPositive = false;
           }
        }
        
        System.out.println("allPosition");
        for (int[] position : attributePosition) {
        	System.out.println("A position: " + Arrays.toString(position));
        }     
        
        // call checker
        checkThreshold(attributePosition, attributeNumberList, binaryList, attributeRangeMap, attributeTupleMap);
    }
    

    protected void checkThreshold(LinkedList<int[]> attributePosition, LinkedList<Integer> attributeNumberList, LinkedList<Integer> binaryList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
    	System.out.println("Checking all positions collected");
    	LinkedList<int[]> attributeCheckedPosition = new LinkedList<>();
    	
    	for (int[] currentPosition : attributePosition) {
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
	        // support & confidence check
	        if (support > 0.5 && confidence > 0.5) { 
	        	System.out.println("Range " + Arrays.toString(currentPosition) + " is accepted");
	        	System.out.println("Support: " + support);
	        	System.out.println("Confidence: " + confidence);
	        	attributeCheckedPosition.add(currentPosition);
	        } else {
	        	System.out.println("Range " + Arrays.toString(currentPosition) + " is rejected");
	        }
    	}
    	
    	// call converter
    	convertPositionToRange(attributeCheckedPosition, attributeNumberList, attributeRangeMap, attributeTupleMap);
    }
    
    private void convertPositionToRange(LinkedList<int[]> attributeCheckedPosition,LinkedList<Integer> attributeNumberList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
    	
    	attributeRangeList = new LinkedList<>();
    	 
    	System.out.println("All positions for attribute number after check is :");
    	for (int[] position : attributeCheckedPosition) {
    		System.out.println("A position: " + Arrays.toString(position));

    		// convert position to numerical equivalence
    		// convert values
    		// get key of rangeMap to get its value
    		// use this value as key to tupleMap to retrieve its value
    		// use retrieved tuple value to convert position to range

    		LinkedList<LinkedList<Float[]>> convertedMapKey = attributeRangeMap.get(attributeNumberList);
    		
    		LinkedList<Float[]> rangeList = new LinkedList<>();
    		for (Integer attributeNumber : attributeNumberList) {
    			Float[] range = new Float[2];
    			range[0] = (Float) attributeTupleMap.get(convertedMapKey).get(position[0]).get(attributeNumber);
    			range[1] = (Float) attributeTupleMap.get(convertedMapKey).get(position[1]).get(attributeNumber);
    			System.out.println("A position (range converted) " + Arrays.toString(range));
    			rangeList.add(range);
    		}
    		attributeRangeList.add(rangeList);
    	}
    	
		System.out.println("New key: " + attributeNumberList + " & new range : " + attributeRangeList);
		// for 1st iteration
		// call appendToMap
		if (attributeNumberList.size() <= 1) {
			appendToMap(attributeRangeList, attributeNumberList);
		}
    }
    
    private void appendToMap(LinkedList<LinkedList<Float[]>> attributeRangeList, LinkedList<Integer> attributeNumberList) {
    	
    	// append tuple map first 
    	// since we need key from old range map
    	
    	for (LinkedList<Float[]> rangeList : attributeRangeList) {
    		
    		LinkedList<LinkedList> attributeTupleList = new LinkedList<>();
    		
    		// convert key from range map
    		LinkedList<LinkedList<Float[]>> convertedMapKey = new LinkedList<>(attributeRangeMap.get(attributeNumberList));
    		
    		attributeTupleMap.get(convertedMapKey).stream().forEach(tuple -> {
    			Float[] range = rangeList.getFirst();
    			
    			if (range[0] <= (float) tuple.get(attributeNumberList.getFirst()) && (float) tuple.get(attributeNumberList.getFirst()) <= range[1]) {
    				attributeTupleList.add(tuple);
    			}
    		});
    		
    		// convert key for tuple map
    		LinkedList<LinkedList<Float[]>> attributeRangeList2 = new LinkedList<>();
    		attributeRangeList2.add(rangeList);
    		System.out.println("append new range to tuple map");
    		System.out.println("new key : " + attributeRangeList2);
    		attributeTupleMap.put(attributeRangeList2, attributeTupleList);
    	}

    	// append range map
    	attributeRangeMap.put(attributeNumberList, attributeRangeList);
    }
    
    protected LinkedList<LinkedList<Float[]>> getAttributeRangeList() {
    	return attributeRangeList;
    }
     
}
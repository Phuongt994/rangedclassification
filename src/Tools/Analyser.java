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
    private LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap;
    private LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap;

    public Analyser(LinkedList<LinkedList> allTuple, HashMap<String, LinkedList<LinkedList>> allClassMap) {
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
                
                // append attributeRangeMap
                attributeRangeMap.put(attributeNumberList, attributeRangeList);
                // append attributeTupleMap
                attributeTupleMap.put(attributeRangeList, attributeRangeTupleList);
            }
            
            System.out.println("For class 1: ");
            System.out.println("attributeRangeMap" + attributeRangeMap.entrySet());
            System.out.println("attributeTupleMap" + attributeTupleMap.entrySet());
            
        
            System.out.println("Class " + classTag + " now sent to binaryConvert()");
            binaryConvert(classTag, attributeRangeMap, attributeTupleMap);

            System.out.println("Done 1 class - sending to generator\n");
            System.out.println("attributeRangeMap" + attributeRangeMap.entrySet());
            System.out.println("attributeTupleMap" + attributeTupleMap.entrySet());
            // new Generator(classTag, attributeRangeMap, attributeTupleMap);
        });
    }
    
    /***
     * Do we need RangeMap?
     * This only has effects in 1st round
     * @param attributeRangeMap
     * @param attributeTupleMap
     */
    private void binaryConvert(String classTag, LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap, LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap) {
    	this.attributeRangeMap = attributeRangeMap;
    	this.attributeTupleMap = attributeTupleMap;
        for (Object key : this.attributeRangeMap.keySet()) {
        	
        	LinkedList<Integer> attributeNumberList = (LinkedList<Integer>) key;
        	int attributeNumber = attributeNumberList.getFirst();        	
        	
        	// for each range of attribute
        	attributeRangeMap.get(key).stream().forEach(range -> {
        		System.out.println("A range : " + Arrays.toString(range));
        		
        		// to dodge key map conflict
        		// still dont know why
        		LinkedList<Float[]> aRangeList = new LinkedList<>();
        		aRangeList.add(range);
   
            	// sort tuple by ascending order of its range
                Comparator<LinkedList> comp = (a, b)-> ((Float) a.get(attributeNumber)).compareTo((Float) b.get(attributeNumber));
                this.attributeTupleMap.get(aRangeList).sort(comp);

                // create a temp binary list
                LinkedList<Integer> binaryList = new LinkedList<>();
                this.attributeTupleMap.get(aRangeList).stream().forEach(tuple -> {
                    // append 1 and -1 into an array for max sum solution
                    if (tuple.getLast().equals(classTag)) {
                        binaryList.add(1);
                    } else {
                        binaryList.add(-1);
                    }
                });
                
                // call maxsum
                // return a list of positions           
                LinkedList<int[]> attributePosition = new LinkedList<>();
                attributePosition = maxSum(attributeNumberList, binaryList);
                
                // call thresholdCheck()
                // return a list of float ranges
                LinkedList<Float[]> attributeRangeList = new LinkedList<>();
                attributeRangeList = thresholdCheck(attributePosition, attributeNumberList, binaryList);
                
                // call appendToMap()
                // no return (yet?)
                appendToMap(attributeRangeList, attributeNumberList);
     
        	});
        }

    }

    /**
     * Original method from Kadane's algorithm
     * @param biList a list of 1 or -1 for max sum calculation
     */
    protected LinkedList<int[]> maxSum(LinkedList<Integer> attributeNumberList, LinkedList<Integer> binaryList) {

    	System.out.println("attributeNumberList" + attributeNumberList);
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
        
        return attributePosition;
    }
    

    protected LinkedList<Float[]> thresholdCheck(LinkedList<int[]> attributePosition, LinkedList<Integer> attributeNumberList, LinkedList<Integer> binaryList) {
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
	        if (support > 0.1 && confidence > 0.3) { 
	        	System.out.println("Range " + Arrays.toString(currentPosition) + " is accepted");
	        	System.out.println("Support: " + support);
	        	System.out.println("Confidence: " + confidence);
	        	attributeCheckedPosition.add(currentPosition);
	        } else {
	        	System.out.println("Range " + Arrays.toString(currentPosition) + " is rejected");
	        }
    	}
    	
    	LinkedList<Float[]> attributeRangeList = new LinkedList<>();
    	 
    	System.out.println("All positions for attribute number after check is :");
    	for (int[] position : attributeCheckedPosition) {
    		System.out.println("A position: " + Arrays.toString(position));

    		// convert position to numerical equivalence
    		// different for different iteration
    		if (attributeNumberList.size() == 1) {
    			// if 1st iteration
    			Float[] range = new Float[2];
    			
    			// convert values.. map conflict
    			LinkedList<Float[]> convertedMapKey = attributeRangeMap.get(attributeNumberList);
    			range[0] = (Float) attributeTupleMap.get(convertedMapKey).get(position[0]).get(attributeNumberList.get(0));
    			range[1] = (Float) attributeTupleMap.get(convertedMapKey).get(position[1]).get(attributeNumberList.get(0));
    			System.out.println("A position (range converted) " + Arrays.toString(range));
    			attributeRangeList.add(range);
    		} else {
    			// 2nd iteration onwards
    			// undecided
    		}
    	}
    	return attributeRangeList;
    }
    
    private void appendToMap(LinkedList<Float[]> attributeRangeList, LinkedList<Integer> attributeNumberList) {
    	// for first iteration
    	if (attributeNumberList.size() == 1) {
    		
	    	// append tuple map
	    	int attributeNumber = attributeNumberList.get(0);
	    	for (Float[] range : attributeRangeList) {
	    		LinkedList<Float[]> rangeList = new LinkedList<>();
	    		LinkedList<LinkedList> attributeTupleList = new LinkedList<>();
	    		
	    		// convert key
	    		LinkedList<Float[]> convertedKey = new LinkedList<>();
	    		convertedKey = attributeRangeMap.get(attributeNumberList);
	    		
	    		attributeTupleMap.get(convertedKey).stream().forEach(tuple -> {
	    			if (range[0] <= (float) tuple.get(attributeNumber) && (float) tuple.get(attributeNumber) <= range[1]) {
	    				attributeTupleList.add(tuple);
	    			}
	    		});
	    		attributeTupleMap.put(rangeList, attributeTupleList);
	    	}
	    	
	    	// append range map
	    	attributeRangeMap.put(attributeNumberList, attributeRangeList);
	    	
    	} else {
    		//for 2nd iteration onwards
    		// undecided
    	}
    	
    }
    
}
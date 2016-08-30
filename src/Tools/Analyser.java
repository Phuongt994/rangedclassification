package Tools;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by phuongt994 on 28/06/2016.
 */
public class Analyser {
	
    private HashMap<String, LinkedList<LinkedList>> allClassMap;
    private LinkedList<LinkedList> allTuple;
    private LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap;
    private LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap;
    private LinkedList<Float[]> attributeRangeList;

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
            new Generator(classTag, attributeRangeMap, attributeTupleMap);
        });
    }
    
    /***
     * Do we need RangeMap?
     * This only has effects in 1st round
     * @param attributeRangeMap
     * @param attributeTupleMap
     */
    protected void binaryConvert(String classTag, LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap, LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap) {
    	
    	if (attributeRangeMap.keySet().size() > 1) {
    	// for 1st iteration

    		for (Object key : attributeRangeMap.keySet()) {

    			LinkedList<Integer> attributeNumberList = (LinkedList<Integer>) key;
    			Integer attributeNumber = attributeNumberList.getFirst();        	
    			System.out.println("For attribute number " + attributeNumber);
    			// for each range of attribute
    			System.out.println((attributeRangeMap.get(key)).size());

    			attributeRangeMap.get(key).stream().forEach(range -> {
    				System.out.println("A range : " + Arrays.toString(range));

    				// to dodge key map conflict
    				LinkedList<Float[]> aRangeList = new LinkedList<>();
    				aRangeList.add(range);

    				// sort tuple by ascending order of its range
    				Comparator<LinkedList> comp = (a, b)-> ((Float) a.get(attributeNumber)).compareTo((Float) b.get(attributeNumber));
    				attributeTupleMap.get(aRangeList).sort(comp);

    				// create a temp binary list
    				LinkedList<Integer> binaryList = new LinkedList<>();
    				attributeTupleMap.get(aRangeList).stream().forEach(tuple -> {
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
    				attributePosition = maxSum(attributeNumberList, binaryList, attributeRangeMap, attributeTupleMap);

    				// call thresholdCheck()
    				// return a list of float ranges
    				LinkedList<Float[]> attributeRangeList = new LinkedList<>();
    				attributeRangeList = thresholdCheck(attributePosition, attributeNumberList, binaryList, attributeRangeMap, attributeTupleMap);

    				// call appendToMap()
    				// no return (yet?)
    				appendToMap(attributeRangeList, attributeNumberList);
    			});
    		}

    	} else {
    	// for 2nd+ iteration
    		// get the (one) combo key
	        Object key = attributeRangeMap.keySet().toArray()[0];
	        
	        // get the (one) combo range
	        LinkedList<Float[]> aCombinedRange = new LinkedList<>(attributeRangeMap.get(key));
	        
	        System.out.println("A range : " + aCombinedRange);
	        
	        LinkedList<Integer> binaryList = new LinkedList<>();
	        attributeTupleMap.get(aCombinedRange).stream().forEach(tuple -> {
	        	if (tuple.getLast().equals(classTag)) {
	        		binaryList.add(1);
	        	} else {
	        		binaryList.add(-1);
	        	}
	        });
	        
	        // WHAT CAN BE DONE FOR NEXT ITERATION?
	        
    		// call maxsum
    		// return a list of positions           
    		LinkedList<int[]> attributePosition = new LinkedList<>();
    		attributePosition = maxSum((LinkedList<Integer>) key, binaryList, attributeRangeMap, attributeTupleMap);

    		// call thresholdCheck()
    		// return a list of float ranges
    		LinkedList<Float[]> attributeRangeList = new LinkedList<>();
    		attributeRangeList = thresholdCheck(attributePosition, (LinkedList<Integer>) key, binaryList, attributeRangeMap, attributeTupleMap);

    		// NO append to map in 2nd+ iteration
    		// appendToMap(attributeRangeList, (LinkedList<Integer>) key);
    		
    	};
    }

    /**
     * Original method from Kadane's algorithm
     * @param biList a list of 1 or -1 for max sum calculation
     */
    protected LinkedList<int[]> maxSum(LinkedList<Integer> attributeNumberList, LinkedList<Integer> binaryList, LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap, LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap) {
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
        return attributePosition;
    }
    

    protected LinkedList<Float[]> thresholdCheck(LinkedList<int[]> attributePosition, LinkedList<Integer> attributeNumberList, LinkedList<Integer> binaryList, LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap, LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap) {
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
    	
    	attributeRangeList = new LinkedList<>();
    	 
    	System.out.println("All positions for attribute number after check is :");
    	for (int[] position : attributeCheckedPosition) {
    		System.out.println("A position: " + Arrays.toString(position));

    		// convert position to numerical equivalence
    		// different for different iteration
    		if (attributeNumberList.size() == 1) {
    			// if 1st iteration
    			int attributeNumber = attributeNumberList.getFirst();
    			Float[] range = new Float[2];
    			
    			// convert values.. map conflict
    			LinkedList<Float[]> convertedMapKey = attributeRangeMap.get(attributeNumberList);
    			range[0] = (Float) attributeTupleMap.get(convertedMapKey).get(position[0]).get(attributeNumber);
    			range[1] = (Float) attributeTupleMap.get(convertedMapKey).get(position[1]).get(attributeNumber);
    			System.out.println("A position (range converted) " + Arrays.toString(range));
    			attributeRangeList.add(range);
    		} else {
    			// for 2nd+ iteration
    			// get mutual key then separate it into individual key
    			LinkedList<Float[]> convertedMapKey = attributeRangeMap.get(attributeNumberList);
    			
    			for (int attributeNumber : attributeNumberList) {
    				Float[] aRange = new Float[2];
    				aRange[0] = (Float) attributeTupleMap.get(convertedMapKey).get(position[0]).get(attributeNumber);
    				aRange[1] = (Float) attributeTupleMap.get(convertedMapKey).get(position[1]).get(attributeNumber);
    				attributeRangeList.add(aRange);

    			}
    			
    			System.out.println("New key: " + attributeNumberList + " & new range : " + attributeRangeList);
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
	    		rangeList.add(range);
	    		
	    		LinkedList<LinkedList> attributeTupleList = new LinkedList<>();
	    		
	    		// convert key
	    		LinkedList<Float[]> convertedKey = new LinkedList<>(attributeRangeMap.get(attributeNumberList));
	    			    		
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
    
    protected LinkedList<Float[]> getAttributeRangeList() {
    	return attributeRangeList;
    }
     
}
package tools;

import java.util.*;
import java.util.stream.Collectors;

public class Analyser {
	
    private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap;
    private LinkedList<LinkedList<Float[]>> attributeRangeList;

    public Analyser() {
    }
    
    /***
     * Get max-min range r(a) for each attribute (a)
     * RangeMap <- r(a)
     */
    protected void maxMin() {
    	
    	// for each class
        Scanners.allClassMap.keySet().forEach(k -> {
            String classTag = k;
            
            // one rangeMap for each class
            attributeRangeMap = new LinkedHashMap<>();

            // for each attribute
            for (int i = 1; i < Scanners.allClassMap.get(k).get(0).size() - 1; i++) {
            	
            	int attributeNumber = i;
               	LinkedList<Integer> attributeNumberList = new LinkedList<>();
            	attributeNumberList.add(attributeNumber);

            	
            	// find max-min range for each attribute
                LinkedList<Float> attributeValue = new LinkedList<>();
                Scanners.allClassMap.get(k).stream().forEach(t -> {
                    attributeValue.add((Float) t.get(attributeNumber));
                });

                Collections.sort(attributeValue);
                
                Float[] attributeRange = new Float[2];
                attributeRange[0] = attributeValue.getFirst();
                attributeRange[1] = attributeValue.getLast();
                LinkedList<Float[]> attributeRangeL = new LinkedList<>();
                attributeRangeL.add(attributeRange);
				// linkedlist conversion for min-max range to fit class type
                LinkedList<LinkedList<Float[]>> attributeRangeList = new LinkedList<>();
                attributeRangeList.add(attributeRangeL);

				
                // append attributeRangeMap
                attributeRangeMap.put(attributeNumberList, attributeRangeList);
            }
            
             convertToBinary(classTag, attributeRangeMap);
                
            //new Generator(classTag, allTuple, attributeRangeMap, attributeTupleMap);
        });
    }
    
    
     protected void convertToBinary(String classTag, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap) {
    	 
    	 // for each attribute
    	 for (Object key : attributeRangeMap.keySet()) {
    		 LinkedList<Integer> keyList = ((LinkedList<Integer>) key);

    		 LinkedList<LinkedList<Float[]>> rangeList = new LinkedList<>(attributeRangeMap.get(key));

    		 // create a binary list
    		 LinkedList<Integer> binaryList = new LinkedList<>();
    		 
    		 // find sub-tuples under indicated range
    		 // get attribute number and range first
             LinkedList attributeRangeTupleList = new LinkedList<LinkedList>();
             LinkedList<Float[]> rangeL = rangeList.getFirst();
             Float[] range = rangeL.getFirst();
             
             
             Scanners.allTuple.stream().forEach(t -> {
                 if (range[0] <= (Float) t.get(keyList.getFirst()) && (Float) t.get(keyList.getFirst()) <= range[1]) {
                     attributeRangeTupleList.add(t);
                 }
             });
             
             Comparator<LinkedList> comp = (a, b)-> ((Float) a.get(attributeNumber)).compareTo((Float) b.get(attributeNumber));
			 attributeRangeTupleList.sort(comp);
				
			// IN PROGRESS FROM HERE 
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


    protected void maxSum(LinkedList<Integer> attributeNumberList, LinkedList<Integer> binaryList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
    	
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
          
        // call checker
        checkSupportConfidence(attributePosition, attributeNumberList, binaryList, attributeRangeMap, attributeTupleMap);
    }
    

    protected void checkSupportConfidence(LinkedList<int[]> attributePosition, LinkedList<Integer> attributeNumberList, LinkedList<Integer> binaryList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {

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
	        	attributeCheckedPosition.add(currentPosition);
	        } 
    	}
    	
    	// call converter
    	convertPositionToRange(attributeCheckedPosition, attributeNumberList, attributeRangeMap, attributeTupleMap);
    }
    
    private void convertPositionToRange(LinkedList<int[]> attributeCheckedPosition,LinkedList<Integer> attributeNumberList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
    	
    	attributeRangeList = new LinkedList<>();

    	for (int[] position : attributeCheckedPosition) {

    		// convert position to numerical equivalence

    		LinkedList<LinkedList<Float[]>> convertedMapKey = attributeRangeMap.get(attributeNumberList);
    		
    		LinkedList<Float[]> rangeList = new LinkedList<>();
    		for (Integer attributeNumber : attributeNumberList) {
    			Float[] range = new Float[2];
    			range[0] = (Float) attributeTupleMap.get(convertedMapKey).get(position[0]).get(attributeNumber);
    			range[1] = (Float) attributeTupleMap.get(convertedMapKey).get(position[1]).get(attributeNumber);
    			
    			rangeList.add(range);
    		}
    		attributeRangeList.add(rangeList);
    	}
    	
		// for 1st iteration
		// call appendToMap
		if (attributeNumberList.size() <= 1) {
			appendToMap(attributeRangeList, attributeNumberList);
		}
    }
    
    private void appendToMap(LinkedList<LinkedList<Float[]>> attributeRangeList, LinkedList<Integer> attributeNumberList) {
    	
    	// append tuple map first 
    	
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

    		attributeTupleMap.put(attributeRangeList2, attributeTupleList);
    	}

    	// append range map
    	attributeRangeMap.put(attributeNumberList, attributeRangeList);
    }
    
    protected LinkedList<LinkedList<Float[]>> getAttributeRangeList() {
    	return attributeRangeList;
    }
     
}
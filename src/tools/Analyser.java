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
            
			 LinkedList attributeRangedTupleList = new LinkedList<LinkedList>();
			 System.out.println("pre-analysis :\n" + attributeRangeMap.entrySet());
			 
			 // binary converter
             convertToBinary(classTag, attributeRangedTupleList, attributeRangeMap);
             
             System.out.println("post-analysis: \n" + attributeRangeMap.entrySet());
             
             // Generator class initialised
             new Generator(classTag, attributeRangeMap);
        });
    }
    
    
     protected void convertToBinary(String classTag, LinkedList<LinkedList> attributeRangedTupleList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap) {
    	 
    	 // for each attribute
    	 for (Object key : attributeRangeMap.keySet()) {
    		 LinkedList<Integer> keyList = ((LinkedList<Integer>) key);

    		 LinkedList<LinkedList<Float[]>> rangeList = new LinkedList<>(attributeRangeMap.get(key));

    		 // create a binary list
    		 LinkedList<Integer> binaryList = new LinkedList<>();
    		 
    		 // ONLY FOR 1ST ROUND
    		 // for 2nd+ round tuple list is pre-defined from previous methods
    		 if (keyList.size() == 1) {
    			 // find sub-tuples under indicated range
    			 // get attribute number and range first

    			 LinkedList<Float[]> rangeL = rangeList.getFirst();
    			 Float[] range = rangeL.getFirst();

    			 Scanners.allTuple.stream().forEach(t -> {
    				 if (range[0] <= (Float) t.get(keyList.getFirst()) && (Float) t.get(keyList.getFirst()) <= range[1]) {
    					 attributeRangedTupleList.add(t);
    				 }
    			 });

    			 Comparator<LinkedList> comp = (a, b)-> ((Float) a.get(keyList.getFirst())).compareTo((Float) b.get(keyList.getFirst()));
    			 attributeRangedTupleList.sort(comp);
    		 }
    		 
    		 attributeRangedTupleList.stream().forEach(tuple -> {
    			 // append 1 and -1 into an array for max sum solution
    			 if (tuple.getLast().equals(classTag)) {
    				 binaryList.add(1);
    			 } else {
    				 binaryList.add(-1);
    			 }
    		 });
    		 
    		 maxSum(keyList, binaryList, attributeRangeMap, attributeRangedTupleList);
    	 }

    }


    protected void maxSum(LinkedList<Integer> keyList, LinkedList<Integer> binaryList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedList<LinkedList> attributeRangedTupleList) {
    	System.out.println("binaryList pre-maxsum " + binaryList);
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
        
        System.out.println("attributePosition post-maxsum " + attributePosition);
        // call checker
        checkSupportConfidence(attributePosition, keyList, binaryList, attributeRangeMap, attributeRangedTupleList);
    }
    

    protected void checkSupportConfidence(LinkedList<int[]> attributePosition, LinkedList<Integer> keyList, LinkedList<Integer> binaryList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedList<LinkedList> attributeRangedTupleList) {

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
	        if (support >= 0.5) {
	        	if (confidence >= 0.5) {
	        		attributeCheckedPosition.add(currentPosition.clone());
	        	}
	        } 
    	}
    	
    	// call converter
    	convertPositionToRange(attributeCheckedPosition, keyList, attributeRangeMap, attributeRangedTupleList);
    }
    
    private void convertPositionToRange(LinkedList<int[]> attributeCheckedPosition,LinkedList<Integer> keyList, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedList<LinkedList> attributeRangedTupleList) {
    	
    	attributeRangeList = new LinkedList<>();
    	System.out.println("All positions (checked) : " + attributeCheckedPosition);
    	for (int[] position : attributeCheckedPosition) {
    		
    		LinkedList<Float[]> rangeList = new LinkedList<>();
    		
    		for (Integer attributeNumber : keyList) {
    			Float[] range = new Float[2];
    			range[0] = (Float) attributeRangedTupleList.get(position[0]).get(attributeNumber);
    			range[1] = (Float) attributeRangedTupleList.get(position[1]).get(attributeNumber);
    			
    			rangeList.add(range);
    		}
    		attributeRangeList.add(rangeList);
    	}
    	
    	// append range map
    	attributeRangeMap.put(keyList, attributeRangeList);
    		
    }
    
    protected LinkedList<LinkedList<Float[]>> getAttributeRangeList() {
    	return attributeRangeList;
    }
     
}
package Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Phuongt994 on 10/07/2016.
 */
public class Generator {
    private String classTag;
    private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap;
    private LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap;
    private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> newAttributeRangeMap;
    private LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> newAttributeTupleMap;

    public Generator(String classTag, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
        this.classTag = classTag;
        this.attributeRangeMap = attributeRangeMap;
        this.attributeTupleMap = attributeTupleMap;
        
        System.out.println("Generator started for " + this.classTag);
        System.out.println("attributeRangeMap :" + this.attributeRangeMap);
        System.out.println("attributeTupleMap :" + this.attributeTupleMap);
        
    	newAttributeRangeMap = new LinkedHashMap<>();
    	newAttributeTupleMap = new LinkedHashMap<>();
    			
        aPriori();     
        
        // recursive call after 1 round done
        // only if there is possible pair left
        // if not, finalise rule
        
        if (newAttributeRangeMap.keySet().size() > 1 ) {
        	System.out.println("Continue for next loop");
        	new Generator(this.classTag, newAttributeRangeMap, newAttributeTupleMap);  
        } else {
        	System.out.println("No possible pair - loop ends");
        	finaliseRule(newAttributeRangeMap, newAttributeTupleMap);
        }
    } 

    private void aPriori() {

    	System.out.println("Apriori started");
       
    	LinkedList rangeMapKeySet = new LinkedList<Object>(attributeRangeMap.keySet());
    	
    	// NEED different way to check null
    	// may move null checker to combined method 
    	// keep pairing until no pair left (size = 1)
    	while (rangeMapKeySet.size() > 1) {
    		
	    	for (int i = 0; i < rangeMapKeySet.size(); i++) {
	
	    		Object key1 = rangeMapKeySet.getFirst();
	
	    		System.out.println("Attribute number " + (LinkedList<Integer>) key1 + " is being processed");
	
	    		rangeMapKeySet.remove(key1);
	
	    		System.out.println("keySet after key " + (LinkedList<Integer>) key1 + " is removed");
	
	    		for (Object postKey : rangeMapKeySet) {
	    			System.out.print((LinkedList<Integer>) postKey);
	    		}  		
	
	    		System.out.println("All ranges in range 1 : " + attributeRangeMap.get(key1));
	
	    		// skip null ranges
	    		if (attributeRangeMap.get(key1).isEmpty()) {
	    			System.out.println("Range1 is null - skipped");
	    			continue;
	    			
	    		} else {
	
	    			for (int j = 0; j < rangeMapKeySet.size(); j++) {
	    				
	    				Object key2 = rangeMapKeySet.get(j);
	
	    				// skip null ranges
	    				if (attributeRangeMap.get(key2).isEmpty()) {
	    					System.out.println("Range2 is null - skipped");
	    					continue;
	    				} else {
		    				LinkedList<LinkedList<Integer>> aCombinedKey = setACombinedKey(key1, key2);
		    				setACombinedRange(aCombinedKey);	
	    				}
	    			}
	    		}
	    	}
    	}
    }
    
    private LinkedList<LinkedList<Integer>> setACombinedKey(Object key1, Object key2) {
    	LinkedList<Integer> key1List = new LinkedList<>((LinkedList<Integer>) key1); 
    	LinkedList<Integer> key2List = new LinkedList<>((LinkedList<Integer>) key2);
    	LinkedList<LinkedList<Integer>> aCombinedKey = new LinkedList<>();
    	
    	// for 1st iteration
    	if (key1List.size() <= 1) {
			aCombinedKey.add(key1List);
			aCombinedKey.add(key2List);
    	} else {
    		if (key1List.getFirst() == key2List.getFirst() && key1List.getFirst() < key2List.getLast()) {
    			aCombinedKey.add(key1List);
    			aCombinedKey.add(key2List);
    		}
    	}
    	return aCombinedKey;
    }
    
    private void setACombinedRange(LinkedList<LinkedList<Integer>> aCombinedKey) {
    	
		for (LinkedList<Float[]> range1 : attributeRangeMap.get(aCombinedKey.getFirst())) {

			// for each range in a remaining key
			// call analyser
			for (LinkedList<Float[]> range2 : attributeRangeMap.get(aCombinedKey.getLast())) {
				LinkedList<LinkedList<LinkedList<Float[]>>> aCombinedRange = new LinkedList<>();
				LinkedList<LinkedList<Float[]>> range1List = new LinkedList<>();
				LinkedList<LinkedList<Float[]>> range2List = new LinkedList<>();
				range1List.add(range1);
				range2List.add(range2);
				aCombinedRange.add(range1List);
				aCombinedRange.add(range2List);
				
				System.out.println("A combined key : " + aCombinedKey);
				System.out.println("A combined range : " + aCombinedRange);
				
				analyseNewCombination(aCombinedKey, aCombinedRange);
			}
		}
    }
    
    private void analyseNewCombination(LinkedList<LinkedList<Integer>> aCombinedKey, LinkedList<LinkedList<LinkedList<Float[]>>> aCombinedRange) {
    	Analyser2 analyser = new Analyser2(null, null, classTag, aCombinedKey, aCombinedRange, (LinkedHashMap) attributeRangeMap.clone(), (LinkedHashMap) attributeTupleMap.clone());
    	
    	if (analyser.getNewCombinedRange() != null) {
    		
    		// append new combination to 2 maps
    		LinkedList<LinkedList<Float[]>> newCombinedRange = new LinkedList<>(analyser.getNewCombinedRange());

			LinkedList<Integer> newCombinedKey = new LinkedList<>(analyser.getNewCombinedKey());
			
			LinkedList<LinkedList> newMutualTupleList = new LinkedList<>(analyser.getNewMutualTupleList());
			
			System.out.println("New combined range for " + newCombinedKey + " is " + newCombinedRange);
			newAttributeRangeMap.put(newCombinedKey, newCombinedRange);
			System.out.println("Tuple under this range : " + newMutualTupleList);
			newAttributeTupleMap.put(newCombinedRange, newMutualTupleList);
		} else {
			System.out.println("No range found");
		}
    }
    
    private void finaliseRule(LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> newAttributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> newAttributeTupleMap) {
    	System.out.println("----------------");
    	System.out.println("End of loop");
    	System.out.println("Final attributeRangeMap for class : " + classTag + " is " + newAttributeRangeMap);
    	System.out.println("Final attributeTupleMap for class : " + classTag + " is " + newAttributeTupleMap);
    	System.out.println("Classification rule for class " + classTag);
    	for (Object key : newAttributeRangeMap.keySet()) {
    		LinkedList<Integer> attributeNumberList = new LinkedList<>((LinkedList<Integer>) key);
    		for (int i = 0; i < attributeNumberList.size(); i++) {
    			System.out.println("Attribute " + attributeNumberList.get(i) + " has range");
    			System.out.println(Arrays.toString(newAttributeRangeMap.get(key).get(i).get(0)));
    		}
    	}
    	
    }
}

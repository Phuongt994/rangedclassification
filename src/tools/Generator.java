package tools;

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
    private LinkedList<LinkedList> allTuple;
    private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap;
    private LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap;
    private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> newAttributeRangeMap;
    private LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> newAttributeTupleMap;

    public Generator(String classTag, LinkedList<LinkedList> allTuple, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
        this.classTag = classTag;
        this.allTuple = allTuple;
        this.attributeRangeMap = attributeRangeMap;
        this.attributeTupleMap = attributeTupleMap;
        
    	newAttributeRangeMap = new LinkedHashMap<>();
    	newAttributeTupleMap = new LinkedHashMap<>();
    			
        aPriori();     
        
        // recursive call after 1 round done
        // only if there is possible pair left
        // if not, finalise rule
        
        if (newAttributeRangeMap.keySet().size() > 1 ) {
        	new Generator(this.classTag, allTuple, newAttributeRangeMap, newAttributeTupleMap);  
        } else {
        	finaliseRule(newAttributeRangeMap, newAttributeTupleMap);
        }
    } 

    private void aPriori() {

    	LinkedList rangeMapKeySet = new LinkedList<Object>(attributeRangeMap.keySet());
    	
    	// keep pairing until no pair left (size = 1)
    	while (rangeMapKeySet.size() >= 1) {
    		
	    	for (int i = 0; i < rangeMapKeySet.size(); i++) {
	
	    		Object key1 = rangeMapKeySet.getFirst();
	
	
	    		rangeMapKeySet.remove(key1);
	
	    		// skip null ranges
	    		if (attributeRangeMap.get(key1).isEmpty()) {
	    			continue;
	    			
	    		} else {
	
	    			for (int j = 0; j < rangeMapKeySet.size(); j++) {
	    				
	    				Object key2 = rangeMapKeySet.get(j);
	
	    				// skip null ranges
	    				if (attributeRangeMap.get(key2).isEmpty()) {
	    					continue;
	    				} else {
		    				LinkedList<LinkedList<Integer>> aCombinedKey = setACombinedKey(key1, key2);
		    				if (!aCombinedKey.isEmpty()) {
		    					setACombinedRange(aCombinedKey);
		    				}
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
				
				analyseNewCombination(aCombinedKey, aCombinedRange);
			}
		}
    }
    
    private void analyseNewCombination(LinkedList<LinkedList<Integer>> aCombinedKey, LinkedList<LinkedList<LinkedList<Float[]>>> aCombinedRange) {
    	SubAnalyser subAnalyser = new SubAnalyser(null, null, classTag, aCombinedKey, aCombinedRange, (LinkedHashMap) attributeRangeMap.clone(), (LinkedHashMap) attributeTupleMap.clone());
    	
    	if (subAnalyser.getNewCombinedRange() != null) {
    		
    		// append new combination to 2 maps
    		LinkedList<LinkedList<Float[]>> newCombinedRange = new LinkedList<>(subAnalyser.getNewCombinedRange());

			LinkedList<Integer> newCombinedKey = new LinkedList<>(subAnalyser.getNewCombinedKey());
			
			LinkedList<LinkedList> newMutualTupleList = new LinkedList<>(subAnalyser.getNewMutualTupleList());
			
			newAttributeRangeMap.put(newCombinedKey, newCombinedRange);
			newAttributeTupleMap.put(newCombinedRange, newMutualTupleList);
		} 
    }
    
    private void finaliseRule(LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> newAttributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> newAttributeTupleMap) {
    	
    	new Monitor(classTag, allTuple, newAttributeRangeMap, newAttributeTupleMap);	
    }
}
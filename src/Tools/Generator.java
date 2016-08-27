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
    private LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap;
    private LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap;

    public Generator(String classTag, LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap, LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap) {
        this.classTag = classTag;
        this.attributeRangeMap = attributeRangeMap;
        this.attributeTupleMap = attributeTupleMap;
        
        System.out.println("Generator started for " + this.classTag);
        System.out.println("attributeRangeMap :" + attributeRangeMap);
        System.out.println("attributeTupleMap :" + attributeTupleMap);
        
        aPriori();     
        // recursive for next iteration
        // new Generator(this.classTag, generatedCRMap, this.allTuple, this.allClassMap, this.allAttributeMap);  
    } 

    private void aPriori() {

    	System.out.println("Apriori started");
       
    	for (Object key1 : new ArrayList<Object>(attributeRangeMap.keySet())) {
    		LinkedList<Integer> aPoppedKey = (LinkedList<Integer>) key1;
    		
    		System.out.println("Attribute number " + aPoppedKey.toString() + " is being processed");
    		
    		LinkedList<Float[]> aPoppedKeyRangeList = new LinkedList<>(attributeRangeMap.get(key1));
    		
    		attributeRangeMap.keySet().remove(key1);
    		
    		System.out.println("keySet after key " + aPoppedKey + " is removed");
    		for (Object postKey : attributeRangeMap.keySet()) {
    			System.out.print((LinkedList<Integer>) postKey);
    		}
    		
    		// initialise combined key
    		LinkedList<LinkedList<Integer>> aCombinedKey = new LinkedList<>();
    		aCombinedKey.add(aPoppedKey);
    		
    		for (Float[] aPoppedKeyRange : aPoppedKeyRangeList) {
    			
    			// for each remaining key
    			for (Object key2 : attributeRangeMap.keySet()) {
    				LinkedList<Integer> aRemainingKey = (LinkedList<Integer>) key2;
    				aCombinedKey.add(aRemainingKey);
    	        	System.out.println("Now pairing attr (key) ranges " + aCombinedKey.getFirst() + " to attr (key) ranges " + aRemainingKey.getLast());
    				
    				// for each range in each remaining key
    				for (Float[] aRemainingKeyRange : attributeRangeMap.get(key2)) {
        				// initialise combined range
        	    		LinkedList<Float[]> aCombinedRange = new LinkedList<>();
        	    		aCombinedRange.add(aPoppedKeyRange);
    					aCombinedRange.add(aRemainingKeyRange);
    					
    					// initialise analyserGen
    					System.out.println("AnalyserGen started");
    					// AnalyserGen analyser = new AnalyserGen(null, null, classTag, aCombinedKey, aCombinedRange, attributeRangeMap, attributeTupleMap);
    					
    					// ongoing
    				}
    			}
    		}
    	}
    }
}

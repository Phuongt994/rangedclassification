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
    private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<int[]>>> LR;
    private LinkedList<LinkedList> allTuple;
    private HashMap<String, LinkedList<LinkedList>> allClassMap;
    private String classTag;
    private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList>> allAttributeMap;
    private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<int[]>>> generatedCRMap;
    public Generator(String classTag, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<int[]>>> LR, LinkedList<LinkedList> allTuple, HashMap<String, LinkedList<LinkedList>> allClassMap, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList>> allAttributeMap) {
        this.LR = LR;
        this.classTag = classTag;
        this.allTuple = allTuple;
        this.allClassMap = allClassMap;
        this.allAttributeMap = allAttributeMap;
        System.out.println("Generator started for " + this.classTag);
        for (Object key : LR.keySet()) {
        	System.out.println("For attribute number " + (LinkedList<Integer>) key + " Ranges are: ");
        	for (LinkedList<int[]> aPosRange : LR.get(key)) {
        		for (int[] range : aPosRange) {
        			System.out.println(Arrays.toString(range));
        		}
        	}
        }
    	generatedCRMap = new LinkedHashMap<>();
        aPriori(this.LR);
        
        // recursive for next iteration!
        // have to loop this 
        new Generator(this.classTag, generatedCRMap, this.allTuple, this.allClassMap, this.allAttributeMap);  
    } 

    private void aPriori(LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<int[]>>> LR) {

    	System.out.println("Apriori started");
       
    	LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<int[]>>> tempLR = new LinkedHashMap<>(LR);
    	LR.clear();
        
        for (Object k : new ArrayList<Object>(tempLR.keySet())) {
  
        	LinkedList<Integer> aKey = (LinkedList<Integer>) k;
        	// Would getFirst() work for next iteration? - need change
        	Integer aPoppedKey = (Integer) aKey.getFirst();
        	
        	System.out.println("Attribute number " + aPoppedKey + " is being processed");
        	// get combined key (integer array)
        	// set combined key [0] = popped key
        	// CHANGE I TO BE DYNAMIC PLEEEEES
        	// aCombinedKey here now moved into loop due to the need for renewal at every loop 
        	
        	
    		// get combined range (linked list of linked list of float)
        	// get popped key's ranges before delete
    		LinkedList<LinkedList<int[]>> combinedRange = new LinkedList<>();
    		LinkedList<LinkedList<int[]>> poppedKeyRangeList = new LinkedList<>(tempLR.get(k));
    		LinkedList<int[]> poppedKeyRange = new LinkedList<>(poppedKeyRangeList.get(0));
    		
    		tempLR.keySet().remove(k);
    		System.out.println("keySet after key " + aPoppedKey + " is removed");
    		
    		for (Object postKey : tempLR.keySet()) {
    			System.out.print((LinkedList<Integer>) postKey);
    		}
    		
    		// for each remaining key range
    		for (Object k2 : tempLR.keySet()) {
    	
            
    			LinkedList<Integer> aKey2 = (LinkedList<Integer>) k2;
    			Integer aRemainingKey = (Integer) aKey2.getFirst();
    			
    			LinkedList<LinkedList<Integer>> aCombinedKey = new LinkedList<LinkedList<Integer>>();
            	aCombinedKey.add(aKey);
    			
    			// set combined key [1] = remaining key
    			System.out.println("Now pairing attr (key) ranges " + aPoppedKey + " to attr (key) ranges " + aRemainingKey);
				aCombinedKey.add(aKey2);
				
				// for each popped key range
    			for (int[] aPoppedKeyRange : poppedKeyRange) {
    				// for each range in remaining key
    	    		LinkedList<LinkedList<int[]>> remainingKeyRangeList = new LinkedList<>(tempLR.get(k2));
    	    		LinkedList<int[]> remainingKeyRange = new LinkedList<>(remainingKeyRangeList.get(0));
    				for (int[] aRemainingKeyRange : remainingKeyRange) {
    					// get a 'combined' range
    					System.out.println("Now pairing " + Arrays.toString(aPoppedKeyRange) + " with " + Arrays.toString(aRemainingKeyRange));
    					LinkedList<int[]> aCombinedRange = new LinkedList<>();
    					aCombinedRange.add(aPoppedKeyRange);
    					aCombinedRange.add(aRemainingKeyRange);
    					
    					// CHECKER BEFORE ACCEPTED
    					// THIS CONTAINS BAD RECURSIVE CALLS
    					System.out.println("AnalyserGen started");
    	    			AnalyserGen analyser = new AnalyserGen(this.allTuple, this.allClassMap, aCombinedKey, aCombinedRange, classTag, this.allAttributeMap);
    	    			if (analyser.getRejectedRange() == true) {
    	    				System.out.println("This pair is rejected");
    	    			} else {
	    					// add combined range to big combined range list
	    					combinedRange.add(aCombinedRange);
	    					System.out.println("A combined range for " + aCombinedKey + " is ");
	    	    			for (LinkedList<int[]> cR : combinedRange) {
	    	    				 for (int[] fl : cR) {
	    	    					 System.out.println(Arrays.toString(fl));
	    	    				 }
	    	    			 }
	    	    			 // put key and range in generated CR 
	    	    			 System.out.println("generatedCR is being appended");
	    	    			 // clone??
	    	    			 LinkedList<Integer> aCombinedKeyList = new LinkedList<>();
	    	    			 aCombinedKeyList.add(aCombinedKey.get(0).get(0));
	    	    			 aCombinedKeyList.add(aCombinedKey.get(1).get(0));
	    	    			 generatedCRMap.put(aCombinedKeyList, combinedRange); 
	 
    	    			}
    				}
    			}
    		}
        }
        
		System.out.println("Generated done");

		for (Object aCRKey : generatedCRMap.keySet()) {
			System.out.println("New attribute combination : " + (LinkedList<Integer>) aCRKey);
			for (LinkedList<int[]> aVal : generatedCRMap.get(aCRKey)) {
				for (int[] aValList : aVal) {
					System.out.println(">> Ranges are : " + Arrays.toString(aValList));
				}
			}
		}    
    }
}

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
    private LinkedHashMap<LinkedList<Integer>, LinkedList<int[]>> LR;
    private LinkedList<LinkedList> allTuple;
    private HashMap<String, LinkedList<LinkedList>> allClassMap;
    private String classTag;
    
    public Generator(String classTag, LinkedHashMap<LinkedList<Integer>, LinkedList<int[]>> LR, LinkedList<LinkedList> allTuple, HashMap<String, LinkedList<LinkedList>> allClassMap) {
        this.LR = LR;
        this.classTag = classTag;
        this.allTuple = allTuple;
        this.allClassMap = allClassMap;
        System.out.println("Generator started for " + this.classTag);
        for (Object key : LR.keySet()) {
        	System.out.println("For attribute number " + (int) key + " Ranges are: ");
        	for (int[] aPosRange : LR.get(key)) {
        		System.out.println(Arrays.toString(aPosRange));
        	}
        }
        aPriori(this.LR);
    
        
    } 

    private void aPriori(LinkedHashMap<LinkedList<Integer>, LinkedList<int[]>> LR) {
    	System.out.println("Apriori started");
       
    	LinkedHashMap<LinkedList<Integer>, LinkedList<int[]>> tempLR = new LinkedHashMap<>(LR);
        
        for (Object aKey : new ArrayList<Object>(tempLR.keySet())) {
        	LinkedList<Integer> aKeyList = (LinkedList<Integer>) aKey;
        	// Would getFirst() work for next iteration?
        	int key = (Integer) aKeyList.getFirst();
        	LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<int[]>>> generatedCRMap = new LinkedHashMap<>();
        	Object aPoppedKey = key;
        	System.out.println("Attribute number " + (Integer) aPoppedKey + " is being processed");
        	// get combined key (integer array)
        	// set combined key 0 = popped key
        	// CHANGE I TO BE DYNAMIC PLEEEEES
        	LinkedList<Integer> aCombinedKey = new LinkedList<Integer>();
        	aCombinedKey.add((Integer) aPoppedKey);
    		// get combined range (linked list of linked list of float)
    		LinkedList<LinkedList<int[]>> combinedRange = new LinkedList<>();
    		
    		// get popped key's ranges before delete
    		LinkedList<int[]> poppedKeyRange = new LinkedList<>(tempLR.get(aPoppedKey));
    		tempLR.keySet().remove(aPoppedKey);
    		System.out.println("keySet after key " + (Integer) aPoppedKey + " is removed");
    		for (Object postKey : tempLR.keySet()) {
    			System.out.println((Integer) postKey);
    		}
    		
    		// for each remaining key range
    		for (Object remainingKey : tempLR.keySet()) {
    			System.out.println("Now pairing attr (key) ranges " + (Integer) aPoppedKey + " to attr (key) ranges " + (Integer) remainingKey);
    			// set combined key 1 = remaining key
				aCombinedKey.add((Integer) remainingKey);
				
				// for each popped key range
    			 for (int[] aPoppedKeyRange : poppedKeyRange) {
    				// for each range in remaining key
    				for (int[] aRemainingKeyRange : tempLR.get(remainingKey)) {
    					// get a 'combined' range
    					System.out.println("Now pairing " + Arrays.toString(aPoppedKeyRange) + " with " + Arrays.toString(aRemainingKeyRange));
    					LinkedList<int[]> aCombinedRange = new LinkedList<>();
    					aCombinedRange.add(aPoppedKeyRange);
    					aCombinedRange.add(aRemainingKeyRange);
    					
    					// CHECKER BEFORE ACCEPTED
    	    			 new AnalyserGen(allTuple, allClassMap, aCombinedKey, aCombinedRange, classTag);
    	    			 
    					// add combined range to big combined range list
    					combinedRange.add(aCombinedRange);
    				} 			
    			}
    			 System.out.println("A combined range for " + aCombinedKey.clone() + " is ");
    			 for (LinkedList<int[]> cR : combinedRange) {
    				 for (int[] fl : cR) {
    					 System.out.println(Arrays.toString(fl));
    				 }
    			 }
    			 
    			 
    			 // put key and range in generated CR 
    			 System.out.println("generatedCR is being appened");
    			 // clone??
    			 generatedCRMap.put((LinkedList<Integer>) aCombinedKey.clone(), combinedRange); 
    		}
    		
    		System.out.println("Generated done");
    		for (Object aCRKey : generatedCRMap.keySet()) {
    			System.out.println("New attribute combination : " + Arrays.toString((int[]) aCRKey));
    			if (generatedCRMap.get(aCRKey) != null) {
    				for (LinkedList<int[]> aVal : generatedCRMap.get(aCRKey)) {
    					System.out.println(">> Ranges are : " + aVal);
    				}
    			} else {
    				System.out.println("Null range");
    			}
    		}
        }
        
        
    }
}

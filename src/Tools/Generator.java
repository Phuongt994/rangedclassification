package Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Phuongt994 on 10/07/2016.
 */
public class Generator {
    private LinkedHashMap<Integer, LinkedList<Float[]>> LR;
    private String classTag;
    public Generator(String classTag, LinkedHashMap<Integer, LinkedList<Float[]>> LR) {
        this.LR = LR;
        this.classTag = classTag;
        System.out.println("Generator started for " + this.classTag);
        for (Object key : LR.keySet()) {
        	System.out.println("For attribute number " + (int) key + " Ranges are: ");
        	for (Float[] aFloatRange : LR.get(key)) {
        		System.out.println(Arrays.toString(aFloatRange));
        	}
        }
        aPriori(this.LR);
    }

    private void aPriori(LinkedHashMap<Integer, LinkedList<Float[]>> LR) {
    	System.out.println("Apriori started");
        LinkedHashMap<int[], LinkedList<LinkedList<Float[]>>> generatedCR = new LinkedHashMap<>();
        LinkedHashMap<Integer, LinkedList<Float[]>> tempLR = new LinkedHashMap<>(LR);
        
        for (Object key : new ArrayList<Object>(tempLR.keySet())) {
        	Object poppedKey = key;
        	System.out.println("Attribute number " + (Integer) poppedKey + " is being processed");
        	// get combined key (integer array)
        	// set combined key 0 = popped key
        	// consider making the array larger with each loop
        	int i = 2;
        	int[] aCombinedKey = new int[i];
        	aCombinedKey[0] = (int) poppedKey;
    		// get combined range (linked list of linked list of float)
    		LinkedList<LinkedList<Float[]>> combinedRange = new LinkedList<>();
    		
    		// get popped key's ranges before delete
    		LinkedList<Float[]> poppedKeyRange = new LinkedList<>(tempLR.get(poppedKey));
    		tempLR.keySet().remove(poppedKey);
    		System.out.println("keySet after key " + (Integer) poppedKey + " is removed");
    		for (Object postKey : tempLR.keySet()) {
    			System.out.println((Integer) postKey);
    		}
    		
    		// for each remaining key range
    		for (Object remainingKey : tempLR.keySet()) {
    			System.out.println("Now pairing attr (key) ranges " + (Integer) poppedKey + " to attr (key) ranges " + (Integer) remainingKey);
    			// set combined key 1 = remaining key
				aCombinedKey[1] = (int) remainingKey;
				
				// for each popped key range
    			 for (Float[] aPoppedKeyRange : poppedKeyRange) {
    				// for each range in remaining key
    				for (Float[] aRemainingKeyRange : tempLR.get(remainingKey)) {
    					// get a 'combined' range
    					System.out.println("Now pairing " + Arrays.toString(aPoppedKeyRange) + " wih " + Arrays.toString(aRemainingKeyRange));
    					LinkedList<Float[]> aCombinedRange = new LinkedList<>();
    					aCombinedRange.add(aPoppedKeyRange);
    					aCombinedRange.add(aRemainingKeyRange);
    					// add combined range to big combined range list
    					combinedRange.add(aCombinedRange);
    				} 			
    			}
    		}
    		
    		System.out.println("A combined range for " + Arrays.toString(aCombinedKey) + " is ");
    		for (LinkedList<Float[]> cR : combinedRange) {
    			for (Float[] fl : cR) {
    				System.out.println(Arrays.toString(fl));
    			}
    		}
        	// put key and range in generated CR 
    		System.out.println("generatedCR is being appened");
        	generatedCR.put(aCombinedKey, combinedRange);
        }
        
        System.out.println("Generated done");
        for (Object key : generatedCR.keySet()) {
        	System.out.println("New attribute combination : " + Arrays.toString((int[]) key));
        	if (generatedCR.get(key) != null) {
        		for (LinkedList<Float[]> val : generatedCR.get(key)) {
        			System.out.println(">> Ranges are : " + val);
        		}
        	} else {
        		System.out.println("Null range");
        	}
        }
    }
}

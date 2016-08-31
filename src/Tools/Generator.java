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
        
        aPriori();     
        // recursive for next iteration
        // new Generator(this.classTag, generatedCRMap, this.allTuple, this.allClassMap, this.allAttributeMap);  
    } 

    private void aPriori() {
    	newAttributeRangeMap = new LinkedHashMap<>();
    	newAttributeTupleMap = new LinkedHashMap<>();

    	System.out.println("Apriori started");
       
    	LinkedList rangeMapKeySet = new LinkedList<Object>(attributeRangeMap.keySet());
    	if (rangeMapKeySet.size() <= 1) {
    		System.out.println("Loop ends");
    		finalise(attributeRangeMap, attributeTupleMap);
    	} else {
	    	
	    	for (int i = 0; i < rangeMapKeySet.size(); i++) {
	    		Object key1 = rangeMapKeySet.getFirst();
	    		
	    		System.out.println("Attribute number " + (LinkedList<Integer>) key1 + " is being processed");
	    		
	    		rangeMapKeySet.remove(key1);
	    		
	    		System.out.println("keySet after key " + (LinkedList<Integer>) key1 + " is removed");
	    		for (Object postKey : rangeMapKeySet) {
	    			System.out.print((LinkedList<Integer>) postKey);
	    		}  		
	    		
				System.out.println("ALl ranges in range 1 : " + attributeRangeMap.get(key1));
				
				// skip null ranges
				if (attributeRangeMap.get(key1).isEmpty()) {
					System.out.println("Range1 is null - skipped");
					continue;
					
				} else {
					
		    		for (int j = 0; j < rangeMapKeySet.size(); j++) {
		    			
		    			Object key2 = rangeMapKeySet.get(j);
		    			System.out.println("ALl ranges in range 2 : " + attributeRangeMap.get(key2));
		    			
		    			// skip null ranges
		    			if (attributeRangeMap.get(key2).isEmpty()) {
		    				System.out.println("Range2 is null - skipped");
		    				continue;
		    			}
		    			// for each remaining key
		    			// get combined key
		    			LinkedList<LinkedList<Integer>> aCombinedKey = new LinkedList<>();
		    			
		    			// check if remaining key is join-able
		    			if (((LinkedList<Integer>) key1).size() == 1) {
							// for 1st iteration
							// initialise combined key
							aCombinedKey.add((LinkedList<Integer>) key1);
							aCombinedKey.add((LinkedList<Integer>) key2);
							System.out.println("Now pairing attr (key) ranges " + aCombinedKey.getFirst() + " to attr (key) ranges " + aCombinedKey.getLast()); // change this for 3rd+ iteration		
						} else {
							// for 2nd+ iteration
							// key has to be joined differently
							LinkedList<Integer> key1List = new LinkedList<>((LinkedList<Integer>) key1);
							LinkedList<Integer> key2List = new LinkedList<>((LinkedList<Integer>) key2);
						
							 if (key1List.getFirst() == key2List.getFirst() && key1List.getFirst() < key2List.getLast()) {
							 	aCombinedKey.add(key1List);
							 	aCombinedKey.add(key2List);
							 }
						}
						
		    			System.out.println("attributeRangeMap " + attributeRangeMap);
		    			System.out.println("attributeTupleMap " + attributeTupleMap);
		    			
		    			if (((LinkedList<Integer>) key1).size() == 1) {
		    				// for 1st iteration
			    			for (LinkedList<Float[]> range1 : attributeRangeMap.get(key1)) {
			
			    				// for each range in each remaining key
			    				for (LinkedList<Float[]> range2 : attributeRangeMap.get(key2)) {
			
			    					LinkedList<LinkedList<LinkedList<Float[]>>> aCombinedRange = new LinkedList<>();
			    					LinkedList<LinkedList<Float[]>> range1List = new LinkedList<>();
			    					LinkedList<LinkedList<Float[]>> range2List = new LinkedList<>();
			    					range1List.add(range1);
			    					range2List.add(range2);
			    					aCombinedRange.add(range1List);
			    					aCombinedRange.add(range2List);
			
			    					System.out.println("A combined key : " + aCombinedKey);
			    					System.out.println("A combined range : " + aCombinedRange);
			    					// initialise analyserGen
			    					// with new (empty) range and tuple map
			    					System.out.println("AnalyserGen started");
			    					AnalyserGen analyserGen = new AnalyserGen(null, null, classTag, aCombinedKey, aCombinedRange, (LinkedHashMap) attributeRangeMap.clone(), (LinkedHashMap) attributeTupleMap.clone());
			    					
			    					if (analyserGen.getNewCombinedRange() != null) {
			    						// grab new combo from analyserGen
			    						LinkedList<LinkedList<Float[]>> newCombinedRange = new LinkedList<>(analyserGen.getNewCombinedRange());
			    						LinkedList<Integer> newCombinedKey = new LinkedList<>(analyserGen.getNewCombinedKey());
			    						LinkedList<LinkedList> newMutualTupleList = new LinkedList<>(analyserGen.getNewMutualTupleList());
			    						System.out.println("New combined range for " + newCombinedKey + " is " + newCombinedRange);
			    						newAttributeRangeMap.put(newCombinedKey, newCombinedRange);
			    						System.out.println("Tuple under this range : " + newMutualTupleList);
			    						newAttributeTupleMap.put(newCombinedRange, newMutualTupleList);
			    					} else {
			    						System.out.println("No range found");
			    					}
			    				}
			    			}
		    			} else {
		    				// for 2nd+ iteration
		    				LinkedList<LinkedList<Float[]>> range1 = attributeRangeMap.get(key1);
	
		    				LinkedList<LinkedList<Float[]>> range2 = attributeRangeMap.get(key2);
	
		    				LinkedList<LinkedList<LinkedList<Float[]>>> aCombinedRange = new LinkedList<>();
		    				aCombinedRange.add(range1);
		    				aCombinedRange.add(range2);
		    				System.out.println("AnalyserGen started");
		    				AnalyserGen analyserGen = new AnalyserGen(null, null, classTag, aCombinedKey, aCombinedRange, (LinkedHashMap) attributeRangeMap.clone(), (LinkedHashMap) attributeTupleMap.clone());
	
		    				if (analyserGen.getNewCombinedRange() != null) {
		    					// grab new combo from analyserGen
		    					LinkedList<LinkedList<Float[]>> newCombinedRange = new LinkedList<>(analyserGen.getNewCombinedRange());
		    					LinkedList<Integer> newCombinedKey = new LinkedList<>(analyserGen.getNewCombinedKey());
		    					LinkedList<LinkedList> newMutualTupleList = new LinkedList<>(analyserGen.getNewMutualTupleList());
		    					System.out.println("New combined range for " + newCombinedKey + " is " + newCombinedRange);
		    					newAttributeRangeMap.put(newCombinedKey, newCombinedRange);
		    					System.out.println("Tuple under this range : " + newMutualTupleList);
		    					newAttributeTupleMap.put(newCombinedRange, newMutualTupleList);
		    				} else {
		    					System.out.println("No range found");
		    				}
		    			}
		    		}	
	    		}
	    	}	
	    	
	    	System.out.println("New attributeRangeMap for class : " + classTag + " is " + newAttributeRangeMap);
	    	System.out.println("New attributeTupleMap for class : " + classTag + " is " + newAttributeTupleMap);
	    	System.out.println("Continue onto next loop");
    		new Generator(classTag, newAttributeRangeMap, newAttributeTupleMap);
    	}	
    }
    
    private void finalise(LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> newAttributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> newAttributeTupleMap) {
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

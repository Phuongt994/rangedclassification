package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Monitor {
	private String classTag;
	private LinkedList<LinkedList> allTuple;
	private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap;
	private LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap;
	private String ruleSet;
	
	public Monitor (String classTag, LinkedList<LinkedList> allTuple, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
		this.classTag = classTag;
		this.allTuple = allTuple;
		this.attributeRangeMap = attributeRangeMap;
		this.attributeTupleMap = attributeTupleMap;
		
		evaluateRule();
	}
	
	private void evaluateRule() {
		ruleSet = new String();
		
		// Confusion matrix used
    	System.out.println("\nFinal attributeRangeMap for class : " + classTag + " is " + attributeRangeMap);
    	// System.out.println("Final attributeTupleMap for class : " + classTag + " is " + attributeTupleMap); // not necessary? 
    	System.out.println("Classification rule for class " + classTag);
    	
    	LinkedList<LinkedList> coveredTupleList = new LinkedList<>(allTuple);
    	
    	for (Object key : attributeRangeMap.keySet()) {
    		
    		LinkedList<Integer> attributeNumberList = new LinkedList<>((LinkedList<Integer>) key);
    		
    		for (int i = 0; i < attributeNumberList.size(); i++) {
    			int attributeNumber = attributeNumberList.get(i);
    			
    			System.out.println("Attribute " + attributeNumber + " has range");
    			Float[] range = attributeRangeMap.get(key).get(0).get(i);
    			
    			// mini-swap 
    			// testing
    			if (range[0] > range[1]) {
    				Float temp = range[0];
    				range[0] = range[1];
    				range[1] = temp;
    			}
    			System.out.println(Arrays.toString(range));
    			
    			// record rule as string
    			String aRule = " \n\tattribute " + (Integer.toString(attributeNumber)) + " is within range " + Arrays.toString(range) + " ";
    			ruleSet = ruleSet + aRule;
    			
    			for (int j = 0; j < coveredTupleList.size(); j++) {
    				if (range[0] <= (Float) coveredTupleList.get(j).get(attributeNumber) && (Float) coveredTupleList.get(j).get(attributeNumber) <= range[1]) {
    					continue;
    				} else {
    					coveredTupleList.remove(coveredTupleList.get(j));
    				}
    			}
    		}
    	}
	
    	// check accuracy
    	int accuracyCount = 0;
    	for (int k = 0; k < coveredTupleList.size(); k++) {
    		if ((coveredTupleList.get(k)).getLast().toString().equals(classTag)) {
    			accuracyCount++;
    		}
    	}
    	
    	Float accuracy = (float) accuracyCount / coveredTupleList.size();
    	
    	outputRule(accuracy);
	}
	
	
	private void outputRule(Float accuracy) {
		System.out.println("Accuracy for above rule is : " + accuracy);
		// reformat rule string
		String classifier = "\nIf " + ruleSet + " \n..then it belongs to class " + classTag;
		String measures = "The accuracy for this rule is : " + accuracy.toString() + " \n";
		
		// write rules into a text file
		 try {
			 
			 File file = new File("classifier.txt");

	    		// create file if not exist
	    		if(!file.exists()){
	    			file.createNewFile();
	    		}

	    		FileWriter fileWritter = new FileWriter(file.getName(),true);
	    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	    	        bufferWritter.write(classifier + "\n" + measures);
	    	        bufferWritter.close();

		        System.out.println("Done");

		 } catch(IOException e) {
			 e.printStackTrace();
		 }
	}
	
}

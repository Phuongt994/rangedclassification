package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Monitor {
	private String classTag;
	private LinkedList<LinkedList> allTuple;
	private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap;
	private LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap;
	private String ruleSet;
	private LinkedList<LinkedList> coveredTupleList;
	
	public Monitor (String classTag, LinkedList<LinkedList> allTuple, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
		this.classTag = classTag;
		this.allTuple = allTuple;
		this.attributeRangeMap = attributeRangeMap;
		this.attributeTupleMap = attributeTupleMap;
		
		evaluateRule();
	}
	
	private void evaluateRule() {
		ruleSet = new String();
		
		coveredTupleList = new LinkedList<>(allTuple);
    	
    	for (Object key : attributeRangeMap.keySet()) {
    		
    		LinkedList<Integer> attributeNumberList = new LinkedList<>((LinkedList<Integer>) key);
    		
    		for (int i = 0; i < attributeNumberList.size(); i++) {
    			int attributeNumber = attributeNumberList.get(i);
    			
    			System.out.println("Attribute " + attributeNumber + " has range");
    			Float[] range = attributeRangeMap.get(key).get(0).get(i);
    			
    			// swap ranges in case [a,b] where b > a
    			if (range[0] > range[1]) {
    				Float temp = range[0];
    				range[0] = range[1];
    				range[1] = temp;
    			}
    			System.out.println(Arrays.toString(range));
    			
    			// record rule as string
    			String aRule = " \n\tattribute " + (Integer.toString(attributeNumber)) + " is within range " + Arrays.toString(range) + " ";
    			ruleSet = ruleSet + aRule;
    			
    			Iterator<LinkedList> iterator = coveredTupleList.iterator();

    			while (iterator.hasNext()) {
    			    LinkedList tuple = iterator.next();
    			    
    			    if (range[0] > (Float) tuple.get(attributeNumber) || (Float) tuple.get(attributeNumber) > range[1]) {
    			        iterator.remove();
    			    }
    			}
    		}
    	}
	
    	outputRule();
	}
	
	
	private void outputRule() {
		// reformat rule string
		String classifier = "\nIf " + ruleSet + " \n..then it belongs to class " + classTag;
		
		// write rules into a text file
		 try {
			 
			 File file = new File("classifier.txt");

	    		// create file if not exist
	    		if(!file.exists()){
	    			file.createNewFile();
	    		}

	    		FileWriter fileWritter = new FileWriter(file.getName(),true);
	    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	    	        bufferWritter.write(classifier);
	    	        bufferWritter.close();

		 } catch(IOException e) {
			 e.printStackTrace();
		 }
	}
	
}

package Tools;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class AnalyserGen extends Analyser {
	private LinkedHashMap<Integer, LinkedList<LinkedList>> allAttributeMap;
	private LinkedList<LinkedList<Integer>> aCombinedKey;
	private LinkedList<int[]> aCombinedRange;
	private String classTag;
	
	public AnalyserGen(LinkedList<LinkedList> allTuple, HashMap allClassMap, LinkedList<LinkedList<Integer>> aCombinedKey, LinkedList<int[]> aCombinedRange, String classTag) {
		super(allTuple, allClassMap);
		// TODO Auto-generated constructor stub
		this.allAttributeMap = getAllAttributeMap();
		this.aCombinedKey = aCombinedKey;
		this.aCombinedRange = aCombinedRange; // this only concerns 2 ranges: int[] R1 and int[] R2 or however big this loop is
		this.classTag = classTag;
		System.out.println("adjustRange started?");
		adjustRange(this.aCombinedKey, this.aCombinedRange);
	}
	
	/***
	 * get tuples within cRange within cKey
	 * check which tuple lies in both ranges
	 * those are not will be eliminated!!
	 */
	private void adjustRange(LinkedList<LinkedList<Integer>> aCombinedKey, LinkedList<int[]> aCombinedRange) {
		// could get a loop through each 
		// but wont cope well with multiple attributes
		// so a HashSet will do
		
		
		LinkedHashSet allCommonTupleSet = new LinkedHashSet<LinkedList>();
		for (int i = 0; i < aCombinedRange.size(); i++) {
			LinkedList<Integer> aKey = aCombinedKey.get(i);
			int[] aRange = aCombinedRange.get(i);	
			for (int j = aRange[0]; j < aRange[i]+1; j++) {
				allCommonTupleSet.add(allAttributeMap.get(aKey).get(j));
//				for (Object key : allAttributeMap.keySet()) {
//					int mapKey = (int) key;
//					if (aKey == mapKey) {
//						allCommonTupleSet.add(allAttributeMap.get(key).get(j));
//					}
//				}
			}
		}
		
		System.out.println("binaryConvert overriden started");
		binaryConvert(allCommonTupleSet);
		// send hashset to binaryConvert
		// override binaryConvert
		
	}
	
	private void binaryConvert(LinkedHashSet<LinkedList<LinkedList>> allCommonTupleSet) {
		// change hashset to linkedlist for easier execution
		LinkedList<LinkedList> allCommonTupleList = new LinkedList<LinkedList>(allCommonTupleSet);
		// prepare binary list
		LinkedList<Integer> binaryList = new LinkedList<>();
		for (LinkedList<LinkedList> tuple : allCommonTupleList) {
			if (tuple.getLast().equals(classTag)) {
				binaryList.add(1);
			} else {
				binaryList.add(-1);
			}
		}
		
		// don't know what to do with LR yet..
		LinkedHashMap<LinkedList<Integer>, LinkedList<int[]>> LR = new LinkedHashMap<>();
		LinkedHashSet<Integer> keyS = new LinkedHashSet<>();
		for (LinkedList<Integer> k : aCombinedKey) {
			for (Integer keyInt : k) {
				keyS.add(keyInt);
			}
		}
		
		LinkedList<Integer> keyLL = new LinkedList<>(keyS);
		//maxSum(keyLL, binaryList, LR);
	}
	
	private void checkThresh() {
		System.out.println("Replaced");
	}
	
}
package Tools;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class AnalyserGen extends Analyser {
	private LinkedHashMap<Integer, LinkedList<LinkedList>> allAttributeMap;
	private LinkedList<Integer> aCombinedKey;
	private LinkedList<int[]> aCombinedRange;
	private String classTag;
	
	public AnalyserGen(LinkedList<LinkedList> allTuple, HashMap allClassMap, LinkedList<Integer> aCombinedKey, LinkedList<int[]> aCombinedRange, String classTag) {
		super(allTuple, allClassMap);
		// TODO Auto-generated constructor stub
		this.allAttributeMap = getAllAttributeMap();
		this.aCombinedKey = aCombinedKey;
		this.aCombinedRange = aCombinedRange; // this only concerns 2 ranges: int[] R1 and int[] R2 or however big this loop is
		this.classTag = classTag;
	}
	
	/***
	 * get tuples within cRange within cKey
	 * check which tuple lies in both ranges
	 * those are not will be eliminated!!
	 */
	private void adjustRange() {
		// could get a loop through each 
		// but wont cope well with multiple attributes
		// so a HashSet will do
		LinkedHashSet allCommonTupleSet = new LinkedHashSet<LinkedList>();
		for (int i = 0; i < aCombinedRange.size(); i++) {
			int aKey = aCombinedKey.get(i);
			int[] aRange = aCombinedRange.get(i);	
			for (int j = aRange[0]; j < aRange[i]+1; j++) {
				int count = aKey;
				for (Object key : allAttributeMap.keySet()) {
					int mapKey = (int) key;
					if (aKey == mapKey) {
						allCommonTupleSet.add(allAttributeMap.get(key).get(j));
					}
				}
			}
		}
		
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
		maxSum(aCombinedKey, binaryList, null);
	}
	
	
}

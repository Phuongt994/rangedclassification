package Tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class AnalyserGen extends Analyser {
	private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList>> allAttributeMap;
	private LinkedList<LinkedList<Integer>> aCombinedKey;
	private LinkedList<int[]> aCombinedRange;
	private String classTag;
	
	public AnalyserGen(LinkedList<LinkedList> allTuple, HashMap<String, LinkedList<LinkedList>> allClassMap, LinkedList<LinkedList<Integer>> aCombinedKey, LinkedList<int[]> aCombinedRange, String classTag, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList>> allAttributeMap) {
		super(allTuple, allClassMap);
		// TODO Auto-generated constructor stub
		this.allAttributeMap = allAttributeMap;
		this.aCombinedKey = aCombinedKey;
		this.aCombinedRange = aCombinedRange; // this only concerns 2 ranges: int[] R1 and int[] R2 or however big this loop is
		this.classTag = classTag;
		System.out.println("\nadjustRange started");
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
		
		// HashSet to get all combined tuples
		
		LinkedHashSet<LinkedList> aCombinedTupleSet = new LinkedHashSet<>();
		LinkedHashSet<LinkedList> aMutualTupleSet = new LinkedHashSet<>();
		
		for (int i = 0; i < 2; i++) {
			LinkedList<Integer> aKey = aCombinedKey.get(i);
			
			System.out.println("aKey val: " + aKey);

			int[] aRange = aCombinedRange.get(i);
			System.out.println("aKey range: " + Arrays.toString(aRange));
			for (int j = aRange[0]; j <= aRange[1]; j++) {
				if (i == 0) {
					aCombinedTupleSet.add(allAttributeMap.get(aKey).get(j));
				} else {
					if (aCombinedTupleSet.contains(allAttributeMap.get(aKey).get(j))) {
						aMutualTupleSet.add(allAttributeMap.get(aKey).get(j));
					} 
					aCombinedTupleSet.add(allAttributeMap.get(aKey).get(j));
				}
			}
		}

		
		System.out.println("aCombinedTupleSet" + aCombinedTupleSet);
		System.out.println("aMutualTupleSet" + aMutualTupleSet);
		
//		boolean densityChecker = densityCheck(aMutualTupleSet, aCombinedTupleSet);
//		if (densityChecker == true) {
//			System.out.println("Range accepted for density, proceed to binaryConvert");
			// binaryConvert(aCombinedTupleSet);
//		} 		
	}
	
	private boolean densityCheck(LinkedHashSet<LinkedList> aMutualTupleSet, LinkedHashSet<LinkedList> aCombinedTupleSet) {		
		Float density = (float) aMutualTupleSet.size() /  aCombinedTupleSet.size();
		System.out.println("Density value : " + density);
		
		if (density >= 0.6) {
			System.out.println("Accepted");
			return true;
		} else {
			return false;
		}
	}
	
	/***
	 * ERROR-FILLED PART
	 * Which one to use for binaryList, alltuple or commontuple?
	 * @param aCombinedTupleSet
	 */
	private void binaryConvert(LinkedHashSet<LinkedList> aCombinedTupleSet) {
		// change hashset to linkedlist for easier execution
	
		// LinkedList<LinkedList> allCommonTupleList = new LinkedList<LinkedList>(aCombinedTupleSet);
	
		// prepare binary list
		LinkedList<Integer> binaryList = new LinkedList<>();
		
		for (LinkedList tuple : aCombinedTupleSet) {
			if ((tuple.getLast()).equals(classTag)) {
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
		System.out.println("maxSum 2nd round started");
		maxSum(keyLL, binaryList, LR);
	}
	
	private void checkThresh() {
		System.out.println("Replaced");
	}
	
}
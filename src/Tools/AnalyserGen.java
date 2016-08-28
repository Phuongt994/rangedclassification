package Tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class AnalyserGen extends Analyser {
	private HashMap<String, LinkedList<LinkedList>> allClassMap;
	private LinkedList<LinkedList> allTuple;
	private String classTag;
	private LinkedList<LinkedList<Integer>> aCombinedKey;
	private LinkedList<Float[]> aCombinedRange;
	private LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap;
	private LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap;
	
	
	public AnalyserGen(HashMap<String, LinkedList<LinkedList>> allClassMap,	 LinkedList<LinkedList> allTuple, String classTag, LinkedList<LinkedList<Integer>> aCombinedKey, LinkedList<Float[]> aCombinedRange, LinkedHashMap<LinkedList<Integer>, LinkedList<Float[]>> attributeRangeMap, LinkedHashMap<LinkedList<Float[]>, LinkedList<LinkedList>> attributeTupleMap) {
		super(allTuple, allClassMap);
		// TODO Auto-generated constructor stub
		this.classTag = classTag;
		this.aCombinedKey = aCombinedKey;
		this.aCombinedRange = aCombinedRange;
		this.attributeRangeMap = attributeRangeMap;
		this.attributeTupleMap = attributeTupleMap;
		
		// adjustRange(this.aCombinedKey, this.aCombinedRange);
	}
}
	
//	/***
//	 * get tuples within cRange within cKey
//	 * check which tuple lies in both ranges
//	 * those are not will be eliminated!!
//	 */
//	private void adjustRange(LinkedList<LinkedList<Integer>> aCombinedKey, LinkedList<int[]> aCombinedRange) {
//		// could get a loop through each 
//		// but wont cope well with multiple attributes
//		// so a HashSet will do
//		
//		// HashSet to get all combined tuples
//		
//		LinkedHashSet<LinkedList> aCombinedTupleSet = new LinkedHashSet<>();
//		LinkedHashSet<LinkedList> aMutualTupleSet = new LinkedHashSet<>();
//		
//		for (int i = 0; i < 2; i++) {
//			LinkedList<Integer> aKey = aCombinedKey.get(i);
//
//			System.out.println("aKey val: " + aKey);
//
////			int[] aRange = aCombinedRange.get(i);
////			System.out.println("aKey range: " + Arrays.toString(aRange));
////			for (int j = aRange[0]; j <= aRange[1]; j++) {
////				if (i == 0) {
////					aCombinedTupleSet.add(allAttributeMap.get(aKey).get(j));
////				} else {
////					if (aCombinedTupleSet.contains(allAttributeMap.get(aKey).get(j))) {
////						aMutualTupleSet.add(allAttributeMap.get(aKey).get(j));
////					} else {
////						aCombinedTupleSet.add(allAttributeMap.get(aKey).get(j));
////					}
////				}
////			}
////		}
//
//		
////		System.out.println("aCombinedTupleSet" + aCombinedTupleSet);
////		System.out.println("aMutualTupleSet" + aMutualTupleSet);
////		
////		boolean densityChecker = densityCheck(aMutualTupleSet, aCombinedTupleSet);
////		if (densityChecker == true) {
////			binaryConvert(aMutualTupleSet);
////		} else {
////			System.out.println("Rejected");
////		}
////	}
////	
////	private boolean densityCheck(LinkedHashSet<LinkedList> aMutualTupleSet, LinkedHashSet<LinkedList> aCombinedTupleSet) {		
////		Float density = (float) aMutualTupleSet.size() /  aCombinedTupleSet.size();
////		System.out.println("Density value : " + density);
////		
////		if (density >= 0.2) {
////			System.out.println("Accepted");
////			rejectedRange = false;
////			return true;
////		} else {
////			rejectedRange = true;
////			return false;
////		}
////	}
//	
//	/***
//	 * ERROR-FILLED PART
//	 * Use mutualTupleSet
//	 * will use a new AttributeMap
//	 * @param aCombinedTupleSet
//	 */
//	private void binaryConvert(LinkedHashSet<LinkedList> aMutualTupleSet) {
//		// change hashset to linkedlist for easier execution
//	
//		// LinkedList<LinkedList> allCommonTupleList = new LinkedList<LinkedList>(aCombinedTupleSet);
//	
//		// prepare binary list
//		LinkedList<Integer> binaryList = new LinkedList<>();
//		
//		for (LinkedList tuple : aMutualTupleSet) {
//			if ((tuple.getLast()).equals(classTag)) {
//				binaryList.add(1);
//			} else {
//				binaryList.add(-1);
//			}
//		}
//		
//
//		// What's LR for?
//		LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<int[]>>> LR = new LinkedHashMap<>();
//		LinkedHashSet<Integer> keyS = new LinkedHashSet<>();
//		for (LinkedList<Integer> k : aCombinedKey) {
//			for (Integer keyInt : k) {
//				keyS.add(keyInt);
//			}
////		}
////		
////		LinkedList<Integer> keyLL = new LinkedList<>(keyS);
////		System.out.println("maxSum 2nd round started");
////		LinkedList<int[]> allPosition = new LinkedList<>();
////		allPosition = maxSum(keyLL, binaryList, LR);
////		
////		// attributeNumberList can't be null : need to change key type to attr no list type
////		// temp list for now
////		LinkedList<Integer> attributeNumberList = new LinkedList<>();
////		attributeNumberList.add(aCombinedKey.get(0).get(0));
////		attributeNumberList.add(aCombinedKey.get(1).get(0));
////		
////		thresholdCheck(allPosition, binaryList, attributeNumberList, LR);
////	}
//	
//
//		}
//	}
//}

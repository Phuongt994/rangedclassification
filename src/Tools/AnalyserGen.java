package Tools;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

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
		this.classTag = classTag;
		this.aCombinedKey = aCombinedKey;
		this.aCombinedRange = aCombinedRange;
		this.attributeRangeMap = attributeRangeMap;
		this.attributeTupleMap = attributeTupleMap;
		
		preset();
	}

	private void preset() {
		
		LinkedHashSet<LinkedList> aCombinedTupleSet = new LinkedHashSet<>();
		LinkedList<LinkedList> aMutualTupleList = new LinkedList<>();
		
		for (int i = 0; i < 2; i++) {
			LinkedList<Integer> aKey = aCombinedKey.get(i);
			
			Float[] aRange = aCombinedRange.get(i);
			
			LinkedList<Float[]> aRangeList = new LinkedList<>();
			aRangeList.add(aRange);
			
			for (LinkedList<LinkedList> tuple : attributeTupleMap.get(aRangeList)) {
				if (i == 0) {
					aCombinedTupleSet.add(tuple);
				} else {
					if (aCombinedTupleSet.contains(tuple)) {
						aMutualTupleList.add(tuple);
					}
					aCombinedTupleSet.add(tuple);
				}
			}
		}
		
		densityCheck(aCombinedTupleSet, aMutualTupleList);
	}

	private void densityCheck(LinkedHashSet<LinkedList> aCombinedTupleSet, LinkedList<LinkedList> aMutualTupleList) {
		
		Float density = (float) aMutualTupleList.size() / aCombinedTupleSet.size();
		
		if (density >= 0.2) {
			System.out.println("Density accepted");
			
			// prepare new combined key
			LinkedList<Integer> aCombinedKeyList = new LinkedList<>();
			aCombinedKeyList.add(aCombinedKey.getFirst().get(0));
			aCombinedKeyList.add(aCombinedKey.getLast().get(0));
			
			// append to a NEW range and tuple map
			attributeRangeMap.clear();
			attributeTupleMap.clear();
			attributeRangeMap.put(aCombinedKeyList, aCombinedRange);
			attributeTupleMap.put(aCombinedRange, aMutualTupleList);
			
			System.out.println(attributeRangeMap);
			System.out.println(attributeTupleMap);
			
			// proceed to binaryConvert()
			binaryConvert(classTag, attributeRangeMap, attributeTupleMap);
			
		} else {
			System.out.println("Density rejected");
		}
	}
}



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

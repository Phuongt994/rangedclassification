package Tools;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Analyser2 extends Analyser1 {
	private HashMap<String, LinkedList<LinkedList>> allClassMap;
	private LinkedList<LinkedList> allTuple;
	private String classTag;
	private LinkedList<LinkedList<Integer>> aCombinedKey;
	private LinkedList<LinkedList<LinkedList<Float[]>>> aCombinedRange;
	private LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap;
	private LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap;
	private LinkedList<Integer> newCombinedKey;
	private LinkedList<LinkedList> aMutualTupleList;
	private LinkedList<LinkedList<Float[]>> newCombinedRange;
		
	
	public Analyser2(HashMap<String, LinkedList<LinkedList>> allClassMap,	 LinkedList<LinkedList> allTuple, String classTag, LinkedList<LinkedList<Integer>> aCombinedKey, LinkedList<LinkedList<LinkedList<Float[]>>> aCombinedRange, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
		super(allTuple, allClassMap);
		this.classTag = classTag;
		this.aCombinedKey = aCombinedKey;
		this.aCombinedRange = aCombinedRange;
		this.attributeRangeMap = attributeRangeMap;
		this.attributeTupleMap = attributeTupleMap;
		
		preset();
	}

	private void preset() {
		// move null tuple check to here
		//
		
		LinkedHashSet<LinkedList> aCombinedTupleSet = new LinkedHashSet<>();
		aMutualTupleList = new LinkedList<>();
			
		for (int i = 0; i < aCombinedKey.size(); i++) {
			LinkedList<Integer> aKey = aCombinedKey.get(i);

			LinkedList<LinkedList<Float[]>> aRange = aCombinedRange.get(i);

			for (LinkedList<LinkedList> tuple : attributeTupleMap.get(aRange)) {
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
			if (aCombinedKey.get(0).size() < 2) {
				// prepare new combined key
				newCombinedKey = new LinkedList<>();
				newCombinedKey.addAll(aCombinedKey.getFirst());
				newCombinedKey.addAll(aCombinedKey.getLast());
			} else {
				// for 2nd+ iteration
				LinkedHashSet<Integer> combinedKeySet = new LinkedHashSet<>();
				for (LinkedList<Integer> key : aCombinedKey) {
					combinedKeySet.addAll(key);
				}
				newCombinedKey = new LinkedList<>();
				newCombinedKey.addAll(combinedKeySet);
			}
			
			
			// prepare new combined range
			newCombinedRange = new LinkedList<>();
			newCombinedRange = aCombinedRange.getFirst();
			newCombinedRange.addAll(aCombinedRange.getLast());
			
			// append to a NEW range and tuple map
			attributeRangeMap.clear();
			attributeTupleMap.clear();
			attributeRangeMap.put(newCombinedKey, newCombinedRange);
			attributeTupleMap.put(newCombinedRange, aMutualTupleList);
			
			System.out.println(attributeRangeMap);
			System.out.println(attributeTupleMap);
			
			// proceed to binaryConvert()
			binaryConvert(classTag, attributeRangeMap, attributeTupleMap);
			
			// after all is done
			newCombinedRange = new LinkedList<>(getAttributeRangeList());
			System.out.println(newCombinedRange);
			
		} else {
			System.out.println("Density rejected");
		}
	}
	
	protected LinkedList<LinkedList<Float[]>> getNewCombinedRange() {
		return newCombinedRange;
	}
	
	protected LinkedList<Integer> getNewCombinedKey() {
		return newCombinedKey;
	}
	
	protected LinkedList<LinkedList> getNewMutualTupleList() {
		return aMutualTupleList;
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

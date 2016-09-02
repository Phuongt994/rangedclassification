package tools;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class SubAnalyser extends Analyser {
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
		
	
	public SubAnalyser(HashMap<String, LinkedList<LinkedList>> allClassMap,	 LinkedList<LinkedList> allTuple, String classTag, LinkedList<LinkedList<Integer>> aCombinedKey, LinkedList<LinkedList<LinkedList<Float[]>>> aCombinedRange, LinkedHashMap<LinkedList<Integer>, LinkedList<LinkedList<Float[]>>> attributeRangeMap, LinkedHashMap<LinkedList<LinkedList<Float[]>>, LinkedList<LinkedList>> attributeTupleMap) {
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

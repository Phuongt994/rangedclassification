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
	private LinkedHashSet<LinkedList> aCombinedTupleSet;
	private LinkedList<LinkedList> aMutualTupleList;

		
	
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
		aCombinedTupleSet = new LinkedHashSet<>();
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
	}

	protected LinkedHashSet<LinkedList> getNewCombinedTupleSet() {
		return aCombinedTupleSet;
	}
	
	protected LinkedList<LinkedList> getNewMutualTupleList() {
		return aMutualTupleList;
	}

}


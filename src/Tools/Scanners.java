package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by phuongt994 on 17/06/2016.
 */
public class Scanners {
	/***
	 * dD data directory
	 * sc1 scanner1
	 * sc2 scanner2
	 * Tuple all tuples
	 * C all classes
	 * cM class map
	 * t one tuple
	 */
	private LinkedList<String> aTuple;
    private LinkedList<LinkedList> allTuple;
    private HashSet<String> allClass;
    private HashMap<String, LinkedList<LinkedList>> allClassMap;


    public Scanners() {
        scanData();
    }

    private void scanData () {
        // specify data directory
        String dataDirectory = "wine.csv";

        // initialise aTuple list and class set
        allTuple = new LinkedList<>();
        allClass = new HashSet<>();

        // scanner to read data
        // read each line
        // use delimiter to separate items and append to a tuple
        // add each tuple to the tuple list
        // tuple list can now retrieve many sublists of tuples where elements in each tuple can be queried
        try {
            Scanner scanner1 = new Scanner(new File(dataDirectory));
            while (scanner1.hasNext()) {
                String str = scanner1.nextLine();
                Scanner scanner2 = new Scanner(str);
                scanner2.useDelimiter(",");
                aTuple = new LinkedList<>();
                while (scanner2.hasNext()) {
                    aTuple.add(scanner2.next());
                }
                allTuple.add(aTuple);
            }

            // process tuple elements (for class tag and ordered number)
            for (int i = 0; i < allTuple.size(); i++) {
                allTuple.get(i).addFirst(i);
                allClass.add(allTuple.get(i).getLast().toString());
                // change string attribute values to float
                for (int j = 1; j < aTuple.size(); j++) {
                    allTuple.get(i).set(j, Float.parseFloat(allTuple.get(i).get(j).toString()));
                }
            }
           
            sortData();

            reportScan();
            
            // Pass tuples and hashmap to Analyser
            new Analyser(allTuple, allClassMap);
            
        }
        catch (FileNotFoundException fe) {
            System.out.println(fe);
        }  
    }

    private void sortData() {
        // make a HashMap for class tags and (processed) tuples
        // initialise HashMap
        allClassMap = new HashMap<>();

        allClass.stream().forEach(c -> {
            allClassMap.put(c, null);
        });

        // for each class as key in Map
        allClassMap.keySet().stream().forEach(k -> {
            LinkedList temp = new LinkedList<LinkedList>();
            // for each tuple
            for (int i = 0; i < allTuple.size(); i++) {
                // if tuple's class == class name
                // add to temp tuple list
                if (allTuple.get(i).getLast().equals(k)) {
                    temp.add(allTuple.get(i));
                }
                // append temp list to relevant class key in Map
                allClassMap.put(k, temp);
            }
        });
    }

    private void reportScan() {
        System.out.println("Scanner started \nTupleuple list ready: " + allTuple);
        System.out.println("Class list ready: " + allClass);
        System.out.println("Hashmap ready: " + allClassMap.entrySet());
    }

}

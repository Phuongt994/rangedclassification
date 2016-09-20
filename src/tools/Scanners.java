package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Scanners {
    public static LinkedList<LinkedList> allTuple;
    public static HashMap<String, LinkedList<LinkedList>> allClassMap;


    public Scanners() {
        scanData();
    }

    private void scanData () {
        // specify data directory
        String dataDirectory = "wine.csv";

        // initialise aTuple list and class set
        allTuple = new LinkedList<>();
        HashSet<String> allClass = new HashSet<>();

        // scanner to read data
        // use delimiter to separate items and append to a tuple
        try {
            Scanner scanner1 = new Scanner(new File(dataDirectory));
            while (scanner1.hasNext()) {
                String str = scanner1.nextLine();
                Scanner scanner2 = new Scanner(str);
                scanner2.useDelimiter(",");
                LinkedList<String> aTuple = new LinkedList<>();
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
                for (int j = 1; j < allTuple.getFirst().size() - 1; j++) {
                    allTuple.get(i).set(j, Float.parseFloat(allTuple.get(i).get(j).toString()));
                }
            }
           
            sortData(allClass);
            
            // Pass tuples and hashmap to Analyser
            Analyser analyser = new Analyser();
            analyser.maxMin();
            
        }
        catch (FileNotFoundException fe) {
            System.out.println(fe);
        }  
    }

    private void sortData(HashSet<String> allClass) {
        // make a HashMap for class tags and (processed) tuples
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
                
                allClassMap.put(k, temp);
            }
        });
    }

}

package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by phuongt994 on 17/06/2016.
 */
public class Scanners {
    private String dataDir;
    private Scanner scanner1, scanner2;
    private LinkedList<String> t;
    private LinkedList<LinkedList> tuple;
    private HashSet<String> classes;
    private HashMap<String, LinkedList<LinkedList>> classMap;


    public Scanners() {
        processData();
    }

    private void processData() {
        // specify data directory
        dataDir = "iris.csv";

        //initialise tuple list and class set
        tuple = new LinkedList<>();
        classes = new HashSet<>();

        // scanner to read data
        // read each line
        // use delimiter to separate items and append to a tuple
        // add each tuple to the tuple list
        // tuple list can now retrieve many sublists of tuples where elements in each tuple can be queried
        try {
            scanner1 = new Scanner(new File(dataDir));
            while (scanner1.hasNext()) {
                String str = scanner1.nextLine();
                scanner2 = new Scanner(str);
                scanner2.useDelimiter(",");
                t = new LinkedList<>();
                while (scanner2.hasNext()) {
                    t.add(scanner2.next());
                }
                tuple.add(t);
            }

            // process tuple elements (for class tag and ordered number)
            for (int i = 0; i < tuple.size(); i++) {
                tuple.get(i).add(0, i);
                classes.add(tuple.get(i).get(5).toString());
                // change string attribute values to float
                for (int j = 1; j < 5; j++) {
                    tuple.get(i).set(j, Float.parseFloat(tuple.get(i).get(j).toString()));
                }
            }

            sortData();

            scanner1.close();
            scanner2.close();

            reportScan();
            // Pass hashmap into Analyser?
            new Analyser(tuple, classMap);

            // pass tuple and class tags to analyser
            // new Sorter(tuple, classes);
        }
        catch (FileNotFoundException fe) {
            System.out.println(fe);
        }  
    }

    private void sortData() {
        // make a HashMap for class tags and (processed) tuples
        // initialise HashMap
        classMap = new HashMap<>();

        classes.stream().forEach(c -> {
            classMap.put(c, null);
        });

        // for each class as key in Map
        classMap.keySet().stream().forEach(k -> {
            LinkedList temp = new LinkedList<List>();
            // for each tuple
            for (int i = 0; i < tuple.size(); i++) {
//                int order = i;
                // if tuple's class == class name
                // add to temp tuple list
                if (tuple.get(i).get(5).equals(k)) {
                    temp.add(tuple.get(i));
                }
                // append temp list to relevant class key in Map
                classMap.put(k, temp);
            }
        });
    }

    private void reportScan() {
        System.out.println("Scanner started \nTuple list ready: " + tuple);
        System.out.println("Class list ready: " + classes);
        System.out.println("Hashmap ready: " + classMap.entrySet());
    }

}

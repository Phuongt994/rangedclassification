package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by phuongt994 on 17/06/2016.
 */
public class DScanner {
    private String dataDir;
    private Scanner scanner1, scanner2;
    private ArrayList<String> t,a1,a2,a3,a4,c;
    private Analyser analyser;


    public DScanner() {
        // call process data method in this class
        processData();
        // initiate AR class with params - AR will invoke AR methods in itself
        analyser = new Analyser(a1,a2,a3,a4,c);

    }

    private void processData() {
        // specify data directory
        dataDir = "iris.csv";
        a1 = new ArrayList<>();
        a2 = new ArrayList<>();
        a3 = new ArrayList<>();
        a4 = new ArrayList<>();
        t = new ArrayList<>();
        c = new ArrayList<>();

        // scanner to read data
        try {
            // scanner take tuple of lines first (set t)
            scanner1 = new Scanner(new File(dataDir));
            while (scanner1.hasNext()) {
                String dataTuple = scanner1.nextLine();
                t.add(dataTuple);
            }

            // scanner now separate a1, a2, a3, a4, c into their relevant array (set a1234, c)
            for (int j = 0; j < t.size(); j++) {
                scanner2 = new Scanner(t.get(j));
                scanner2.useDelimiter(",");

                a1.add(scanner2.next());
                a2.add(scanner2.next());
                a3.add(scanner2.next());
                a4.add(scanner2.next());
                c.add(scanner2.next());
            }

            // scanner tag an ID for each line (set t)
            for (int i = 0; i < t.size(); i++) {
                t.set(i, i + "_" + t.get(i));
            }
        }
        catch (FileNotFoundException fe) {
            System.out.println(fe);
        }
        finally {
            scanner1.close();
            scanner2.close();
        }
    }
}

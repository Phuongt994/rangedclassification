import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by phuongt994 on 17/06/2016.
 */
public class DataReady {
    String dataDir;
    Scanner scanner1, scanner2;
    List<String> t,a1,a2,a3,a4,c;


    public DataReady() {
        processData();
    }

    private void processData() {
        // specify data directory
        dataDir = "iris.csv";
        a1 = new ArrayList<String>();
        a2 = new ArrayList<String>();
        a3 = new ArrayList<String>();
        a4 = new ArrayList<String>();
        t = new ArrayList<String>();
        c = new ArrayList<String>();

        // scanner to read data
        try {
            scanner1 = new Scanner(new File(dataDir));
            while (scanner1.hasNext()) {
                String dataTuple = scanner1.nextLine();
                t.add(dataTuple);
            }

            for (int j = 0; j < t.size(); j++) {
                scanner2 = new Scanner(t.get(j));
                scanner2.useDelimiter(",");

                a1.add(scanner2.next());
                a2.add(scanner2.next());
                a3.add(scanner2.next());
                a4.add(scanner2.next());
                c.add(scanner2.next());
            }

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

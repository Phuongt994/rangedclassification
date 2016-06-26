package Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by phuongt994 on 17/06/2016.
 */

public class Analyser {
    private ArrayList<String> a1,a2,a3,a4,c;
    private ArrayList<Float> minMax;
    private ArrayList<ArrayList<String>> listOfA;
    private HashMap<Integer, ArrayList<Float>> mapOfA;


    public Analyser(ArrayList<String> a1, ArrayList<String> a2, ArrayList<String> a3, ArrayList<String> a4, ArrayList<String> c) {
        listOfA = new ArrayList<>();
        listOfA.add(this.a1 = a1);
        listOfA.add(this.a2 = a2);
        listOfA.add(this.a3 = a3);
        listOfA.add(this.a4 = a4);
        this.c = c;

        // initiate map to store 1) attribute name and 2) attribute min-max value
        mapOfA = new HashMap<>();

        // call max-min method on each a(ttribute)
        listOfA.stream().forEach((lstr) -> { minMax(lstr, listOfA.indexOf(lstr) + 1); });

        // test output
        System.out.println(getMap());
    }

    private void minMax(ArrayList<String> a, int index) {
        // initiate maxMin array
        minMax = new ArrayList<>();

        // find min-max value for range
        Collections.sort(a);
        float min = Float.parseFloat(a.get(0));
        float max = Float.parseFloat(a.get(a.size() - 1));
        minMax.add(min);
        minMax.add(max);

        mapOfA.put(index, minMax);
    }

    private HashMap<Integer, ArrayList<Float>> getMap() {
        return mapOfA;
    }
}

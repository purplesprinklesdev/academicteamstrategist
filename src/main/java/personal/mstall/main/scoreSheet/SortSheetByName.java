package personal.mstall.main.scoreSheet;

import java.util.Comparator;

public class SortSheetByName implements Comparator<Sheet> {
    public int compare (Sheet a, Sheet b) {
        return a.name.compareTo(b.name);
    }
}

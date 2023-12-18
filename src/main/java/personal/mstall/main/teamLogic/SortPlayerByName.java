package personal.mstall.main.teamLogic;

import java.util.Comparator;

public class SortPlayerByName implements Comparator<Player> {
    public int compare (Player a, Player b) {
        String[] aNameArray = a.name.split(" ");
        String[] bNameArray = b.name.split(" ");

        int result = aNameArray[aNameArray.length-1].compareTo(bNameArray[bNameArray.length-1]);
        if (result == 0) {
            for (int i = 0; i < aNameArray.length-1; i++) {
                if (i == bNameArray.length-1)
                    result = -1;
                
                result = aNameArray[i].compareTo(bNameArray[i]);

                if (result != 0)
                    break;
            }
        }
        return result;
    }
}
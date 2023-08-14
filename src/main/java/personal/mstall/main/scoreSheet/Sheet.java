package personal.mstall.main.scoreSheet;

import java.util.ArrayList;

import personal.mstall.main.teamLogic.Player;
import personal.mstall.main.teamLogic.Roster;

public class Sheet {
    public String name;

    private ArrayList<String[]> sheet;

    public ArrayList<String[]> getSheet() {
        return sheet;
    }

    public String[] getPlayer(String name) {
        for(String[] row : sheet) {
            if (row[0].equals(name))
                return row;
        }
        return null;
    }

    public void setSheet(ArrayList<String[]> sheet) {
        this.sheet = sheet;
    }

    public Sheet() {
        this.name = "New Sheet";
        sheet = new ArrayList<>();
    
        for (Player player : Roster.roster.players) {
            String[] base = { "", "-", "-", "-", "-" };
            base[0] = player.name;
            sheet.add(base);
        }
    }

    public Sheet(String name, ArrayList<String[]> sheet) {
        this.name = name;
        this.sheet = sheet;
    }
}

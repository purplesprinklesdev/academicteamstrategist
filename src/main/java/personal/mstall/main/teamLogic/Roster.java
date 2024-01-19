package personal.mstall.main.teamLogic;
import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import personal.mstall.main.scoreSheet.ScoreSheets;
import personal.mstall.main.scoreSheet.Sheet;
import personal.mstall.main.util.FileType;
import personal.mstall.main.util.SaveManager;

@XmlRootElement
public class Roster {
    // Singleton
    @XmlTransient
    public static Roster roster;

    public ArrayList<Player> players;


    public Roster(ArrayList<Player> players) {
        if (roster != null)
            return;
        
        this.players = players;
        roster = this;
    }
    public Roster(Roster that) {
        if (roster != null)
            return;
        
        roster = that;
    }
    public Roster() {
        if (roster != null)
            return;
        
        this.players = new ArrayList<>();
        roster = this;
    }
    public static Roster loadFromFile() {
        if (roster != null)
            return roster;

        Object rosterObject = SaveManager.Load(FileType.ROSTER);
        
        if (rosterObject == null)
            new Roster();
        else
            new Roster((Roster) rosterObject);
        
        return roster;
    }

    public Player findPlayerWithName(String name) {
        if (name == "Empty Slot")
            return null;
        
        for (Player player : players) {
            if (player.name == name)
                return player;
        }
        
        System.out.println("Player with name \"" + name + "\" not found. This shouldn't happen!");
        return null;
    }

    public static void updatePlayerAverages() {
        final double[] totalQuestions = { 15, 15, 10, 10 };

        for (Player player : roster.players) {

            double[] totalGames = { 0, 0, 0, 0 };
            double[] correctAnswers = { 0, 0, 0, 0 };
            double[] averages = { 0, 0, 0, 0 };

            for (Sheet sheet : ScoreSheets.getSheets()) {
                String[] row = sheet.getPlayer(player.name);
                if (row == null) 
                    continue;
                    
                for (int i = 0; i < correctAnswers.length; i++) {
                    if (row[i + 1].equals("-"))
                        continue;

                    totalGames[i]++;
                    
                    correctAnswers[i] += Double.parseDouble(row[i + 1]);
                }
            }
            
            for (int i = 0; i < averages.length; i++) {
                if (totalGames[i] == 0)
                    averages[i] = 0;
                else
                    averages[i] = correctAnswers[i] / (totalQuestions[i] * totalGames[i]);
                
                if (i == 2) // Alphabet round - new calculation method
                    averages[i] /= 4;
            }
            
            player.setSectionAverages(averages);
        }
    }
}

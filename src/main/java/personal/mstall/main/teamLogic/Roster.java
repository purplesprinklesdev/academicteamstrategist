package personal.mstall.main.teamLogic;
import java.util.ArrayList;
import java.util.Arrays;

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
        for (Player player : players) {
            if (player.name == name)
                return player;
        }
        
        System.out.println("Player with name \"" + name + "\" not found!");
        return null;
    }

    public static void updatePlayerAverages() {
        double[] totalQuestions = { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 10, 10 };
        
        for (Player player : roster.players) {
            double[] correctAnswers = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    
            for (Sheet sheet : ScoreSheets.scoreSheets.sheets) {
                String[] row = sheet.getPlayer(player.name);
                if (row == null) 
                    continue;
                    
                for (int i = 0; i < correctAnswers.length; i++) {
                    correctAnswers[i] += Double.parseDouble(row[i + 1]);
                }
            }
    
            double[] averages = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            for (int i = 0; i < averages.length; i++) {
                averages[i] = correctAnswers[i] / totalQuestions[i];
            }

            double[] sectionAverages = { 0, 0, 0, 0 };
            for (int section = 0; section < 4; section++) {
                double[] splitAverage = {};
                switch (section) {
                    case 0: 
                        splitAverage = Arrays.copyOfRange(averages, 0, 5); //   Section 1
                        break;
                    case 1:
                        splitAverage = Arrays.copyOfRange(averages, 5, 10); //  Section 2
                        break;
                    case 2:
                        splitAverage = Arrays.copyOfRange(averages, 10, 11); // Section 3
                        break;
                    case 3:
                        splitAverage = Arrays.copyOfRange(averages, 11, 12); // Section 4
                        break;
                }
    
                double total = 0;
                for (double n : splitAverage) {
                    total += n;
                }

                sectionAverages[section] = total / splitAverage.length;
            }
            
            player.setSectionAverages(sectionAverages);
        }
    }
}

package personal.mstall.main.teamLogic;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class TeamComp {
    // Singleton
    @XmlTransient
    public static TeamComp teamComp;

    public Team[] teams;
    
    public TeamComp(TeamComp that) {
        if (teamComp != null)
            return;
        
        teamComp = that;
    }
    public TeamComp() {
        if (teamComp != null)
            return;
        
        this.teams = new Team[8];
        teamComp = this;
    }

    public boolean generateNewComp() {
        ArrayList<Sheet> sheets = ScoreSheets.scoreSheets.sheets;
        ArrayList<Player> roster = Roster.roster.players;
        
        if (sheets.size() < 1 | roster.size() < 2)
            return false;

        System.out.println("CALCULATING AVERAGES...");

        for (Player player : roster) {
            int[] correctAnswers = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            int[] totalQuestions = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

            for (Sheet sheet : sheets) {
                String[] row = sheet.getPlayer(player.name);
                if (row == null)
                    continue;
                
                //  Initialize with 1 because row[0] is player name
                for (int = 1; i < row.length; i++) {
                    correctAnswers[i] += Integer.parseInt(row[i]);
                    totalQuestions[i] += i < 12 ? 3 : 10;
                }
            }

            double[] averages = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            for (int = 0; i < averages.length; i++) {
                if (totalQuestions[i] == 0)
                    continue;
                averages[i] = correctAnswers[i] / totalQuestions[i];
            }

            double[] sectionAverages = { 0, 0, 0, 0 };
            for (int section = 0; section < 4; section++) {
                double[] splitAverage;
                switch (section) {
                    case 0: 
                        splitAverage = Arrays.copyOfRange(averages, 0, 5);
                        break;
                    case 1:
                        splitAverage = Arrays.copyOfRange(averages, 5, 10);
                        break;
                    case 2:
                        splitAverage = Arrays.copyOfRange(averages, 10, 11);
                        break;
                    case 3:
                        splitAverage = Arrays.copyOfRange(averages, 11, 12);
                        break;
                }

                double total = 0;
                for (double n : splitAverage) {
                    total += n;
                }

                sectionAverages[section] = total;
            }
            player.setAverages(averages);
            player.setSectionAverages(sectionAverages);
        }

        System.out.println("RANKING PLAYERS...");
        //  Rank players by average in each section, create a group of the best 8 for each

        for (int section = 0; section < 4; section++) { //4 total sections of a half
            ArrayList<Player> fullTeam = new ArrayList<Player>();


        }
        

        //  Generate all possible combinations of a split for each section (8c4) * 4 = 280

        //  Remove illegal combinations

        //  Find highest scoring combination for the whole game
    }
}

package personal.mstall.main.teamLogic;

import java.util.ArrayList;
import java.util.HashSet;

import jakarta.xml.bind.annotation.*;
import personal.mstall.main.StrategistApp;

@XmlRootElement
public class TeamComp {

    public String name = "";

    public ArrayList<Team> teams;

    private static final double[] questionsPerSection = { 15, 15, 10, 10 };

    public TeamComp() {
        this.teams = new ArrayList<>();
        for (int half = 0; half < 2; half++) {
            for (int section = 0; section < 4; section++)
                this.teams.add(new Team(half, section));
        }
    }

    public TeamComp(TeamComp that) {
        this.teams = new ArrayList<>();
        for (Team team : that.teams) {
            teams.add(team);
        }
    }

    public Rating getRating() {
        if (teams.size() == 0)
            return null;

        

        double firstHalfScore = 0;
        double secondHalfScore = 0;

        for (int half = 0; half < 2; half++) {
            double answersInHalf = 0;

            for (int section = 0; section < 4; section++) {
                int i = section + (4 * half);
                Team team = teams.get(i);

                double answersInSection = 0;

                for (Player player : team.getPlayers()) {
                    double[] sectionAverages = player.getSectionAverages();
                    answersInSection += questionsPerSection[section] * sectionAverages[section];
                }

                if (answersInSection > questionsPerSection[section])
                    answersInSection = questionsPerSection[section];

                answersInHalf += answersInSection;
            }

            if (half == 0)
                firstHalfScore = answersInHalf;
            else
                secondHalfScore = answersInHalf;
        }
        
        return new Rating(Math.round((float) firstHalfScore), Math.round((float) secondHalfScore));
    }

    public static TeamComp getTeamCompFromChoiceBoxes() {
        TeamComp teamComp = new TeamComp();

        int i = 0;
        for(int teamNo = 0; teamNo < 8; teamNo++) {
            Team team = teamComp.teams.get(teamNo);
            HashSet<Player> players = new HashSet<>();

            for (int playerNo = 0; playerNo < 4; playerNo++) {
                String name = StrategistApp.allChoiceBoxes.get(i).getValue();
                Player player = null;

                if (name != "Empty Slot") {
                    player = Roster.roster.findPlayerWithName(name);

                    if (player != null) {
                        int sectionIndex = teamNo < 4 ? teamNo : teamNo - 4;
                        double labelValue = questionsPerSection[sectionIndex] * player.getSectionAverages()[sectionIndex];
                        StrategistApp.allChoiceBoxLabels.get(i).setText(String.valueOf(Math.round(labelValue)));
                        players.add(player);
                        i++;
                        continue;
                    }
                } else {
                    StrategistApp.allChoiceBoxLabels.get(i).setText("0");
                }
                players.add(new Player());
                i++;
            }
            team.setPlayers(players);
        }

        return teamComp;
    }

    public static void setChoiceBoxesWithTeamComp(TeamComp teamComp) {
        StrategistApp.ignoreChoiceBoxEvents = true;

        int i = 0;
        for(int teamNo = 0; teamNo < 8; teamNo++) {
            Team team = teamComp.teams.get(teamNo);

            for (Player player : team.getPlayers()) {
                StrategistApp.allChoiceBoxes.get(i).setValue(player.name);
                i++;
            }
        }
        StrategistApp.ignoreChoiceBoxEvents = false;
    }
}

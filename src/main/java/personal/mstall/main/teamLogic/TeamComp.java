package personal.mstall.main.teamLogic;

import java.util.ArrayList;
import java.util.HashSet;

import jakarta.xml.bind.annotation.*;
import javafx.scene.control.ChoiceBox;
import personal.mstall.main.StrategistApp;
import personal.mstall.main.util.FileType;
import personal.mstall.main.util.SaveManager;

@XmlRootElement
public class TeamComp {

    public ArrayList<Team> teams;

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

        double[] questionsPerSection = { 15, 15, 10, 10 };

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

    public static TeamComp getTeamCompFromChoiceBoxes(ArrayList<ChoiceBox<String>> allChoiceBoxes) {
        TeamComp teamComp = new TeamComp();

        int i = 0;
        for(int teamNo = 0; teamNo < 8; teamNo++) {
            Team team = teamComp.teams.get(teamNo);
            HashSet<Player> players = new HashSet<>();

            for (int playerNo = 0; playerNo < 4; playerNo++) {
                String name = allChoiceBoxes.get(i).getValue();
                Player player = null;

                i++;

                if (name != "Empty Slot") {
                    player = Roster.roster.findPlayerWithName(name);

                    if (player != null) {
                        players.add(player);
                        continue;
                    }
                }

                players.add(new Player());
            }

            team.setPlayers(players);
        }

        return teamComp;
    }

    public static void setChoiceBoxesWithTeamComp(ArrayList<ChoiceBox<String>> allChoiceBoxes, TeamComp teamComp) {
        StrategistApp.ignoreChoiceBoxEvents = true;
        int i = 0;
        for(int teamNo = 0; teamNo < 8; teamNo++) {
            Team team = teamComp.teams.get(teamNo);

            for (Player player : team.getPlayers()) {
                allChoiceBoxes.get(i).setValue(player.name);
                i++;
            }
        }
        StrategistApp.ignoreChoiceBoxEvents = false;
    }

    public static TeamComp loadFromFile() {
        Object teamCompObject = SaveManager.Load(FileType.TEAMCOMP);

        if (teamCompObject == null)
            return new TeamComp();
        else
            return new TeamComp((TeamComp) teamCompObject);
    }
}

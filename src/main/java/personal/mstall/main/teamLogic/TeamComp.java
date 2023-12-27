package personal.mstall.main.teamLogic;

import java.util.ArrayList;
import java.util.HashSet;

import jakarta.xml.bind.annotation.*;
import personal.mstall.main.StrategistApp;
import personal.mstall.main.util.Debug;

@XmlRootElement
public class TeamComp {

    public String name = "";

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
        long startTime = System.currentTimeMillis();
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
        Debug.PrintOut("FUNC TIMER: getRating took " + (System.currentTimeMillis() - startTime) + "ms");
        return new Rating(Math.round((float) firstHalfScore), Math.round((float) secondHalfScore));
    }

    public static TeamComp getTeamCompFromChoiceBoxes() {
        long startTime = System.currentTimeMillis();
        TeamComp teamComp = new TeamComp();

        int i = 0;
        for(int teamNo = 0; teamNo < 8; teamNo++) {
            Team team = teamComp.teams.get(teamNo);
            HashSet<Player> players = new HashSet<>();

            for (int playerNo = 0; playerNo < 4; playerNo++) {
                String name = StrategistApp.allChoiceBoxes.get(i).getValue();
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

        Debug.PrintOut("FUNC TIMER: getTeamCompFromChoiceBoxes took " + (System.currentTimeMillis() - startTime) + "ms");
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

package personal.mstall.main.util;

import personal.mstall.main.scoreSheet.ScoreSheets;
import personal.mstall.main.teamLogic.Roster;
import personal.mstall.main.teamLogic.TeamComp;

public enum FileType {
    ROSTER("roster", Roster.class),
    SCORESHEETS("scoresheets", ScoreSheets.class),
    TEAMCOMP("teamcomp", TeamComp.class)
    ;


    public final String filename;
    public final Class<?> clss;
    private FileType(String filename, Class<?> clss) {
        this.filename = filename;
        this.clss = clss;
    }
}

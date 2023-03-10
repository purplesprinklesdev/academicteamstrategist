package personal.mstall.main.util;

import personal.mstall.main.teamLogic.Roster;

public enum FileType {
    ROSTER("roster", Roster.class)
    ;


    public final String filename;
    public final Class<?> clss;
    private FileType(String filename, Class<?> clss) {
        this.filename = filename;
        this.clss = clss;
    }
}

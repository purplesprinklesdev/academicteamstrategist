package personal.mstall.main;

import personal.mstall.main.team.Roster;

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

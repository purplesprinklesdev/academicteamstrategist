package personal.mstall.main.teamLogic;

public class Team {
    private int half;
    private int section;
    private String playerNames;

    private Player[] players;

    public int getHalf() { return half; }
    public int getSection() { return section; }
    public boolean setSection(int half, int section) {
        if (half < 1 | half > 2)
            return false
        if (section < 1 | section > 4)
            return false;
        
        this.half = half;
        this.section = section;
    }
    public String getPlayerNames() { return playerNames; }
    public Player[] getPlayers() { return players; }
    public boolean setPlayers(Player[] players) {
        if (players.length != 4)
            return false;
        
        String names = "";
        for (Player player : players) {
            names += player.name + " ";
        }
        names.subString(0, names.length);
        playerNames = names;

        this.players = players;
    }

    public Team(int section, Player[] players) {
        if (section < 0 | section > 7)
            throw new Exception("Section is out of range! Must be 0-7, inclusive.");
        this.section = section;
        this.players = players;
    }
    public Team(int section) {
        if (section < 0 | section > 7)
            throw new Exception("Section is out of range! Must be 0-7, inclusive.");
        this.section = section;
        this.players = new Player[4];
    }
}

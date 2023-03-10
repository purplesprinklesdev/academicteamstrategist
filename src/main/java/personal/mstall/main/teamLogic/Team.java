package personal.mstall.main.teamLogic;

public class Team {
    private int type;
    public Player[] players;

    public int getType() { return type; }
    public void setType(int type) throws Exception {
        if (type < 0 | type > 7)
            throw new Exception("Type is out of range! Must be 0-7, inclusive.");
        this.type = type;
    }

    public Team(int type, Player[] players) throws Exception {
        if (type < 0 | type > 7)
            throw new Exception("Type is out of range! Must be 0-7, inclusive.");
        this.type = type;
        this.players = players;
    }
    public Team(int type) throws Exception {
        if (type < 0 | type > 7)
            throw new Exception("Type is out of range! Must be 0-7, inclusive.");
        this.type = type;
        this.players = new Player[4];
    }
}

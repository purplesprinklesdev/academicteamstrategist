package personal.mstall.main.teamLogic;

import java.util.HashSet;

import jakarta.xml.bind.annotation.*;

public class Team {
    @XmlAttribute
    private int half = 0;
    @XmlAttribute
    private int section = 0;
    @XmlElement
    private HashSet<Player> players;
    @XmlTransient
    private double[] sectionAverages;

    public int getHalf() {
        return half;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int half, int section) {
        if (half < 0 || half > 1) {
            half = 0;
            System.out.println("Half out of range!");
        }
        if (section < 0 || section > 3) {
            section = 0;
            System.out.println("Section out of range!");
        }

        this.half = half;
        this.section = section;
    }

    @XmlTransient
    public HashSet<Player> getPlayers() {
        return players;
    }

    public boolean setPlayers(HashSet<Player> players) {
        if (players.size() > 4)
            return false;

        this.players = players;
        return true;
    }

    public boolean addPlayer(Player player) {
        if (players != null && players.size() >= 4) 
            return false;

        this.players.add(player);
        return true;
    }

    public double getSectionAverage() {
        if (sectionAverages == null)
            updateSectionAverages();
        return sectionAverages[section];
    }
    public double getSectionAverage(int section) {
        if (sectionAverages == null)
            updateSectionAverages();
        return sectionAverages[section];
    }
    public double[] getSectionAverages() {
        if (sectionAverages == null)
            updateSectionAverages();
        return sectionAverages;
    }

    private void updateSectionAverages() {
        sectionAverages = new double[4];
        
        for (Player player : players) {
            double[] playerSectionAverages = player.getSectionAverages();
            for (int section = 0; section < 4; section++)
                sectionAverages[section] += playerSectionAverages[section];
        }

        for (int section = 0; section < 4; section++) {
            if (sectionAverages[section] > 1.0)
                sectionAverages[section] = 1.0;
        }
    }

    public Team(int half, int section) {
        this.players = new HashSet<>();
        setSection(half, section);

        for (int i = 0; i < 4; i++) {
            this.players.add(new Player());
        }
    }

    public Team() {
        this.players = new HashSet<>();
        setSection(0, 0);

        for (int i = 0; i < 4; i++) {
            this.players.add(new Player());
        }
    }

    public boolean equals(Team other) {
        return players.equals(other.getPlayers());
    }
}

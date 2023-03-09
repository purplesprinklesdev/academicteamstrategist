package personal.mstall.main.team;
import java.util.ArrayList;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Roster {
    // Singleton
    @XmlTransient
    public static Roster roster;

    public ArrayList<Player> players;


    public Roster(ArrayList<Player> players) {
        if (roster != null)
            return;
        
        this.players = players;
        roster = this;
    }
    public Roster(Roster that) {
        if (roster != null)
            return;
        
        roster = that;
    }
    public Roster() {
        if (roster != null)
            return;
        
        this.players = new ArrayList<>();
        roster = this;
    }
}

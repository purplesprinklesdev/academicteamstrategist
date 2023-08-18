package personal.mstall.main.teamLogic;
import java.util.ArrayList;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import personal.mstall.main.util.FileType;
import personal.mstall.main.util.SaveManager;

@XmlRootElement
public class TeamCompSaves {
    // Singleton
    @XmlTransient
    public static TeamCompSaves teamCompSaves;

    public ArrayList<TeamComp> comps;

    public TeamCompSaves(ArrayList<TeamComp> comps) {
        if (teamCompSaves != null)
            return;
        
        this.comps = comps;
        teamCompSaves = this;
    }
    public TeamCompSaves(TeamCompSaves that) {
        if (teamCompSaves != null)
            return;
        
        teamCompSaves = that;
    }
    public TeamCompSaves() {
        if (teamCompSaves != null)
            return;
        
        this.comps = new ArrayList<>();
        teamCompSaves = this;
    }
    public static TeamCompSaves loadFromFile() {
        if (teamCompSaves != null)
            return teamCompSaves;
        
        Object teamCompsObject = SaveManager.Load(FileType.TEAMCOMPS);

        if (teamCompsObject == null)
            new TeamCompSaves();
        else
            new TeamCompSaves((TeamCompSaves) teamCompsObject);

        return teamCompSaves;
    }
}

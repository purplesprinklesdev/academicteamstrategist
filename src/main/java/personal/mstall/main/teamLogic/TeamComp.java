package personal.mstall.main.teamLogic;

@XmlRootElement
public class TeamComp {
    // Singleton
    @XmlTransient
    public static TeamComp teamComp;

    public Team[] teams;
    
    public TeamComp(TeamComp that) {
        if (teamComp != null)
            return;
        
        teamComp = that;
    }
    public TeamComp() {
        if (teamComp != null)
            return;
        
        this.teams = new Team[8];
        teamComp = this;
    }

    public void generateNewComp() {
        //  Get Scoresheets

        //  For each player in the roster, calculate their average by checking every score sheet

        //  Make teams of 8 for each section, find highest score for each section

        //  Generate all possible combinations of a split for each section (8c4) * 4 = 280

        //  Remove illegal combinations

        //  Find highest scoring combination for the whole game
    }
}

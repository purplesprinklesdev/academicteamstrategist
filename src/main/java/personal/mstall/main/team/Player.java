package personal.mstall.main.team;

public class Player {
    public int index;
    public String name;
    private double[] averages = { 0,0,0,0,0,0,0,0,0,0,0,0 };
    
    public double[] getAverages() { return averages; }
    public double getAverage(int i) { return averages[i]; }

    

    public Player(int index, String name) {
        this.index = index;
        this.name = name;
    }
    public Player(int index, String name, double[] averages) {
        this.index = index;
        this.name = name;
        this.averages = averages;
    }
}

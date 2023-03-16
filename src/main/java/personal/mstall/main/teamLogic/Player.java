package personal.mstall.main.teamLogic;

public class Player {
    public int index;
    public String name;
    private double[] averages = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private double[] sectionAverages;

    public double[] getAverages() {
        return averages;
    }

    public double getAverage(int i) {
        return averages[i];
    }

    public void setAverages(double[] averages) {
        this.averages = averages;
    }

    public void setAverage(int i, double average) {
        averages[i] = average;
    }
    public double getSectionAverages() {
        return sectionAverages;
    }
    public void setSectionAverages(double[] sectionAverages) {
        this.sectionAverages = sectionAverages;
    }

    public Player() {
    }

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

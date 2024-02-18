package personal.mstall.main.teamLogic;

public class Rating {
    public double total;
    public double firstHalf;
    public double secondHalf;

    public Rating(int firstHalf, int secondHalf) {
        this.firstHalf = firstHalf;
        this.secondHalf = secondHalf;
        total = firstHalf + secondHalf;
    }

    public Rating(double firstHalf, double secondHalf) {
        this.firstHalf = firstHalf;
        this.secondHalf = secondHalf;
        total = firstHalf + secondHalf;
    }
}

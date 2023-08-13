package personal.mstall.main.teamLogic;

public class Rating {
    public int total;
    public int firstHalf;
    public int secondHalf;

    public Rating(int firstHalf, int secondHalf) {
        this.firstHalf = firstHalf;
        this.secondHalf = secondHalf;
        total = firstHalf + secondHalf;
    }
}

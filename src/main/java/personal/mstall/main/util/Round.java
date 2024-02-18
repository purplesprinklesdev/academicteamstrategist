package personal.mstall.main.util;

public class Round {
    public static double ToDecimalPlace(double n, double place) {
        return (Math.round(n * place)) / place;
    }
}

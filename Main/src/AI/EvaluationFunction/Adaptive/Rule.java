package AI.EvaluationFunction.Adaptive;

public class Rule {

    double[] configuration;
    int id;
    double weight;
    double score;

    public Rule(double[] configuration, int id) {

        this.configuration = configuration;
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double[] getConfiguration() {
        return configuration;
    }

    public void setConfiguration(double[] configuration) {
        this.configuration = configuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

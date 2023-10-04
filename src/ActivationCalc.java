public class ActivationCalc {

    private Double gin;

    public ActivationCalc(Double gin) {
        this.gin = gin;
    }

    public int step(double theta) {
        if (gin >= theta) {
            return 1;
        } else {
            return 0;
        }
    }

    public double sigmoid(double theta, double g) {
        return 1 / (1 + Math.exp(-g * (gin - theta)));
    }

    public int sign(double theta) {
        if (gin >= theta)
            return 1;
        else return -1;
    }

    public double tanh(double theta, double g) {
        double y = g * (gin - theta);
        return (Math.exp(y) - Math.exp(-y)) / (Math.exp(y) + Math.exp(-y));
    }

    public double linear(double theta, double a) {
        double x = gin - theta;
        if (x < -a) {
            return -1;
        } else if (x >= -a && x <= a) {
            return x / a;
        } else {
            return 1;
        }
    }
}

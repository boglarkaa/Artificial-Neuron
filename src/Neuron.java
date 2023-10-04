import java.util.ArrayList;

public class Neuron {
    private ArrayList<Double> x;
    private ArrayList<Double> w;

    public Neuron() {
        this.x = null;
        this.w = null;
    }

    public ArrayList<Double> getX() {
        return x;
    }

    public ArrayList<Double> getW() {
        return w;
    }

    public void setX(ArrayList<Double> x) {
        this.x = x;
    }

    public void setW(ArrayList<Double> w) {
        this.w = w;
    }

    public Neuron(ArrayList<Double> x, ArrayList<Double> w) {
        this.x = x;
        this.w = w;
    }
}

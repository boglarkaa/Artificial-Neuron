public class GlobalInputCalc {
    public static double sum(Neuron neuron) {
        double sum = 0;
        for (int i = 0; i < neuron.getX().size(); i++) {
            sum += neuron.getX().get(i) * neuron.getW().get(i);
        }
        return sum;
    }

    public static double prod(Neuron neuron) {
        double prod = 1;
        for (int i = 0; i < neuron.getX().size(); i++) {
            prod *= neuron.getX().get(i) * neuron.getW().get(i);
        }
        return prod;
    }

    public static double minimum(Neuron neuron) {
        double min = neuron.getX().get(0) * neuron.getW().get(0);
        for (int i = 1; i < neuron.getX().size(); i++) {
            if (min > neuron.getX().get(i) * neuron.getW().get(i)) {
                min = neuron.getX().get(i) * neuron.getW().get(i);
            }
        }
        return min;
    }

    public static double maximum(Neuron neuron) {
        double max = neuron.getX().get(0) * neuron.getW().get(0);
        for (int i = 1; i < neuron.getX().size(); i++) {
            if (max < neuron.getX().get(i) * neuron.getW().get(i)) {
                max = neuron.getX().get(i) * neuron.getW().get(i);
            }
        }
        return max;
    }
}

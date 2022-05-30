package functions;

import exceptions.FactorialOverflowException;
import exceptions.FunctionIrrelevantException;
import log.CSVLogger;

import java.util.HashMap;
import java.util.Map;

public class MathematicsBasic {

    // default accuracy
    private final int ACCURACY;

    public int ACCURACY() {
        return ACCURACY;
    }

    public MathematicsBasic(int accuracy) {
        this.ACCURACY = accuracy;
    }

    public MathematicsBasic() {
        ACCURACY = 5;
    }

    /*
     y (-inf, +inf), x (0, +inf) for y = ln(x)
     ln(1+x/1-x) = 2 * (x + x^3/3 + x^5/5 + ...)
     num of correct digits after the decimal point ~ accuracy
     */
    public double ln(double z) throws FunctionIrrelevantException {
        if (z <= 0) {
            throw new FunctionIrrelevantException("Function (ln) value is irrelevant in point " + z);
        }

        double eps = Math.pow(10, -(ACCURACY));
        double current_eps = 1;

        double result = (z - 1) / (z + 1);

        int i = 3;
        while (2 * current_eps >= eps * 0.001){
            double step_result = Math.pow((z - 1) / (z + 1), i) / i;
            current_eps = Math.abs(step_result);
            result += step_result;
            i += 2;
        }

        return 2 * result;
    }

    /*
     y [-1; 1], x (-inf, +inf)
     */

    public double sin(double x) throws FactorialOverflowException, FunctionIrrelevantException {
        double eps = Math.pow(10, -(ACCURACY));
        double current_eps = Math.abs(x);

        int i = 0;
        double result = x;
        while (current_eps >= eps) {
            double partRes = 0;
            try {
                partRes = Math.pow(-1, ++i) * Math.pow(x, 2 * i + 1) / factorial(2 * i + 1);
            } catch (FactorialOverflowException e) {
                throw new FactorialOverflowException("Decrease in calculation accuracy occurred");
            }
            current_eps = Math.abs(partRes);
            result += partRes;
        }
        return result;
    }

    /*
     boundary number is 21 (causes long type overflow)
     long max value = 9 223 372 036 854 775 807
     !21 = 18 795 307 255 050 944 540
     */
    public long factorial(int x) throws FactorialOverflowException, FunctionIrrelevantException {
        if (x < 0) {
            throw new FunctionIrrelevantException("Function (factorial) value is irrelevant in point " + x);
        }

        long res = 1;
        for (int i = 1; i <= x; i++) {
            res *= i;
            if (res < 0) throw new FactorialOverflowException("Function can't be calculated for x = " + x + ", too much value");
        }
        return res;
    }

    public void ln(double x, double step, int loop) throws FunctionIrrelevantException {
        double value = x;
        Map<Double, Double> values = new HashMap<>();
        for (int i = 0; i < loop; i++){
            values.put(value, ln(value));
            value += step;
        }
        CSVLogger.log("ln.csv", values);
    }

    public void sin(double x, double step, int loop) throws FunctionIrrelevantException, FactorialOverflowException {
        double value = x;
        Map<Double, Double> values = new HashMap<>();
        for (int i = 0; i < loop; i++){
            values.put(value, sin(value));
            value += step;
        }
        CSVLogger.log("sin.csv", values);
    }
}

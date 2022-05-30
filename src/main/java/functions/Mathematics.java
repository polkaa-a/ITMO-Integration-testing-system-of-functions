package functions;

import exceptions.FactorialOverflowException;
import exceptions.FunctionIrrelevantException;
import log.CSVLogger;
import java.util.HashMap;
import java.util.Map;

public class Mathematics {

    private final MathematicsBasic mathematicsBasic;

    public Mathematics(MathematicsBasic mathematicsBasic) {
        this.mathematicsBasic = mathematicsBasic;
    }

    public MathematicsBasic getMathematicsBasic() {
        return mathematicsBasic;
    }

    /*
     y (-inf, +inf)
     x != PI(1/2 + k) (k - integer)
    */
    public double tan(double x) throws FunctionIrrelevantException, FactorialOverflowException {
        // x/PI - 1/2 = k
        if (((x / Math.PI) - 0.5) % 1 == 0) {
            throw new FunctionIrrelevantException("Function (tan) value is irrelevant in point " + x);
        }
        double sin2 = Math.pow(mathematicsBasic.sin(x), 2);
        double result = Math.sqrt((1 / (1 - sin2)) - 1);

        if (isTanResultNegative(x)){
            return -result;
        }
        return result;
    }

    private boolean isTanResultNegative(double x){
        int k = (int) Math.ceil(x / Math.PI);
        return x / Math.PI + 0.5 > k;
    }

    /*
     x > 0, y (-inf, +inf)
     base > 0, base != 1
     */
    public double log(double base, double x) throws FunctionIrrelevantException {
        if (base <= 0 || base == 1) {
            throw new FunctionIrrelevantException
                    ("Base of logarithm should be more than 0 and not equals 1, your base : " + base);
        }
        if (x <= 0) {
            throw new FunctionIrrelevantException("Function (log) value is irrelevant in point " + x);
        }

        return mathematicsBasic.ln(x) / mathematicsBasic.ln(base);
    }

    public void tan(double x, double step, int loop) throws FunctionIrrelevantException, FactorialOverflowException {
        double value = x;
        Map<Double, Double> values = new HashMap<>();
        for (int i = 0; i < loop; i++){
            values.put(value, tan(value));
            value += step;
        }
        CSVLogger.log("tan.csv", values);
    }

    public void log(double base, double x, double step, int loop) throws FunctionIrrelevantException, FactorialOverflowException {
        double value = x;
        Map<Double, Double> values = new HashMap<>();
        for (int i = 0; i < loop; i++){
            values.put(value, log(base, value));
            value += step;
        }
        CSVLogger.log("log.csv", values);
    }

}

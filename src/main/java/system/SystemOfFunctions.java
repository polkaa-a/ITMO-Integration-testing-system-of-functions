package system;

import exceptions.FactorialOverflowException;
import exceptions.FunctionIrrelevantException;
import functions.Mathematics;
import log.CSVLogger;
import java.util.HashMap;
import java.util.Map;

public class SystemOfFunctions {

    private final Mathematics mathematics;

    public Mathematics getMathematics() {
        return mathematics;
    }

    public SystemOfFunctions(Mathematics mathematics) {
        this.mathematics = mathematics;
    }

    // accuracy ~ num of correct digits after the decimal point
    public double calcSystem(double x) throws FunctionIrrelevantException, FactorialOverflowException {
        if (x <= 0) {
            return mathematics.tan(x);
        } else {
            if (x == 1) {
                throw new FunctionIrrelevantException
                        ("The system of functions has no solution at the current values of x : " + x);
            }
            return ((Math.pow(mathematics.log(10, x) * mathematics.log(3, x), 2) /
                    Math.pow(mathematics.getMathematicsBasic().ln(x), 3)) * (mathematics.log(10, x) /
                    mathematics.getMathematicsBasic().ln(x))) - mathematics.log(5, x);
        }
    }

    public void calcSystem(double x, double step, int loop) throws FunctionIrrelevantException, FactorialOverflowException {
        double value = x;
        Map<Double, Double> values = new HashMap<>();
        for (int i = 0; i < loop; i++){
            values.put(value, calcSystem(value));
            value += step;
        }
        CSVLogger.log("system.csv", values);
    }
}

package integration;

import exceptions.FactorialOverflowException;
import exceptions.FunctionIrrelevantException;
import functions.Mathematics;
import functions.MathematicsBasic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import system.SystemOfFunctions;

import static org.junit.jupiter.api.Assertions.*;

public class SystemOfFunctionIntegrationTest {

    private static SystemOfFunctions systemSpy;
    private MathematicsBasic mathBasicSpy;
    private Mathematics mathematicsSpy;

    @BeforeEach
    public void setUp() {
        mathBasicSpy = Mockito.spy(new MathematicsBasic());
        mathematicsSpy = Mockito.spy(new Mathematics(mathBasicSpy));
        systemSpy = Mockito.spy(new SystemOfFunctions(mathematicsSpy));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.5, 6, 33.334455, 666.666})
    public void calcSystemInputPositive(double x) throws FunctionIrrelevantException, FactorialOverflowException {
        assertEquals(getReferenceFuncValue(x), systemSpy.calcSystem(x), getDefaultDifferential());
        Mockito.verify(mathBasicSpy, Mockito.times(6)).ln(x);
        Mockito.verify(mathematicsSpy, Mockito.times(2)).log(10, x);
        Mockito.verify(mathematicsSpy).log(3, x);
        Mockito.verify(mathematicsSpy).log(5, x);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2, -1.8888, -3, 0})
    public void calcSystemInputNegativeOrZero(double x) throws FunctionIrrelevantException, FactorialOverflowException {
        assertEquals(Math.tan(x), systemSpy.calcSystem(x), getDefaultDifferential());
        Mockito.verify(mathematicsSpy).tan(x);
    }

    private double getReferenceFuncValue(double x) {
        return ((Math.pow(Math.log10(x) * getReferenceLogValue(3, x), 2) /
                Math.pow(Math.log(x), 3)) * (Math.log10(x) /
                Math.log(x))) - getReferenceLogValue(5, x);
    }

    private double getReferenceLogValue(double base, double x) {
        return Math.log(x) / Math.log(base);
    }

    private double getDefaultDifferential() {
        return Math.pow(10, -mathBasicSpy.ACCURACY());
    }
}

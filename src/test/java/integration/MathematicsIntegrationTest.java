package integration;

import exceptions.FactorialOverflowException;
import exceptions.FunctionIrrelevantException;
import functions.Mathematics;
import functions.MathematicsBasic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class MathematicsIntegrationTest {

    private MathematicsBasic mathBasicSpy;
    private Mathematics mathematicsSpy;

    @BeforeEach
    public void setUp() {
        mathBasicSpy = Mockito.spy(new MathematicsBasic());
        mathematicsSpy = Mockito.spy(new Mathematics(mathBasicSpy));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.2, -1, -0.2, 0.1, 0.9, 1.3, 0})
    public void tanUsingBaseSin(double x) throws FunctionIrrelevantException, FactorialOverflowException {
        double result = mathematicsSpy.tan(x);
        Mockito.verify(mathBasicSpy).sin(x);
        Mockito.verify(mathematicsSpy).tan(x);
        assertEquals(Math.tan(x), result, getDefaultDifferential());
        assertEquals(Math.sin(x), mathBasicSpy.sin(x), getDefaultDifferential());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "2,      15",
            "3.5,     5",
            "13,  101.8"
    })
    public void logUsingBaseLn(double base, double x) throws FunctionIrrelevantException {
        ;
        double result = mathematicsSpy.log(base, x);

        Mockito.verify(mathBasicSpy).ln(x);
        Mockito.verify(mathBasicSpy).ln(base);
        Mockito.verify(mathematicsSpy).log(base, x);

        assertEquals(Math.log(x) / Math.log(base), result, getDefaultDifferential());
    }

    private double getDefaultDifferential() {
        return Math.pow(10, -mathBasicSpy.ACCURACY());
    }
}

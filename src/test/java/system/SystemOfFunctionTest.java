package system;

import exceptions.FactorialOverflowException;
import exceptions.FunctionIrrelevantException;
import functions.Mathematics;
import functions.MathematicsBasic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;

public class SystemOfFunctionTest {

    private static SystemOfFunctions system;

    @BeforeAll
    static void setUp() throws FunctionIrrelevantException, FactorialOverflowException {
        MathematicsBasic mathBasicMock = Mockito.mock(MathematicsBasic.class);
        Mathematics mathSpy = Mockito.spy(new Mathematics(mathBasicMock));

        system = new SystemOfFunctions(mathSpy);

        Mockito.when(mathBasicMock.sin(anyDouble())).thenAnswer(
                (Answer<Double>) invocation -> {
                    Object[] args = invocation.getArguments();
                    return Math.sin((Double) args[0]);
                });

        Mockito.when(mathBasicMock.ln(Mockito.anyDouble())).thenAnswer(
                (Answer<Double>) invocation -> {
                    Object[] args = invocation.getArguments();
                    return Math.log((Double) args[0]);
                });

        setUpMathSpyTan(mathSpy);
        setUpMathSpyLog(mathSpy);
    }

    private static void setUpMathSpyTan(Mathematics mathematics) throws FunctionIrrelevantException, FactorialOverflowException {
        Mockito.doReturn(Math.tan(-2)).when(mathematics).tan(-2);
        Mockito.doReturn(Math.tan(-1.888)).when(mathematics).tan(-1.888);
        Mockito.doReturn(Math.tan(-3)).when(mathematics).tan(-3);
        Mockito.doReturn(Math.tan(0)).when(mathematics).tan(0);
    }

    private static void setUpMathSpyLog(Mathematics mathematics) throws FunctionIrrelevantException {

        Mockito.doReturn(Math.log10(0.1)).when(mathematics).log(10, 0.1);
        Mockito.doReturn(getLog(3, 0.1)).when(mathematics).log(3, 0.1);
        Mockito.doReturn(getLog(5, 0.1)).when(mathematics).log(5, 0.1);

        Mockito.doReturn(Math.log10(0.5)).when(mathematics).log(10, 0.5);
        Mockito.doReturn(getLog(3, 0.5)).when(mathematics).log(3, 0.5);
        Mockito.doReturn(getLog(5, 0.5)).when(mathematics).log(5, 0.5);

        Mockito.doReturn(Math.log10(6)).when(mathematics).log(10, 6);
        Mockito.doReturn(getLog(3, 6)).when(mathematics).log(3, 6);
        Mockito.doReturn(getLog(5, 6)).when(mathematics).log(5, 6);

        Mockito.doReturn(Math.log10(33.334455)).when(mathematics).log(10, 33.334455);
        Mockito.doReturn(getLog(3, 33.334455)).when(mathematics).log(3, 33.334455);
        Mockito.doReturn(getLog(5, 33.334455)).when(mathematics).log(5, 33.334455);

        Mockito.doReturn(Math.log10(666)).when(mathematics).log(10, 666);
        Mockito.doReturn(getLog(3, 666)).when(mathematics).log(3, 666);
        Mockito.doReturn(getLog(5, 666)).when(mathematics).log(5, 666);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.5, 6, 33.334455, 666.666})
    public void calcSystemValidInputsFunc(double x) throws FunctionIrrelevantException, FactorialOverflowException {
        assertEquals(getReferenceValue(x), system.calcSystem(x), getDefaultDifferential());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2, -1.8888, -3})
    public void calcSystemValidInputsTan(double x) throws FunctionIrrelevantException, FactorialOverflowException {
        assertEquals(Math.tan(x), system.calcSystem(x), getDefaultDifferential());
    }

    @ParameterizedTest
    @ValueSource(doubles = {1})
    public void calcSystemBoundaryInvalidInputFunc(double x) {
        String expectedLog = "The system of functions has no solution at the current values of x : " + x;
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> system.calcSystem(x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0})
    public void calcSystemBoundaryValidInput(double x) throws FunctionIrrelevantException, FactorialOverflowException {
        assertEquals(Math.tan(x), system.calcSystem(x), getDefaultDifferential());
    }

    private double getReferenceValue(double x) {
        return ((Math.pow(Math.log10(x) * getLog(3, x), 2) /
                Math.pow(Math.log(x), 3)) * (Math.log10(x) /
                Math.log(x))) - getLog(5, x);
    }

    private static double getLog(double base, double x) {
        return Math.log(x) / Math.log(base);
    }

    private double getDefaultDifferential() {
        return Math.pow(10, -system.getMathematics().getMathematicsBasic().ACCURACY());
    }

    @AfterAll
    public static void log() throws FunctionIrrelevantException, FactorialOverflowException {
        system.calcSystem(-40, 2, 80);
    }
}

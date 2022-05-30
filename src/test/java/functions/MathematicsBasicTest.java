package functions;

import exceptions.FactorialOverflowException;
import exceptions.FunctionIrrelevantException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MathematicsBasicTest {

    @DisplayName("ln method on negative inputs")
    @ParameterizedTest
    @ValueSource(doubles = {-1, -20, -111})
    public void lnInvalidInput(double x) {
        MathematicsBasic mathematicsBasic = new MathematicsBasic();
        String expectedLog = "Function (ln) value is irrelevant in point " + x;
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> mathematicsBasic.ln(x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("ln method on positive inputs with different accuracy")
    @ParameterizedTest
    @CsvSource(value = {
            "5,   4, 1.6094",
            "99,  4, 4.5951",
            "266, 4, 5.5834",
            "5,   6, 1.609437",
            "99,  6, 4.595119",
            "266, 6, 5.583496",
            "5,   8, 1.60943791",
            "99,  8, 4.59511985",
            "266, 8, 5.58349630"
    })
    public void lnValidInput(double x, int accuracy, double expected) throws FunctionIrrelevantException {
        MathematicsBasic mathematicsBasic = new MathematicsBasic(accuracy);
        double result = mathematicsBasic.ln(x);
        assertEquals(expected, result, getDifferential(accuracy));
    }

    @DisplayName("ln method on boundary zero input")
    @Test
    public void lnBoundaryInput() {
        MathematicsBasic mathematicsBasic = new MathematicsBasic();
        String expectedLog = "Function (ln) value is irrelevant in point 0";
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> mathematicsBasic.ln(0)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("sin method on valid inputs with different accuracy")
    @ParameterizedTest
    @CsvSource(value = {
            "-3,   2, -0.14",
            "-2.5, 3, -0.598",
            "-2,   5, -0.90929",

            "-1.5, 2, -0.99",
            "-1,   3, -0.841",
            "-0.5, 5, -0.47942",

            "0.5,  2, 0.47",
            "1,    3, 0.841",
            "1.5,  5, 0.99749",

            "2,    2, 0.90",
            "2.5,  3, 0.598",
            "3,    5, 0.14112"
    })
    public void sinValidInput(double x, int accuracy, double expected) throws FunctionIrrelevantException, FactorialOverflowException {
        MathematicsBasic mathematicsBasic = new MathematicsBasic(accuracy);
        double result = mathematicsBasic.sin(x);
        assertEquals(expected, result, getDifferential(accuracy));
    }

    @DisplayName("sin method on boundary inputs with different accuracy")
    @ParameterizedTest
    @CsvSource(value = {
            "0,    3, 0",
            "-0.5, 4, -1",
            "0.5,  5, 1",
            "1.5,  2, -1"
    })
    public void sinBoundaryInput(double coefficient, int accuracy, double expected) throws FunctionIrrelevantException, FactorialOverflowException {
        MathematicsBasic mathematicsBasic = new MathematicsBasic(accuracy);
        double result = mathematicsBasic.sin(Math.PI * coefficient);
        assertEquals(expected, result, getDifferential(accuracy));
    }

    @DisplayName("sin method on boundary inputs with occurring decrease in accuracy")
    @ParameterizedTest
    @CsvSource(value = {
            "8,  6",
            "15, 6"
    })
    public void sinValidInputFactorialOverflow(double x, int accuracy) {
        MathematicsBasic mathematicsBasic = new MathematicsBasic(accuracy);
        String expectedLog = "Decrease in calculation accuracy occurred";
        FactorialOverflowException thrown = assertThrows(
                FactorialOverflowException.class,
                () -> mathematicsBasic.sin(x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("factorial method on valid inputs without occurring decrease in accuracy")
    @ParameterizedTest
    @CsvSource(value = {
            "5, 120",
            "8, 40320",
            "11, 39916800"
    })
    void factorialValidInputs(int x, long expected) throws FactorialOverflowException, FunctionIrrelevantException {
        MathematicsBasic mathematicsBasic = new MathematicsBasic();
        double result = mathematicsBasic.factorial(x);
        assertEquals(expected, result, 0.01);
    }

    @DisplayName("factorial method on boundary inputs without occurring decrease in accuracy")
    @ParameterizedTest
    @CsvSource(value = {
            "0, 1",
            "20, 2432902008176640000"
    })
    void factorialValidBoundaryInputs(int x, long expected) throws FactorialOverflowException, FunctionIrrelevantException {
        MathematicsBasic mathematicsBasic = new MathematicsBasic();
        double result = mathematicsBasic.factorial(x);
        assertEquals(expected, result, 0.01);
    }

    @DisplayName("factorial method on invalid negative inputs")
    @ParameterizedTest
    @CsvSource(value = {
            "-1, 1",
            "-12, 1"
    })
    void factorialInvalidInputs(int x) {
        MathematicsBasic mathematicsBasic = new MathematicsBasic();
        String expectedLog = "Function (factorial) value is irrelevant in point " + x;
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> mathematicsBasic.factorial(x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("factorial method on inputs with occurring decrease in accuracy")
    @ParameterizedTest
    @ValueSource(ints = {21, 28, 45})
    public void factorialOverflowNotificationAppear(int x) {
        MathematicsBasic mathematicsBasic = new MathematicsBasic();
        String expectedLog = "Function can't be calculated for x = " + x + ", too much value";
        FactorialOverflowException thrown = assertThrows(
                FactorialOverflowException.class,
                () -> mathematicsBasic.factorial(x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("factorial method on boundary input without occurring decrease in accuracy")
    @Test
    public void factorialOverflowNotificationNotAppearOnBoundaryInput() {
        MathematicsBasic mathematicsBasic = new MathematicsBasic();
        assertDoesNotThrow(() -> mathematicsBasic.factorial(20));
    }

    private double getDifferential(int accuracy) {
        return Math.pow(10, -(accuracy));
    }

    @AfterAll
    public static void log() throws FunctionIrrelevantException, FactorialOverflowException {
        MathematicsBasic mathematicsBasic = new MathematicsBasic();
        mathematicsBasic.ln(2, 2, 40);
        mathematicsBasic.sin(-4, 0.1, 80);
    }

}

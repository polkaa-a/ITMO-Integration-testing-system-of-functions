package functions;

import exceptions.FactorialOverflowException;
import exceptions.FunctionIrrelevantException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;

public class MathematicsTest {

    private static Mathematics mathematics;

    @BeforeEach
    public void setUp() throws FunctionIrrelevantException, FactorialOverflowException {
        MathematicsBasic mathBasicMock = Mockito.mock(MathematicsBasic.class);

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

        mathematics = new Mathematics(mathBasicMock);
    }

    @DisplayName("tan method on valid inputs")
    @ParameterizedTest
    @CsvSource(value = {
            "-1.5, -14.1014",
            "1.5,   14.1014",

            "-1.2,  -2.5721",
            "-1.3,  -3.6021",

            "-1,    -1.5574",
            "-0.5,  -0.5463",
            "-0.2,  -0.2027",
            "0.1,    0.1003",
            "0.4,    0.4227",
            "0.9,    1.2601",

            "1.2,    2.5721",
            "1.3,    3.6021"
    })
    public void tanValidInput(double x, double expected) throws FunctionIrrelevantException, FactorialOverflowException {
        double result = mathematics.tan(x);
        assertEquals(expected, result, getDefaultDifferential());
    }

    @DisplayName("tan method on boundary valid inputs")
    @ParameterizedTest
    @CsvSource(value = {
            "-1.4,  -5.7978",
            "-1.1,  -1.9647",
            "0,           0",
            "1.1,    1.9647",
            "1.4,    5.7978"
    })
    public void tanBoundaryValidInput(double x, double expected) throws FunctionIrrelevantException, FactorialOverflowException {
        double result = mathematics.tan(x);
        assertEquals(expected, result, getDefaultDifferential());
    }

    @DisplayName("tan method on invalid inputs")
    @ParameterizedTest
    @ValueSource(doubles = {-0.5, 0.5})
    public void tanInvalidInput(double coefficient) {
        String expectedLog = "Function (tan) value is irrelevant in point " + Math.PI * coefficient;
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> mathematics.tan(Math.PI * coefficient)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("log method on valid inputs")
    @ParameterizedTest
    @CsvSource(value = {
            "2,      15, 3.9068",
            "3.5,     5, 1.2847",
            "13,  101.8, 1.8023"
    })
    public void logValidInput(double base, double x, double expected) throws FunctionIrrelevantException {
        double result = mathematics.log(base, x);
        assertEquals(expected, result, getDefaultDifferential());
    }

    @DisplayName("log method on boundary invalid zero input")
    @ParameterizedTest
    @CsvSource(value = {
            "2,   0",
            "300, 0",
            "55,  0"
    })
    public void logBoundaryInvalidInput(double base, double x) {
        String expectedLog = "Function (log) value is irrelevant in point " + x;
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> mathematics.log(base, x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("log method on boundary invalid zero base")
    @ParameterizedTest
    @CsvSource(value = {
            "0,   3",
            "0, 300",
            "0,  55"
    })
    public void logBoundaryInvalidBase(double base, double x) {
        String expectedLog = "Base of logarithm should be more than 0 and not equals 1, your base : " + base;
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> mathematics.log(base, x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("log method on invalid inputs (negative)")
    @ParameterizedTest
    @CsvSource(value = {
            "4,       -6",
            "380, -144.9",
            "99,  -44.44"
    })
    public void logInvalidInput(double base, double x) {
        String expectedLog = "Function (log) value is irrelevant in point " + x;
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> mathematics.log(base, x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("log method on invalid base (negative)")
    @ParameterizedTest
    @CsvSource(value = {
            "-43,         1",
            "-15.89,   1111",
            "-333.555,  666"
    })
    public void logInvalidBase(double base, double x) {
        String expectedLog = "Base of logarithm should be more than 0 and not equals 1, your base : " + base;
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> mathematics.log(base, x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    @DisplayName("log method on invalid base (equals 1)")
    @ParameterizedTest
    @CsvSource(value = {
            "1,    555",
            "1, 1.8989",
            "1,     15"
    })
    public void logInvalidBaseEqualsOne(double base, double x) {
        String expectedLog = "Base of logarithm should be more than 0 and not equals 1, your base : " + base;
        FunctionIrrelevantException thrown = assertThrows(
                FunctionIrrelevantException.class,
                () -> mathematics.log(base, x)
        );
        assertTrue(thrown.getMessage().contains(expectedLog));
    }

    private double getDefaultDifferential() {
        return Math.pow(10, -mathematics.getMathematicsBasic().ACCURACY());
    }

    @AfterAll
    public static void log() throws FunctionIrrelevantException, FactorialOverflowException {
        mathematics.log(2, 2, 2, 40);
        mathematics.tan(-8, 0.1, 80);
    }
}

package log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

public class CSVLogger {
    public static void log(String filename, Map<Double, Double> values){
        try (PrintWriter writer = new PrintWriter(new File(filename))) {

            StringBuilder builder = new StringBuilder();
            for (Double x : values.keySet()) {
                builder.append(x).append(',').append(values.get(x)).append('\n');
            }

            writer.write(builder.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private CSVLogger(){}
}

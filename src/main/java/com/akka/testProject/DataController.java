package com.akka.testProject;

import java.io.*;
import java.util.Map;

/**
 * Created by Iaroslav on 11/11/2015.
 */
public class DataController {

    public static void agregateData(String dataFileName, String fileToWrite, int actorPoolSize) {
        BufferedReader rdr = null;
        BufferedWriter writer = null;
        CalculationService calculationService = new CalculationService(actorPoolSize);
        try {
            rdr = new BufferedReader(new FileReader(dataFileName));
            String[] currentLine;
            while (rdr.ready()) {
                currentLine = rdr.readLine().split(";");
                calculationService.aggregateAmount(new Row(Long.valueOf(currentLine[0]), Long.valueOf(currentLine[1])));
            }
            Map<Long, Row> result = calculationService.getResults(50000);
            System.out.println("workIsDone");
            calculationService.stopActors();
            System.out.println("sendStop");
            writer = new BufferedWriter(new FileWriter(fileToWrite));
            for (Map.Entry<Long, Row> entry : result.entrySet()) {
                writer.write(String.valueOf(entry.getKey()) + ";" + String.valueOf(entry.getValue().getAmount()));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            try {
                if (rdr != null) rdr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

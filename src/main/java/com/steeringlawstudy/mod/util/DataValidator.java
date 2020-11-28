package com.steeringlawstudy.mod.util;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

import static com.steeringlawstudy.mod.SteeringLawStudy.LOGGER;

// parses data gathered during experiments
public class DataValidator {
    private static Scanner in;

    static {
        try {
            in = new Scanner(new File("../run/logs/latest.log"));
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
    }

    // todo --> parse log file and collect data for analysis
    public static void parseData() {
        LocalDateTime now = LocalDateTime.now();
        String fileName = "data-" + now.getYear() +
                "-" + now.getMonthValue() +
                "-" + now.getDayOfMonth() +
                "-" + now.toLocalTime().toSecondOfDay();

        try {
            File log = new File("../run/study/" + fileName + ".log");

            if (!log.exists()) {
                log.createNewFile();
            } else {
                LOGGER.error("FILE EXISTS ALREADY! -" + fileName + ".log");
                return;
            }

            PrintWriter out = new PrintWriter(log);
            String line = in.nextLine();

            while (in.hasNextLine()) {
                if (line.lastIndexOf("concrete") == -1) {

                } else {
                    out.println(line);
                }

                line = in.nextLine();
            }

            out.close();

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}

package com.steeringlawstudy.mod.util;

import java.io.*;
import java.util.Scanner;

import static com.steeringlawstudy.mod.SteeringLawStudy.LOGGER;

// parses data gathered during experiments
public class DataValidator {
    private static Scanner in;

    static {
        try {
            in = new Scanner(new File("../run/logs/latest.log"));
        } catch (FileNotFoundException e) {
            LOGGER.info(e.getMessage());
        }
    }

    // todo --> parse log file and collect data for analysis
    public static void parseData() {
        // todo --> increase file name with each trial
        String currentName = "huhu";

        try {
            File log = new File("../run/study/" + currentName + ".log");

            if (!log.exists()) {
                log.createNewFile();
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

        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }
}

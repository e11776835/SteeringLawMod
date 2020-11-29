package com.steeringlawstudy.mod.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

import static com.steeringlawstudy.mod.SteeringLawStudy.LOGGER;
import static com.steeringlawstudy.mod.SteeringLawStudy.OUT_PATH;
import static com.steeringlawstudy.mod.SteeringLawStudy.START_BLOCK;
import static com.steeringlawstudy.mod.SteeringLawStudy.STOP_BLOCK;
import static com.steeringlawstudy.mod.SteeringLawStudy.PATH_BLOCK;

/**
 * parses data gathered  in logfile during experiments for statistical analysis
 */
public class DataValidator {
    public static void parseData() {
        LocalDateTime now = LocalDateTime.now();
        // todo --> to be exportable, file directory should be handled differently
        String fileName = "data-" + now.getYear() +
                "-" + now.getMonthValue() +
                "-" + now.getDayOfMonth() +
                "-" + now.toLocalTime().toSecondOfDay();
        try {
            // create output file
            File path = new File(OUT_PATH);
            File log = new File(path + "/" + fileName + ".log");

            if (!path.mkdirs() && !path.exists()) {
                LOGGER.error("COULD NOT CREATE DIRECTORY! - " + path);
                return;
            }

            if (!log.exists()) {
                if (!log.createNewFile()) {
                    LOGGER.error("FILE COULD NOT BE CREATED! - " + fileName + ".log");
                    return;
                }
            } else {
                LOGGER.error("FILE EXISTS ALREADY! - " + fileName + ".log");
                return;
            }

            try {
                // connect to input file
                Scanner in = new Scanner(new File("../run/logs/latest.log"));
                PrintWriter out = new PrintWriter(log);
                String line = in.nextLine();
                String lastLine = "";
                String block = "";
                String lastBlock = "";
                String outputText = "";
                LocalTime tick;
                LocalTime lastTick = null;
                long deltaTick = 0;
                long timeSpent = 0;
                boolean outOfBounds = true;
                boolean statusChanged;
                boolean setStart = false;

                // go through log file and parse relevant data..
                out.println("===[tick]=== ===[status]=== =[ms]=");

                while (in.hasNextLine()) {
                    // todo --> add bool check to see if experiment is currently going on

                    // only read lines containing the keyword "trgt"
                    if (line.lastIndexOf("trgt") > -1) {
                        statusChanged = false;
                        block = line.substring(line.lastIndexOf("trgt") + 21);
                        tick = LocalTime.parse(line.substring(11, 23));

                        // get start time
                        if (!setStart) {
                            lastTick = tick.minusNanos(50000000);
                            setStart = true;
                        } else {
                            deltaTick = ChronoUnit.MICROS.between(lastTick, tick);
                        }

                        // check if status changed (out of bounds / inside bounds)..
                        if (line.lastIndexOf(START_BLOCK) != -1 ||
                                line.lastIndexOf(STOP_BLOCK) != -1 ||
                                line.lastIndexOf(PATH_BLOCK) != -1) {
                            //.. and define output text accordingly
                            if (outOfBounds) {
                                outOfBounds = false;
                                statusChanged = true;
                                outputText = "out of bounds";
                            } else {
                                // != did not work
                                if (!block.equalsIgnoreCase(lastBlock)) {
                                    outputText = lastBlock;
                                    statusChanged = true;
                                }
                            }
                        } else {
                            if (!outOfBounds) {
                                outOfBounds = true;
                                statusChanged = true;
                                outputText = lastBlock;
                            }
                        }
                        timeSpent += deltaTick;

                        if (statusChanged) {
                            // ..which is put in the data file
                            out.println(tick + " " + outputText + "\t" +
                                    TimeUnit.MILLISECONDS.convert(timeSpent, TimeUnit.MICROSECONDS));
                            timeSpent = 0;
                        }

                        // prep for next line
                        lastBlock = block;
                        lastTick = tick;
                    }
                    lastLine = line;
                    line = in.nextLine();
                }
                out.close();

            } catch (FileNotFoundException e) {
                LOGGER.error("LOGFILE COULD NOT BE READ!");
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}

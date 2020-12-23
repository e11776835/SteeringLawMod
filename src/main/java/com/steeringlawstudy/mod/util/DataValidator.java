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
import static com.steeringlawstudy.mod.SteeringLawStudy.PATH_VISITED_BLOCK;

/**
 * util class for experiment data handling
 */
public class DataValidator {

    /**
     * parses data gathered in logfile during experiments for statistical analysis
     * creates logfile with details about the experiment
     */
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
                String pos = "";
                String lastBlock = "";
                String lastPos = "";
                String outputText = "";
                LocalTime tick;
                LocalTime lastTick = null;
                long deltaTick = 0;
                long timeSpent = 0;
                boolean outOfBounds = true;
                boolean statusChanged;
                boolean setStart = false;
                boolean running = true;

                // go through log file and parse relevant data..
                out.println("===[tick]=== ===[status]=== =====[pos]===== =[ms]=");

                while (running) {
                    // only read lines containing the keyword "trgt"
                    if (line.contains("trgt")) {
                        statusChanged = false;
                        block = line.substring(line.lastIndexOf("trgt") + 5, line.lastIndexOf("[") - 1);
                        pos = line.substring(line.lastIndexOf("["));
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
                                line.lastIndexOf(PATH_VISITED_BLOCK) != -1 ||
                                line.lastIndexOf(PATH_BLOCK) != -1) {
                            //.. and define output text accordingly
                            if (outOfBounds) {
                                outOfBounds = false;
                                statusChanged = true;
                                outputText = "out of bounds \t\t\t\t";
                            } else {
                                if (!block.equalsIgnoreCase(lastBlock) || !pos.equalsIgnoreCase(lastPos)) {
                                    outputText = lastBlock + "\t" + lastPos;
                                    statusChanged = true;
                                }
                            }
                        } else {
                            if (!outOfBounds) {
                                outOfBounds = true;
                                statusChanged = true;
                                outputText = lastBlock + "\t" + lastPos;
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
                        lastPos = pos;
                        lastTick = tick;
                    }

                    if (in.hasNextLine()) {
                        lastLine = line;
                        line = in.nextLine();
                    } else {
                        // log last open position in targeted blocks
                        tick = lastTick.plusNanos(50000000);

                        if (!lastBlock.equals(START_BLOCK) &&
                                !lastBlock.equals(STOP_BLOCK) &&
                                !lastBlock.equals(PATH_VISITED_BLOCK) &&
                                !lastBlock.equals(PATH_BLOCK)) {
                            lastBlock = "out of bounds \t\t\t\t";
                        } else {
                            lastBlock += "\t" + lastPos;
                        }

                        timeSpent = timeSpent + ChronoUnit.MICROS.between(lastTick, tick);
                        out.println(tick + " " + lastBlock + "\t" +
                                TimeUnit.MILLISECONDS.convert(timeSpent, TimeUnit.MICROSECONDS));
                        out.println("==================================");
                        running = false;
                    }
                }

                in.close();
                out.close();

            } catch (FileNotFoundException e) {
                LOGGER.error("LOGFILE COULD NOT BE READ!");
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}

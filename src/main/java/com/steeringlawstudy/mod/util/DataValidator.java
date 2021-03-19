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
        String fileName = "data-" + now.getYear() +
                "-" + now.getMonthValue() +
                "-" + now.getDayOfMonth() +
                "-" + now.toLocalTime().toSecondOfDay();
        try {
            // create output file
            File path = new File(OUT_PATH);
            File log = new File(path + "/" + fileName + ".log");
            File stats = new File(path + "/" + fileName + "_STATS.log");

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
                LocalTime lastTick = LocalTime.now();
                long deltaTick = 0;
                long timeSpent = 0;
                boolean outOfBounds = true;
                boolean statusChanged;
                boolean setStart = false;
                boolean running = true;

                // go through log file and parse relevant data..
                out.println("===[tick]=== ====[block]==== ==[pos]== \t=======[plyrPos]========= ==[ms]=");

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
                                // if (!block.equalsIgnoreCase(lastBlock) || !pos.equalsIgnoreCase(lastPos)) {
                                if (!block.equalsIgnoreCase(lastBlock)) {
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
                    } else if (line.contains("LEVEL")) {
                        out.println(line.substring(line.lastIndexOf("LEVEL")));

                    } else if (line.contains("CHANGING POSITION")) {
                        out.println(line.substring(line.lastIndexOf("CHANGING")));
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

                // calculate stats from logfile
                in = new Scanner(log);
                out = new PrintWriter(stats);
                line = in.nextLine(); // skip header
                line = in.nextLine();
                boolean print = false;
                boolean success = false;
                int duration = 0;
                int lastDuration = 0;

                // mode 0 = looking for start
                // mode 1 = looking for path / out of bounds
                // mode 2 = looking for finish / out of bounds
                int mode = 0;

                while (in.hasNextLine()) {
                    if (!line.contains("LEVEL") && !line.contains("CHANGING POSITION")) {
                        if (mode == 0) {
                            if (line.contains(START_BLOCK)) {
                                mode = 1;
                                duration = Integer.decode(line.substring(line.lastIndexOf('\t') + 1));
                            }

                        } else if (mode == 1) {
                            if (line.contains("bounds")) {
                                print = true;

                            } else if (line.contains(PATH_BLOCK)) {
                                mode = 2;
                                lastDuration = Integer.decode(line.substring(line.lastIndexOf('\t') + 1));
                            }
                        } else {
                            if (line.contains("bounds")) {
                                print = true;

                            } else if (line.contains(STOP_BLOCK)) {
                                print = true;
                                success = true;
                            }
                        }

                        if (print) {
                            mode = 0;

                            if (!success) {
                                out.println("Miss");
                                duration = 0;
                                lastDuration = 0;

                            } else {
                                int result = duration + lastDuration;
                                out.println("Hit\t" + result + "ms"); // TODO ZEIT AUSRECHNEN
                            }
                        }
                        print = false;
                        success = false;

                    } else {
                        out.println(line);
                    }

                    line = in.nextLine();
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

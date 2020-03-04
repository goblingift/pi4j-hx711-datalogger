/*
 * Copyright (C) 2019 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.hx711datalogger;

import java.io.IOException;

/**
 * Contains the main loop for program logic
 *
 * @author andre
 */
public class MainClass {

    /**
     * Main method for this application. Call em with following parameters:
     * @param args- These are the parameters:
     * param #1: filename of the excel-file (without ending .xlsx)
     * param #2: sleep time in seconds between measurement (default = 60 seconds)
     * param #3: load cell nominal load in kg
     * param #4: load-cell mV/V
     * param #5 and ongoing: DAT pin number + SCK pin number, separated by slash. E.g. "15/16".
     * You can add one or more of these DAT + SCK combinations, if you have connected more than 1 loadcell.
     */
    public static void main(String[] args) throws IOException {
        new DataLoggerScheduler(args).doWork();
    }
    
}

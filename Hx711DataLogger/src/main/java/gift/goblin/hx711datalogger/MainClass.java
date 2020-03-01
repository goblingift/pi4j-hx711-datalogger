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
     * @param args- 
     * param #1: filename of the excel-file (without ending .xlsx) (default = 
     * param #2: sleep time seconds between measurement (default = 60 seconds)
     * param #3: DAT pin number (default = 15)
     * param #4: SCK pin number (default = 16)
     */
    public static void main(String[] args) throws IOException {
        new DataLoggerScheduler(args).doWork();
    }
    
}

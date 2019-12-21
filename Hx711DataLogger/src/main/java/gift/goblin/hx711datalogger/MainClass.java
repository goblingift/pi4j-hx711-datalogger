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
     * param #1: DAT pin number.
     * param #2: SCK pin number.
     * param #3: sleep time milliseconds between measurement.
     */
    public static void main(String[] args) throws IOException {
        new DataLoggerScheduler(args).doWork();
    }
    
}

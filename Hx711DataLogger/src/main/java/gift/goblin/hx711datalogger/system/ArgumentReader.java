/*
 * Copyright (C) 2019 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.hx711datalogger.system;

/**
 * Reads the arguments which came when starting the application by console.
 *
 * @author andre
 */
public class ArgumentReader {

    // Default values, if no argument could get read
    private static final int DEFAULT_PIN_NO_LOAD_CELL_DAT = 15;
    private static final int DEFAULT_PIN_NO_LOAD_CELL_SCK = 16;
    private static final int DEFAULT_MEASUREMENT_RHYTM_MINS = 5;

    public ArgumentReader(MessageReader messageReader) {
        this.messageReader = messageReader;
    }

    private MessageReader messageReader;

    /**
     * Try to read the given pin number for DAT- otherwise take default.
     *
     * @param args as given when starting application.
     * @return read pin number or default.
     */
    public int getPinNumberDat(String[] args) {
        if (args.length >= 1) {
            try {
                int pinNumber = Integer.parseInt(args[0]);
                System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.dat.success", pinNumber));
                return pinNumber;
            } catch (NumberFormatException e) {
                System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.dat.nan", args[0]));
                return DEFAULT_PIN_NO_LOAD_CELL_DAT;
            }
        } else {
            System.out.println(messageReader.getMessage("parameters.dat.null"));
            return DEFAULT_PIN_NO_LOAD_CELL_DAT;
        }
    }
    
    /**
     * Try to read the given pin number for DAT- otherwise take default.
     *
     * @param args as given when starting application.
     * @return read pin number or default.
     */
    public int getPinNumberSCK(String[] args) {
        if (args.length >= 2) {
            try {
                int pinNumber = Integer.parseInt(args[1]);
                System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.sck.success", pinNumber));
                return pinNumber;
            } catch (NumberFormatException e) {
                System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.sck.nan", args[1]));
                return DEFAULT_PIN_NO_LOAD_CELL_SCK;
            }
        } else {
            System.out.println(messageReader.getMessage("parameters.sck.null"));
            return DEFAULT_PIN_NO_LOAD_CELL_SCK;
        }
    }

    public int getSleepTimeBetweenMeasurement(String[] args) {

        if (args.length >= 3) {
            try {
                int rhytm = Integer.parseInt(args[2]);
                System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.rhytm.success", rhytm));
                return rhytm;
            } catch (NumberFormatException e) {
                System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.rhytm.nan", args[2]));
                return DEFAULT_MEASUREMENT_RHYTM_MINS;
            }
        } else {
            System.out.println(messageReader.getMessage("parameters.rhytm.null"));
            return DEFAULT_MEASUREMENT_RHYTM_MINS;
        }
    }

}

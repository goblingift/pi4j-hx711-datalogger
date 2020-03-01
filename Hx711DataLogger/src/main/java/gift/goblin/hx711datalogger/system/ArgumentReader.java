/*
 * Copyright (C) 2019 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.hx711datalogger.system;

import gift.goblin.hx711datalogger.dto.PinAssignment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reads the arguments which came when starting the application by console.
 *
 * @author andre
 */
public class ArgumentReader {

    private static final String DELIMITER_PINS = "/";

    private static final int PARAM_NO_FILENAME = 0;
    private static final int PARAM_NO_RHYTM = 1;
    private static final int PARAM_NO_MAXWEIGHT = 2;
    private static final int PARAM_NO_MVV = 3;

    // Default values, if no argument could get read
    private static final int DEFAULT_PIN_NO_LOAD_CELL_DAT = 15;
    private static final int DEFAULT_PIN_NO_LOAD_CELL_SCK = 16;
    private static final int DEFAULT_MEASUREMENT_RHYTM_SECONDS = 60;
    private static final String DEFAULT_FILENAME = "tareMeasurement.xlsx";

    public ArgumentReader(MessageReader messageReader) {
        this.messageReader = messageReader;
    }

    private MessageReader messageReader;

    public boolean haveEnoughArguments(String[] args) {
        if (args.length < 5) {
            System.out.println(messageReader.getMessage("parameters.count.invalid"));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Read filename and concatenate the suffix ".xlsx" to the filename.
     *
     * @param args the argument String-Array.
     * @return the filename, including suffix.
     */
    public String getFileName(String[] args) {
        String readFileName = args[PARAM_NO_FILENAME];
        readFileName += ".xlsx";
        System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.filename.success", readFileName));
        return readFileName;
    }

    /**
     * Read the sleep time between the measurement, in seconds.
     *
     * @param args the argument String-Array.
     * @return the sleep time in seconds.
     */
    public int getSleepTimeBetweenMeasurement(String[] args) {
        try {
            int rhytm = Integer.parseInt(args[PARAM_NO_RHYTM]);
            System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.rhytm.success", rhytm));
            return rhytm;
        } catch (NumberFormatException e) {
            System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.rhytm.nan", args[PARAM_NO_RHYTM]));
            return DEFAULT_MEASUREMENT_RHYTM_SECONDS;
        }
    }

    public int getMaxWeightOfSingleLoadCell(String[] args) {
        try {
            int maxWeight = Integer.parseInt(args[PARAM_NO_MAXWEIGHT]);
            System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.maxweight.success", maxWeight));
            return maxWeight;
        } catch (NumberFormatException e) {
            System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.maxweight.nan", args[PARAM_NO_MAXWEIGHT]));
            return DEFAULT_MEASUREMENT_RHYTM_SECONDS;
        }
    }

    public double getMVVOfSingleLoadCell(String[] args) {
        try {
            double mVV = Double.parseDouble(args[PARAM_NO_MVV]);
            System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.mvv.success", mVV));
            return mVV;
        } catch (NumberFormatException e) {
            System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.mvv.nan", args[PARAM_NO_MVV]));
            throw new IllegalArgumentException();
        }
    }

    /**
     * Reads the pin assignments from the given parameter-String.
     *
     * @param args argument String-Array, should contain the pin-assignments in
     * format: "DAT/SCK", e.g. "15/16".
     * @return list with pin-assignments.
     * @throws IllegalArgumentException if any of the given pin-assignments
     * doesnt contains the right format.
     */
    public List<PinAssignment> getPinAssignments(String[] args) throws IllegalArgumentException {

        int argumentCount = args.length;
        List<PinAssignment> returnValue = new ArrayList<>();

        for (int i = 4; i < argumentCount; i++) {
            String actArg = args[i];
            Optional<PinAssignment> parsedPinAssignment = parsePinAssignment(actArg);
            if (parsedPinAssignment.isPresent()) {
                returnValue.add(parsedPinAssignment.get());
            } else {
                throw new IllegalArgumentException(messageReader.getMessage("parameters.pins.invalid-format"));
            }
        }

        return returnValue;
    }

    private Optional<PinAssignment> parsePinAssignment(String argument) {
        Optional<PinAssignment> optReturnValue = Optional.empty();

        boolean containsDelimiter = argument.contains(DELIMITER_PINS);
        if (containsDelimiter) {
            String[] splittedArgument = argument.split(DELIMITER_PINS);
            if (splittedArgument.length == 2) {
                String dat = splittedArgument[0];
                String sck = splittedArgument[1];

                try {
                    int datParsed = Integer.parseInt(dat);
                    int sckParsed = Integer.parseInt(sck);
                    optReturnValue = Optional.of(new PinAssignment(datParsed, sckParsed));
                } catch (NumberFormatException e) {
                    System.out.println(messageReader.getMessageAndReplaceHashtag("parameters.pins.nan", dat + sck));
                }
            } else {
                System.out.println(messageReader.getMessage("parameters.pins.invalid-format"));
            }
        } else {
            System.out.println(messageReader.getMessage("parameters.pins.invalid-format"));
        }

        return optReturnValue;
    }

}

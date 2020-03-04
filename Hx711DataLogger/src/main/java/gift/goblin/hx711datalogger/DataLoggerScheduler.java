/*
 * Copyright (C) 2019 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.hx711datalogger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import gift.goblin.hx711.GainFactor;
import gift.goblin.hx711.Hx711;
import gift.goblin.hx711datalogger.dto.PinAssignment;
import gift.goblin.hx711datalogger.excel.ExcelWriter;
import gift.goblin.hx711datalogger.system.ArgumentReader;
import gift.goblin.hx711datalogger.system.MessageReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author andre
 */
public class DataLoggerScheduler {

    private final int loadCellCount;
    private List<Hx711> loadCells;

    private final ArgumentReader argumentReader;
    private final MessageReader messageReader;
    private final ExcelWriter excelWriter;
    private final int sleepTimeBetweenMeasurement;

    public DataLoggerScheduler(String[] args) throws IOException {
        this.messageReader = new MessageReader();
        this.argumentReader = new ArgumentReader(messageReader);

        // Check if we have at least 5 arguments
        boolean enoughArguments = argumentReader.haveEnoughArguments(args);
        if (!enoughArguments) {
            System.out.println(messageReader.getMessage("parameters.count.invalid.shutdown"));
            System.exit(-1);
        }

        // parameter 1: filename
        String fileName = argumentReader.getFileName(args);
        // parameter 2: measurement rhytm (seconds)
        sleepTimeBetweenMeasurement = argumentReader.getSleepTimeBetweenMeasurement(args);
        // parameter 3: maximum weight per single loadcell
        int maxWeightSingleLoadCell = argumentReader.getMaxWeightOfSingleLoadCell(args);
        // parameter 4: mv/V per loadcell
        double mvVLoadCell = argumentReader.getMVVOfSingleLoadCell(args);
        // parameter 5 and followings: dat/sck pin numbers
        List<PinAssignment> pinAssignments = argumentReader.getPinAssignments(args);
        loadCellCount = pinAssignments.size();

        setupRaspberry(pinAssignments, maxWeightSingleLoadCell, mvVLoadCell);

        this.excelWriter = new ExcelWriter(fileName, loadCellCount);
    }

    /**
     * The main-loop method.
     */
    public void doWork() {
        do {
            List<Long> newValues = new ArrayList<>();
            List<Long> newWeights = new ArrayList<>();
            for (Hx711 actLoadCell : loadCells) {
                long value = actLoadCell.readValue();
                System.out.println(messageReader.getMessageAndReplaceHashtag("system.measurement.raw.success", value));
                newValues.add(value);

                long weight = actLoadCell.measure();
                System.out.println(messageReader.getMessageAndReplaceHashtag("system.measurement.weight.success", weight));
                newWeights.add(weight);
                
                try {
                    // 2s wait for next measurement
                    Thread.sleep(2_000);
                } catch (InterruptedException ex) {
                    System.out.println("Error while sleeping!");
                }
            }
            Long sum = newWeights.stream().collect(Collectors.summingLong(Long::longValue));
            System.out.println(messageReader.getMessageAndReplaceHashtag("system.measurement.sum", sum.toString()));

            excelWriter.writeValues(newValues, newWeights);

            try {
                System.out.println("Start sleeping " + sleepTimeBetweenMeasurement + " seconds...");
                Thread.sleep(sleepTimeBetweenMeasurement * 1000);
            } catch (InterruptedException ex) {
                System.out.println(messageReader.getMessage("system.sleep.error"));
                System.out.println(ex.getMessage());
            }
        } while (true);
    }

    private void setupRaspberry(List<PinAssignment> pinAssignments, int maxWeight, double mVV) {

        loadCells = new ArrayList<>();

        for (PinAssignment actPins : pinAssignments) {

            GpioController gpioController = GpioFactory.getInstance();
            GpioPinDigitalInput pinDat = gpioController.provisionDigitalInputPin(RaspiPin.getPinByAddress(actPins.getDat()),
                    "Load-cell DAT", PinPullResistance.OFF);
            GpioPinDigitalOutput pinSck = gpioController.provisionDigitalOutputPin(RaspiPin.getPinByAddress(actPins.getSck()),
                    "Load-cell SCK", PinState.LOW);
            Hx711 hx711 = new Hx711(pinDat, pinSck, maxWeight, mVV, GainFactor.GAIN_128);
            long tareValue = hx711.measureAndSetTare();
            System.out.println(messageReader.getMessageAndReplaceHashtag("system.hx711.tare-value", tareValue));
            
            loadCells.add(hx711);
            System.out.println(messageReader.getMessageAndReplaceHashtag("system.hx711.success", actPins));
        }

        System.out.println(messageReader.getMessage("system.pins.success"));

    }

}

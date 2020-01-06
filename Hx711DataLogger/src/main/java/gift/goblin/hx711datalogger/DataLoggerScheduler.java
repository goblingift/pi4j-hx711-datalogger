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
import gift.goblin.hx711datalogger.excel.ExcelWriter;
import gift.goblin.hx711datalogger.system.ArgumentReader;
import gift.goblin.hx711datalogger.system.MessageReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class DataLoggerScheduler {


    GpioPinDigitalInput pinLoadCellDat;
    GpioPinDigitalOutput pinLoadCellSck;
    Hx711 hx711LoadCell1;
    
    private final ArgumentReader argumentReader;
    private final MessageReader messageReader;
    private final ExcelWriter excelWriter;
    private String[] args;
    private final int sleepTimeBetweenMeasurement;

    public DataLoggerScheduler(String[] args) throws IOException {
        this.args = args;
        this.messageReader = new MessageReader();
        this.argumentReader = new ArgumentReader(messageReader);
        
        int pinNumberDat = argumentReader.getPinNumberDat(args);
        int pinNumberSCK = argumentReader.getPinNumberSCK(args);
        sleepTimeBetweenMeasurement = argumentReader.getSleepTimeBetweenMeasurement(args);
        String fileName = argumentReader.getFileName(args);
        
        setupRaspberry(pinNumberDat, pinNumberSCK);
        
        this.excelWriter = new ExcelWriter(fileName);
    }

    /**
     * The main-loop method.
     */
    public void doWork() {
        do {
            long tareValue = hx711LoadCell1.measureAndSetTare();
            System.out.println(messageReader.getMessageAndReplaceHashtag("system.measurement.success", new Long(tareValue)));
            excelWriter.writeTare(tareValue);
            try {
                System.out.println("Start sleeping " + sleepTimeBetweenMeasurement + " seconds...");
                Thread.sleep(sleepTimeBetweenMeasurement * 1000);
            } catch (InterruptedException ex) {
                System.out.println(messageReader.getMessage("system.sleep.error"));
                System.out.println(ex.getMessage());
            }
        } while (true);
    }
    
    private void setupRaspberry(int pinNumberDat, int pinNumberSCK) {
        GpioController gpioController = GpioFactory.getInstance();
        pinLoadCellDat = gpioController.provisionDigitalInputPin(RaspiPin.getPinByAddress(pinNumberDat),
                "Load-cell DAT", PinPullResistance.OFF);
        pinLoadCellSck = gpioController.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pinNumberSCK),
                "Load-cell SCK", PinState.LOW);
        hx711LoadCell1 = new Hx711(pinLoadCellDat, pinLoadCellSck, 500, 2.0, GainFactor.GAIN_128);
        System.out.println(messageReader.getMessage("system.pins.success"));
    }
    
}

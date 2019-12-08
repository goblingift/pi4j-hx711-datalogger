/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 * Contains the main loop for program logic
 *
 * @author andre
 */
public class MainClass {

    // wiringPi pin-setup
    private static final int PIN_NO_LOAD_CELL_DAT = 15;
    private static final int PIN_NO_LOAD_CELL_SCK = 16;

    GpioPinDigitalInput pinLoadCellDat;
    GpioPinDigitalOutput pinLoadCellSck;
    Hx711 hx711LoadCell1;
    
    /**
     * Main method for this application. Call em with following parameters:
     * @param args- param #1: sleep time milliseconds between measurement.
     * param #2: filename for excel output.
     */
    public static void main(String[] args) {

        new MainClass().startLoop();
        
        
        
    }
    
    private void startLoop() {
        setup();
        
        hx711LoadCell1.measureAndSetTare();
        
        
    }
    
    
    private void setup() {
        GpioController gpioController = GpioFactory.getInstance();
        pinLoadCellDat = gpioController.provisionDigitalInputPin(RaspiPin.getPinByAddress(PIN_NO_LOAD_CELL_DAT),
                "Load-cell DAT", PinPullResistance.OFF);
        pinLoadCellSck = gpioController.provisionDigitalOutputPin(RaspiPin.getPinByAddress(PIN_NO_LOAD_CELL_SCK),
                "Load-cell SCK", PinState.LOW);
        hx711LoadCell1 = new Hx711(pinLoadCellDat, pinLoadCellSck, 500, 2.0, GainFactor.GAIN_128);
    }
    
    
}

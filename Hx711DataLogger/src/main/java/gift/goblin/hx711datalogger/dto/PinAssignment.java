/*
 * Copyright (C) 2020 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.hx711datalogger.dto;

/**
 * Contains the pin assignment of DAT & SCK pins.
 * @author andre
 */
public class PinAssignment {
    private final int dat;
    private final int sck;

    public PinAssignment(int dat, int sck) {
        this.dat = dat;
        this.sck = sck;
    }

    public int getDat() {
        return dat;
    }

    public int getSck() {
        return sck;
    }

    @Override
    public String toString() {
        return "PinAssignment{" + "dat=" + dat + ", sck=" + sck + '}';
    }
    
}

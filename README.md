# pi4j-hx711-datalogger
Test application to get the values of a hx711 load-cell amplifier and save them into an excel-file.

Start the application by console, with the following parameters:
     * param #1: filename of the excel-file (without ending .xlsx)
     * param #2: sleep time in seconds between measurement (default = 60 seconds)
     * param #3: load cell nominal load in kg
     * param #4: load-cell mV/V
     * param #5 and ongoing: DAT pin number + SCK pin number, separated by slash. E.g. "15/16".
     * You can add one or more of these DAT + SCK combinations, if you have connected more than 1 loadcell.
     
     E.g.: "java -jar Hx711DataLogger-1.2.0-SNAPSHOT-jar-with-dependencies.jar 4loadcells 30 3 1.0 15/16 4/5 6/10 11/31",
     if you have connected 4 load-cells with a nominal load of 3kg each.

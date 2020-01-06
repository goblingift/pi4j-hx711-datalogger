/*
 * Copyright (C) 2019 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.hx711datalogger.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author andre
 */
public class ExcelWriter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int rowNumber = 1;
    private File file;
    
    public ExcelWriter(String fileName) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("TareMeasurement");
        file = new File(fileName);
    }
    
    public void writeTare(long tareValue) {
        System.out.println("Start writing to excel file...");

        Row row = sheet.createRow(rowNumber++);
        Cell cellTimestamp = row.createCell(0);
        Cell cellValue = row.createCell(1);
        
        cellTimestamp.setCellValue(LocalDateTime.now().toString());
        cellValue.setCellValue(tareValue);
        
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            System.out.println("Write file to: " + file.getAbsolutePath());
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done with excel file writing!");
    }

}

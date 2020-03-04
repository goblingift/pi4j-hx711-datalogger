/*
 * Copyright (C) 2019 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.hx711datalogger.excel;

import gift.goblin.hx711datalogger.system.MessageReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    private final File file;
    private final int loadCellCount;
    private MessageReader messageReader = null;

    public ExcelWriter(String fileName, int loadCellCount) {
        try {
            this.messageReader = new MessageReader();
        } catch (IOException ex) {
            System.out.println("Exception while initialize MessageReader:" + ex.getMessage());
        }

        this.loadCellCount = loadCellCount;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("TareMeasurement");
        this.file = new File(fileName);

        writeHeaders();
    }

    public void writeHeaders() {
        int i = 0;
        Row row = sheet.createRow(rowNumber++);
        Cell cellTimeStampHeader = row.createCell(i);
        cellTimeStampHeader.setCellValue(messageReader.getMessage("excel.header.timestamp"));

        for (i = 1; i < loadCellCount +1; i++) {
            Cell cellHeaderValue = row.createCell(i);
            cellHeaderValue.setCellValue(messageReader.getMessageAndReplaceHashtag("excel.header.raw-value", i));
            
            Cell cellHeaderWeight = row.createCell(i + loadCellCount);
            cellHeaderWeight.setCellValue(messageReader.getMessageAndReplaceHashtag("excel.header.weight", i));
        }
        Cell cellSumValueHeader = row.createCell(i + loadCellCount);
        cellSumValueHeader.setCellValue(messageReader.getMessage("excel.header.sumvalues"));
        i++;
        Cell cellSumWeightHeader = row.createCell(i + loadCellCount);
        cellSumWeightHeader.setCellValue(messageReader.getMessage("excel.header.sumweight"));
        
        save();
    }

    /**
     * Writes the values into the excel file.
     *
     * @param rawValues list with load-cell values.
     * @param weights list with weight measurements of load-cells.
     */
    public void writeValues(List<Long> rawValues, List<Long> weights) {
        
        Row row = sheet.createRow(rowNumber++);
        Cell cellTimestamp = row.createCell(0);
        cellTimestamp.setCellValue(LocalDateTime.now().toString());

        int columnNumber = 1;
        for (Long actValue : rawValues) {
            Cell cellValue = row.createCell(columnNumber);
            cellValue.setCellValue(actValue);

            columnNumber++;
        }

        for (Long actWeight : weights) {
            Cell cellValue = row.createCell(columnNumber);
            cellValue.setCellValue(actWeight);

            columnNumber++;
        }
        
        Long sumValues = rawValues.stream().collect(Collectors.summingLong(Long::longValue));
        Long sumWeight = weights.stream().collect(Collectors.summingLong(Long::longValue));
        
        Cell cellSumValues = row.createCell(columnNumber);
        cellSumValues.setCellValue(sumValues);
        columnNumber++;
        Cell cellSumWeights = row.createCell(columnNumber);
        cellSumWeights.setCellValue(sumWeight);
        
        save();
    }

    private void save() {
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

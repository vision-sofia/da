package com.viziazasofia.da.data_extract;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@SpringBootApplication
public class DataExtractApplication {

    public static final String SAMPLE_XLS_FILE_PATH = "./sample-xls-file.xls";

    public static void main(String[] args) throws IOException, InvalidFormatException {
        SpringApplication.run(DataExtractApplication.class, args);

        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(new File("./ОБЩА-2014.xls.xlsx"));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");


        System.out.println("Retrieving Sheets using for-each loop");
        for(Sheet sheet: workbook) {
            System.out.println("=> " + sheet.getSheetName());
        }


        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        int train_columns = 7;
        int counter = 0;


        // Iterate first column tables

        System.out.println("\n\nIterating First column tables\n");
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if(startsTable(row)) {
                while (rowIterator.hasNext() && !endOfTable(row)) {
                    row = rowIterator.next();

                    // Now let's iterate over the columns of the current row
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext() && counter < 7) {

                        Cell cell = cellIterator.next();
                        String cellValue = dataFormatter.formatCellValue(cell);
                        System.out.print(cellValue + "\t");
                        counter++;
                    }
                    counter = 0;
                    System.out.println();
                }
            }



            System.out.println();
        }

        // Closing the workbook
        workbook.close();
    }

    private static boolean startsTable(Row row){
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Cell> cellIterator = row.cellIterator();
        String cellValue = "";

        if (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            cellValue = dataFormatter.formatCellValue(cell);
        }

        if(cellValue.equals("№")) return true;

        return false;
    }

    private static boolean endOfTable(Row row){
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Cell> cellIterator = row.cellIterator();

        String cellValue = "";
        int counter = 0;
        while (cellIterator.hasNext() && counter < 3) {

            Cell cell = cellIterator.next();
            cellValue = dataFormatter.formatCellValue(cell);
            counter++;
        }

        if(cellValue.equals("ОБЩО")) return true;

        return false;
    }

    private static void printCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue());
                break;
            case STRING:
                System.out.print(cell.getRichStringCellValue().getString());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue());
                } else {
                    System.out.print(cell.getNumericCellValue());
                }
                break;
            case FORMULA:
                System.out.print(cell.getCellFormula());
                break;
            case BLANK:
                System.out.print("");
                break;
            default:
                System.out.print("");
        }

        System.out.print("\t");

    }





}

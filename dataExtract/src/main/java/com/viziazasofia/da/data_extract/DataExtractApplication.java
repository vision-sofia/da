package com.viziazasofia.da.data_extract;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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




        ArrayList<Train> trains = new ArrayList<>();
        ArrayList<Trolley> trolleybuses = new ArrayList<>();
        ArrayList<Bus> buses = new ArrayList<>();


        for(Sheet sheet: workbook) {

            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();

            int columns = 7;
            int transportNumber = 0;

            boolean isTrain = false;
            boolean isBus = false;
            boolean isTrolley = false;


            // Iterate first column tables

            System.out.println("\n\nIterating First column tables\n");
            Iterator<Row> rowIterator = sheet.rowIterator();



            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if(isThereTrainLine(row)){
                    isTrain = true;
                    transportNumber = getTransportNumber(row);
                }else if(isThereTrolleyLine(row)){
                    isTrolley = true;
                    transportNumber = getTransportNumber(row);
                }else if(isThereBusLine(row)){
                    isBus = true;
                    transportNumber = getTransportNumber(row);
                }


                if(startsTable(row)) {

                    ArrayList<Station> stations = new ArrayList<>();
                    boolean endOfTable = false;

                    while (rowIterator.hasNext() && !endOfTable) {

                        row = rowIterator.next();

                        // Now let's iterate over the columns of the current row
                        Iterator<Cell> cellIterator = row.cellIterator();
                        Station station = new Station();
                        int counter = 0;

                        while (cellIterator.hasNext() && counter < columns) {

                            Cell cell = cellIterator.next();
                            String cellValue = dataFormatter.formatCellValue(cell);

                            if(!cellValue.isEmpty()) {
                                switch (counter) {
                                    case 1:
                                        station.setCode(cellValue);
                                        break;
                                    case 2:
                                        station.setName(cellValue);
                                        break;
                                    case 3:
                                        station.setGotUp(Integer.parseInt(cellValue));
                                        break;
                                    case 4:
                                        station.setDescended(Integer.parseInt(cellValue));
                                        break;
                                    case 5:
                                        station.setExchange(Integer.parseInt(cellValue));
                                        break;
                                    case 6:
                                        station.setLoaded(Integer.parseInt(cellValue));
                                        if(station.getLoaded() == 0) endOfTable = true;
                                        break;
                                }
                            }
                            System.out.print(cellValue + "\t");
                            counter++;
                        }

                        stations.add(station);

                        counter = 0;
                        System.out.println();
                    }
                    if(isTrain) {
                        trains.add(new Train(transportNumber, stations));
                        isTrain = false;
                    }else if(isTrolley){
                        trolleybuses.add(new Trolley(transportNumber, stations));
                        isTrolley = false;
                    }else if(isBus){
                        buses.add(new Bus(transportNumber, stations));
                        isBus = false;
                    }
                }



                System.out.println();
            }



        }

//
//        // Getting the Sheet at index zero
//        Sheet sheet = workbook.getSheetAt(0);

        // Closing the workbook
        workbook.close();



        for(Train train : trains){
            System.out.println(train.toString());
        }

        for(Trolley trolley : trolleybuses){
            System.out.println(trolley.toString());
        }

        for(Bus bus : buses){
            System.out.println(bus.toString());
        }
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


    private static boolean isThereTrainLine(Row row){
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Cell> cellIterator = row.cellIterator();

        String cellValue = "";
        int counter = 0;
        while (cellIterator.hasNext() && counter < 3) {

            Cell cell = cellIterator.next();
            cellValue = dataFormatter.formatCellValue(cell);
            counter++;
        }

        if(cellValue.contains("ТРАМВАЙНА ЛИНИЯ")) return true;

        return false;
    }

    private static boolean isThereBusLine(Row row){
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Cell> cellIterator = row.cellIterator();

        String cellValue = "";
        int counter = 0;
        while (cellIterator.hasNext() && counter < 3) {

            Cell cell = cellIterator.next();
            cellValue = dataFormatter.formatCellValue(cell);
            counter++;
        }

        if(cellValue.contains("АВТОБУСНА ЛИНИЯ") || cellValue.contains("А ЛИНИЯ")) return true;

        return false;
    }


    private static boolean isThereTrolleyLine(Row row){
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Cell> cellIterator = row.cellIterator();

        String cellValue = "";
        int counter = 0;
        while (cellIterator.hasNext() && counter < 3) {

            Cell cell = cellIterator.next();
            cellValue = dataFormatter.formatCellValue(cell);
            counter++;
        }

        if(cellValue.contains("ТРОЛЕЙБУСНА  ЛИНИЯ")) return true;

        return false;
    }


    private static int getTransportNumber(Row row){
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Cell> cellIterator = row.cellIterator();

        String cellValue = "";
        int counter = 0;
        while (cellIterator.hasNext() && counter < 3) {

            Cell cell = cellIterator.next();
            cellValue = dataFormatter.formatCellValue(cell);
            counter++;
        }


        int tmp = 1;
        if(cellValue.contains("-Тм")) tmp = 4;
        if(cellValue.contains(" -Б")) tmp = 4;

        StringBuilder stringBuilder = new StringBuilder();

        for(int index = cellValue.length() - tmp; cellValue.charAt(index) >= '0' && cellValue.charAt(index) <= '9'; index--){
            stringBuilder.append(cellValue.charAt(index));
        }

        System.out.println("!!!!!!" + stringBuilder.reverse().toString());
        return Integer.parseInt((stringBuilder.reverse()).toString());
    }

    private static boolean endOfTable(Row row){
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Cell> cellIterator = row.cellIterator();

        String cellValue = "";
        int counter = 0;
        while (cellIterator.hasNext() && counter < 2) {

            Cell cell = cellIterator.next();
            cellValue = dataFormatter.formatCellValue(cell);
            counter++;
        }

        if(cellValue.contains("ОБЩО")) return true;

        return false;
    }





}

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
    public static final int FIRST_COLUMN = 1;
    public static final int SECOND_COLUMN = 2;

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
            String transportNumber = "";

            boolean isTrain = false;
            boolean isBus = false;
            boolean isTrolley = false;


            // Iterate first column tables

            Iterator<Row> rowIterator = sheet.rowIterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if(isThereTrainLine(row, FIRST_COLUMN)){
                    isTrain = true;
                    transportNumber = getTransportNumber(row, FIRST_COLUMN);
                }else if(isThereTrolleyLine(row,FIRST_COLUMN)){
                    isTrolley = true;
                    transportNumber = getTransportNumber(row,FIRST_COLUMN);
                }else if(isThereBusLine(row,FIRST_COLUMN)){
                    isBus = true;
                    transportNumber = getTransportNumber(row,FIRST_COLUMN);
                }


                if(startsTable(row,FIRST_COLUMN)) {

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


            }






            // Iterate second column tables

            Iterator<Row> secondRowIterator = sheet.rowIterator();

            while (secondRowIterator.hasNext()) {
                Row row = secondRowIterator.next();

                if(isThereTrainLine(row,SECOND_COLUMN)){
                    isTrain = true;
                    transportNumber = getTransportNumber(row,SECOND_COLUMN);
                }else if(isThereTrolleyLine(row,SECOND_COLUMN)){
                    isTrolley = true;
                    transportNumber = getTransportNumber(row,SECOND_COLUMN);
                }else if(isThereBusLine(row,SECOND_COLUMN)){
                    isBus = true;
                    transportNumber = getTransportNumber(row,SECOND_COLUMN);
                }


                if(startsTable(row,SECOND_COLUMN)) {

                    if(secondRowIterator.hasNext()) row = secondRowIterator.next();
                    ArrayList<Station> stations = new ArrayList<>();
                    boolean endOfTable = false;

                    while (secondRowIterator.hasNext() && !endOfTable(row, SECOND_COLUMN)) {

                        row = secondRowIterator.next();

                        // Now let's iterate over the columns of the current row
                        Iterator<Cell> cellIterator = row.cellIterator();
                        Station station = new Station();
                        int counter = 0;

                        while (cellIterator.hasNext() && counter < (columns + 16)) {


                            Cell cell = cellIterator.next();
                            String cellValue = dataFormatter.formatCellValue(cell);


                            if(!cellValue.isEmpty()) {
                                switch (counter) {
                                    case 16:
                                        station.setCode(cellValue);
                                        break;
                                    case 17:
                                        station.setName(cellValue);
                                        break;
                                    case 18:
                                        try {
                                             station.setGotUp(Integer.parseInt(cellValue));
                                        }catch(Exception ex){
                                             station.setGotUp(0);
                                        }
                                        break;
                                    case 19:
                                        try {
                                            station.setDescended(Integer.parseInt(cellValue));
                                        }catch(Exception ex){
                                            station.setDescended(0);
                                        }
                                        break;
                                    case 20:
                                        try {
                                            station.setExchange(Integer.parseInt(cellValue));
                                        }catch(Exception ex){
                                            station.setExchange(0);
                                        }
                                        break;
                                    case 21:
                                        try {
                                            station.setLoaded(Integer.parseInt(cellValue));
                                            if(station.getLoaded() == 0) endOfTable = true;
                                        }catch(Exception ex){
                                            station.setLoaded(0);
                                        }
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


            }



        }


        // Closing the workbook
        workbook.close();



//        System.out.println("{ trams: [");
//        for(Train train : trains){
//
//            if(trains.get(trains.size() - 1).equals(train)) {
//                System.out.println(train.toString());
//                break;
//            }
//
//            System.out.println(train.toString() + ",");
//
//        }
//        System.out.println("] }");


//
//        System.out.println("{ trolleybusses: [");
//        for(Trolley trolley : trolleybuses){
//
//            if(trolleybuses.get(trolleybuses.size() - 1).equals(trolley)) {
//                System.out.println(trolley.toString());
//                break;
//            }
//
//            System.out.println(trolley.toString() + ",");
//
//        }
//        System.out.println("] }");
//
//
//
        System.out.println("{ busses: [");
        for(Bus bus : buses){

            if(buses.get(buses.size() - 1).equals(bus)) {
                System.out.println(bus.toString());
                break;
            }

            System.out.println(bus.toString() + ",");

        }
        System.out.println("] }");
    }

    private static boolean startsTable(Row row, int column){
        DataFormatter dataFormatter = new DataFormatter();

            int tableColumn = 1;
            if(column == SECOND_COLUMN) tableColumn += 15;

            Iterator<Cell> cellIterator = row.cellIterator();
            String cellValue = "";

            int counter = 0;
            while (cellIterator.hasNext() && counter < tableColumn) {
                Cell cell = cellIterator.next();
                cellValue = dataFormatter.formatCellValue(cell);
                counter++;
            }

            if(cellValue.equals("№")) return true;

            return false;

    }


    private static boolean isThereTrainLine(Row row, int column){

            DataFormatter dataFormatter = new DataFormatter();

            int tableColumn = 3;
            if(column == SECOND_COLUMN) tableColumn += 15;

            Iterator<Cell> cellIterator = row.cellIterator();

            String cellValue = "";
            int counter = 0;
            while (cellIterator.hasNext() && counter < tableColumn) {

                Cell cell = cellIterator.next();
                cellValue = dataFormatter.formatCellValue(cell);
                counter++;
            }

            if (cellValue.contains("ТРАМВАЙНА ЛИНИЯ")) return true;

            return false;


    }

    private static boolean isThereBusLine(Row row, int column){

            DataFormatter dataFormatter = new DataFormatter();

            int tableColumn = 3;
            if(column == SECOND_COLUMN) tableColumn += 15;

            Iterator<Cell> cellIterator = row.cellIterator();

            String cellValue = "";
            int counter = 0;
            while (cellIterator.hasNext() && counter < tableColumn) {

                Cell cell = cellIterator.next();
                cellValue = dataFormatter.formatCellValue(cell);
                counter++;
            }

            if (cellValue.contains("АВТОБУСНА ЛИНИЯ") || cellValue.contains("А ЛИНИЯ")) return true;

            return false;



    }


    private static boolean isThereTrolleyLine(Row row, int column){
        DataFormatter dataFormatter = new DataFormatter();

        int tableColumn = 3;
        if(column == SECOND_COLUMN) tableColumn += 15;

        Iterator<Cell> cellIterator = row.cellIterator();

        String cellValue = "";
        int counter = 0;
        while (cellIterator.hasNext() && counter < tableColumn) {

            Cell cell = cellIterator.next();
            cellValue = dataFormatter.formatCellValue(cell);
            counter++;
        }

        if(cellValue.contains("ТРОЛЕЙБУСНА  ЛИНИЯ")) return true;

        return false;
    }


    private static String getTransportNumber(Row row, int column){
        DataFormatter dataFormatter = new DataFormatter();

        int tableColumn = 3;
        if(column == SECOND_COLUMN) tableColumn += 15;

        Iterator<Cell> cellIterator = row.cellIterator();

        String cellValue = "";
        int counter = 0;
        while (cellIterator.hasNext() && counter < tableColumn) {

            Cell cell = cellIterator.next();
            cellValue = dataFormatter.formatCellValue(cell);
            counter++;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(int index = cellValue.length() - 1; cellValue.charAt(index) != '№'; index--){
            stringBuilder.append(cellValue.charAt(index));
        }

        return stringBuilder.reverse().toString();
    }

    private static boolean endOfTable(Row row, int column){
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Cell> cellIterator = row.cellIterator();

        String cellValue = "";
        int counter = 0;
        while (cellIterator.hasNext() && counter < 18) {

            Cell cell = cellIterator.next();

            if(column == SECOND_COLUMN && counter < 15){
                counter++;
                continue;
            }


            cellValue = dataFormatter.formatCellValue(cell);
            counter++;
            if(cellValue.contains("ОБЩО")) return true;
        }

        return false;
    }





}

package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import static org.apache.poi.ss.usermodel.CellType.STRING;

public class Main{
    public static void main(String[] args) {
        String inputFilePath = "E:\\Java\\p2\\src\\main\\resources\\Data.xlsx";
        String outputFilePath = "E:\\Java\\p2\\src\\main\\resources\\Processed.xlsx";

        try {
            Workbook workbook = readExcelFile(inputFilePath);

            // Process data using Java streams
            processExcelData(workbook);

            // Write the processed data to a new Excel file
            writeExcelFile(workbook, outputFilePath);

            // Insert data into the database
//            insertIntoDatabase(workbook);
            insertIntoDatabase();

            System.out.println("Excel processing and database insertion completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Solve();
    }

    private static Workbook readExcelFile(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            return WorkbookFactory.create(fileInputStream);
        }
    }

    private static void processExcelData(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);

        // Use Java streams to filter rows with null values
        StreamSupport.stream(sheet.spliterator(), false)
                .skip(1) // Skip header row
                .filter(row -> StreamSupport.stream(row.spliterator(), false)
                        .noneMatch(cell -> cell.getCellType() == CellType.BLANK))
                .forEach(row -> {
                    // If you need to perform additional processing, do it here
                    // For example, you can access cell values using row.getCell(index).getStringCellValue()
                });

        // Remove rows with null values (if needed, modify the code accordingly)
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (StreamSupport.stream(row.spliterator(), false)
                    .anyMatch(cell -> cell.getCellType() == CellType.BLANK)) {
                rowIterator.remove();
            }
        }
    }

    private static void writeExcelFile(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        }
    }

//    private static void insertIntoDatabase(Workbook workbook) throws SQLException {
//        // Replace the following database connection details with your actual information
//        String url = "jdbc:mysql://localhost:3306/p2";
//        String username = "root";
//        String password = "letmein";
//
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            String insertQuery = "insert into interviews (interviewdate, team, panelname, interviewround, skill, interviewtime, candidate_cur_loc, candidate_pref_loc, candidate_name)\n"
//                    + "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
//                Sheet sheet = workbook.getSheetAt(0);
//
//                for (Row row : sheet) {
//                    // Assuming each row has three columns; adjust as per your actual structure
//                    preparedStatement.setString(1, row.getCell(0).getStringCellValue());
//                    preparedStatement.setString(2, row.getCell(1).getStringCellValue());
//                    preparedStatement.setString(3, row.getCell(2).getStringCellValue());
//                    preparedStatement.setString(4, row.getCell(3).getStringCellValue());
//                    preparedStatement.setString(5, row.getCell(4).getStringCellValue());
//                    preparedStatement.setString(6, row.getCell(5).getStringCellValue());
//                    preparedStatement.setString(7, row.getCell(6).getStringCellValue());
//                    preparedStatement.setString(8, row.getCell(7).getStringCellValue());
//                    preparedStatement.setString(9, row.getCell(8).getStringCellValue());
//                    // Add more parameters if you have additional columns
//
//                    preparedStatement.addBatch();
//                }
//
//                preparedStatement.executeBatch();
//            }
//        }
//    }


//    public static void insertIntoDatabase(){
//        try{
//            FileInputStream file = new FileInputStream("E:\\Java\\p2\\src\\main\\resources\\Data.xlsx");
//
//            //Create Workbook instance holding reference to .xlsx file
//            XSSFWorkbook wb = new XSSFWorkbook(file);
//            XSSFSheet ws = wb.getSheetAt(0);
//            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
//            DateFormat df2 = new SimpleDateFormat("hh:mm:ss");
//            //Iterate through each rows one by one
//            Iterator<Row> rowIterator = ws.iterator();
//            rowIterator.next(); //skipping 1st row which has headings
//
//            String url = "jdbc:mysql://localhost:3306/p2";
//            String username = "root";
//            String password = "letmein";
//            Connection con = DriverManager.getConnection(url, username, password);
//            con.setAutoCommit(false);
//            PreparedStatement p = null;
//            String sql =
//                    "insert into interviews (interviewdate, team, panelname, interviewround, skill, interviewtime, candidate_cur_loc, candidate_pref_loc, candidate_name)\n"
//                            + "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
//            p = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//
//            ResultSet rs = null;
//
//            while (rowIterator.hasNext()){
//
//                Row row = rowIterator.next();
//                if(row.getCell(0).getCellType() == STRING || row.getCell(6).getCellType() == STRING){
//                    continue;
//                }
//                try{
//                    String cellDate = df1.format(row.getCell(0).getDateCellValue());
//                    String team = row.getCell(2).getStringCellValue();
//                    String panel = row.getCell(3).getStringCellValue();
//                    String round = row.getCell(4).getStringCellValue();
//                    String skill = row.getCell(5).getStringCellValue();
//                    String time = cellDate + " " + df2.format(row.getCell(6).getDateCellValue());
//                    String candidate_cur_loc = row.getCell(7).getStringCellValue();
//                    String candidate_pref_loc = row.getCell(8).getStringCellValue();
//                    String candidate_name = row.getCell(9).getStringCellValue();
////                    System.out.print(cellDate+" "+team+" "+panel+" "+round+" "+skill+" "+time+" ");
////                    System.out.print(candidate_cur_loc+" "+candidate_pref_loc+" "+candidate_name);
//
//                    //------------------------------------Inserting Data into Database---------------------------------------------//
//                    p.setString(1, cellDate);
//                    p.setString(2, team);
//                    p.setString(3, panel);
//                    p.setString(4, round);
//                    p.setString(5, skill);
//                    p.setString(6, time);
//                    p.setString(7, candidate_cur_loc);
//                    p.setString(8, candidate_pref_loc);
//                    p.setString(9, candidate_name);
//                    //---------------------------------------------------------------------------------//
//
//                }catch (NullPointerException newe){
//                    continue;
//                }
//                p.addBatch();
////                System.out.println();
//            }
//            int[] updateCounts = p.executeBatch();
//            con.commit();
//            con.setAutoCommit(true);
//            System.out.println(updateCounts.length + " rows inserted");
//
//            file.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    public static void insertIntoDatabase(){
        try {
            FileInputStream file = new FileInputStream("E:\\Java\\p2\\src\\main\\resources\\Data.xlsx");

            // Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet ws = wb.getSheetAt(0);
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat df2 = new SimpleDateFormat("hh:mm:ss");

            // Convert the iterator to Iterable to use parallelStream()
            Iterable<Row> rowIterable = () -> ws.iterator();

            // Iterate through each row in parallel
            StreamSupport.stream(rowIterable.spliterator(), true) // true enables parallel processing
                    .skip(1) // Skip the first row (header)
                    .forEach(row -> {
                        try {
                            if (row.getCell(0).getCellType() == STRING || row.getCell(6).getCellType() == STRING) {
                                return;
                            }

                            String cellDate = df1.format(row.getCell(0).getDateCellValue());
                            String team = row.getCell(2).getStringCellValue();
                            String panel = row.getCell(3).getStringCellValue();
                            String round = row.getCell(4).getStringCellValue();
                            String skill = row.getCell(5).getStringCellValue();
                            String time = cellDate + " " + df2.format(row.getCell(6).getDateCellValue());
                            String candidate_cur_loc = row.getCell(7).getStringCellValue();
                            String candidate_pref_loc = row.getCell(8).getStringCellValue();
                            String candidate_name = row.getCell(9).getStringCellValue();

                            // Inserting Data into Database
                            insertRowIntoDatabase(cellDate, team, panel, round, skill, time,
                                    candidate_cur_loc, candidate_pref_loc, candidate_name);

                        } catch (NullPointerException newe) {
                            // Handle null values if necessary
                        }
                    });

            file.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void insertRowIntoDatabase(String cellDate, String team, String panel,
                                              String round, String skill, String time,
                                              String candidate_cur_loc, String candidate_pref_loc,
                                              String candidate_name) {
        // Replace the following database connection details with your actual information
        String url = "jdbc:mysql://localhost:3306/p2";
        String username = "root";
        String password = "letmein";

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            con.setAutoCommit(false);
            String sql = "INSERT INTO interviews (interviewdate, team, panelname, interviewround, skill, interviewtime, candidate_cur_loc, candidate_pref_loc, candidate_name) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement p = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                p.setString(1, cellDate);
                p.setString(2, team);
                p.setString(3, panel);
                p.setString(4, round);
                p.setString(5, skill);
                p.setString(6, time);
                p.setString(7, candidate_cur_loc);
                p.setString(8, candidate_pref_loc);
                p.setString(9, candidate_name);

                p.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

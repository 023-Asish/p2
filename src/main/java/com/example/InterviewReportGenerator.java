package com.example;
//
//// Import statements...
//import com.itextpdf.text.*;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import org.krysalis.jcharts.axisChart.AxisChart;
//import org.krysalis.jcharts.chartData.AxisChartDataSet;
//import org.krysalis.jcharts.chartData.ChartDataException;
//import org.krysalis.jcharts.chartData.DataSeries;
//import org.krysalis.jcharts.encoders.JPEGEncoder;
//import org.krysalis.jcharts.properties.*;
//import org.krysalis.jcharts.test.TestDataGenerator;
//import org.krysalis.jcharts.types.ChartType;
//
//import java.awt.*;
//import java.io.*;
//import java.sql.*;
//
//public class InterviewReportGenerator {
//
//    private static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
//    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
//    private static final Font TABLE_HEADER_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
//
//    public static void main(String[] args) {
//        Document document = new Document();
//
//        try {
//            PdfWriter.getInstance(document, new FileOutputStream("E:\\Java\\p2\\visualization_report.pdf"));
//            document.open();
//
//            addMetaData(document);
//            addTitlePage(document, "Interview Report");
//
////            Connection connection = DataSourceToLocal.getConnection();
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/p2", "root", "letmein");
//
//
//            // Question 1
//            addSection(document, connection, "Team with Maximum Interviews");
//
//            // Question 2
//            addSection(document, connection, "Team with Minimum Interviews");
//
//            // Question 3
//            addChartSection(document, connection, "Top 3 Panels for Oct-Nov 2023", "panelname");
//
//            // Question 4
//            addChartSection(document, connection, "Top 3 Skills for Oct-Nov 2023", "skill");
//
//            // Question 5
//            addChartSection(document, connection, "Top 3 Skills in Peak Time", "skill");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            document.close();
//        }
//    }
//
//    private static void addMetaData(Document document) {
//        document.addTitle("Interview Report");
//        document.addSubject("Using iText and jCharts");
//        document.addKeywords("Java, PDF, iText, jCharts");
//        document.addAuthor(System.getProperty("user.name"));
//        document.addCreator(System.getProperty("user.name"));
//    }
//
//    private static void addTitlePage(Document document, String title) throws DocumentException {
//        Paragraph titleParagraph = new Paragraph();
//        addEmptyLine(titleParagraph, 1);
//        titleParagraph.add(new Paragraph(title, TITLE_FONT));
//        addEmptyLine(titleParagraph, 1);
//        document.add(titleParagraph);
//    }
//
//    private static void addSection(Document document, Connection connection, String sectionTitle) throws DocumentException, SQLException {
//        addHeading(document, sectionTitle);
//
//        String sql = "SELECT team, COUNT(*) AS total_interviews FROM interviews\n" +
//                "WHERE interviewdate BETWEEN '2023-10-01' AND '2023-11-30'\n" +
//                "GROUP BY team ORDER BY COUNT(*) DESC LIMIT 1;";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
//             ResultSet resultSet = preparedStatement.executeQuery()) {
////            String total_interviews = resultSet.getString("total_interviews");
//            createTable(document, new String[]{"Team", resultSet.getString("total_interviews")}, resultSet);
//        }
//        document.add(new Paragraph("\n"));
//    }
//
//    private static void addChartSection(Document document, Connection connection, String sectionTitle, String columnName) throws DocumentException, SQLException {
//        addHeading(document, sectionTitle);
//
//        String sql = "SELECT " + columnName + ", COUNT(*) AS no_of_interviews FROM interviews\n" +
//                "WHERE interviewdate BETWEEN '2023-10-01' AND '2023-11-30'\n" +
//                "GROUP BY " + columnName + " ORDER BY no_of_interviews DESC LIMIT 3;";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
//             ResultSet resultSet = preparedStatement.executeQuery()) {
//
//            AxisChart chart = getChart(columnName, resultSet);
//            Image chartImage = chartToImage(chart);
//            document.add(chartImage);
//            document.newPage();
//        }
//    }
//
//    private static void addHeading(Document document, String subtitle) throws DocumentException {
//        Paragraph headingParagraph = new Paragraph();
//        addEmptyLine(headingParagraph, 1);
//        headingParagraph.add(new Paragraph(subtitle, SUBTITLE_FONT));
//        addEmptyLine(headingParagraph, 1);
//        document.add(headingParagraph);
//    }
//
//    private static void createTable(Document document, String[] headings, ResultSet resultSet) throws DocumentException, SQLException {
//        PdfPTable table = new PdfPTable(headings.length);
//        table.setPaddingTop(2);
//
//        for (String heading : headings) {
//            PdfPCell headerCell = new PdfPCell(new Phrase(heading, TABLE_HEADER_FONT));
//            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(headerCell);
//        }
//
//        while (resultSet.next()) {
//            for (String heading : headings) {
//                PdfPCell cell = new PdfPCell(new Phrase(resultSet.getString(heading)));
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//            }
//        }
//
//        document.add(table);
//    }
//
//    private static void addEmptyLine(Paragraph paragraph, int number) {
//        for (int i = 0; i < number; i++) {
//            paragraph.add(new Paragraph(" "));
//        }
//    }
//
//    private static AxisChart getChart(String xAxisTitle, ResultSet resultSet) throws SQLException {
//        String[] xAxisLabels = {"Label 1", "Label 2", "Label 3"};  // Replace with your actual labels
//        String yAxisTitle = "Interview Count";
//        String title = " "; // Chart title
//        DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);
//
//        double[][] data = new double[1][3];  // Assuming three labels for simplicity
//        int i = 0;
//        while (resultSet.next() && i < 3) {
//            xAxisLabels[i] = resultSet.getString(1);
//            data[0][i] = resultSet.getInt("no_of_interviews");
//            i++;
//        }
//
//        String[] legendLabels = {""};
//        Paint[] paints = TestDataGenerator.getRandomPaints(1);
//        BarChartProperties barChartProperties = new BarChartProperties();
//        AxisChartDataSet axisChartDataSet = null;
//        try {
//            axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints, ChartType.BAR, barChartProperties);
//        } catch (ChartDataException e) {
//            throw new RuntimeException(e);
//        }
//        dataSeries.addIAxisPlotDataSet(axisChartDataSet);
//
//        ChartProperties chartProperties = new ChartProperties();
//        AxisProperties axisProperties = new AxisProperties();
//        LegendProperties legendProperties = new LegendProperties();
//        return new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 450, 280);
//    }
//
//    private static Image chartToImage(AxisChart chart) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            JPEGEncoder.encode(chart, 1, outputStream);
//            return Image.getInstance(outputStream.toByteArray());
//        } catch (ChartDataException | PropertyException | IOException | BadElementException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.krysalis.jcharts.axisChart.AxisChart;
import org.krysalis.jcharts.chartData.AxisChartDataSet;
import org.krysalis.jcharts.chartData.ChartDataException;
import org.krysalis.jcharts.chartData.DataSeries;
import org.krysalis.jcharts.encoders.JPEGEncoder;
import org.krysalis.jcharts.properties.*;
import org.krysalis.jcharts.test.TestDataGenerator;
import org.krysalis.jcharts.types.ChartType;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class InterviewReportGenerator {
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    public static void main(String args[]) {

        Document document = new Document();
        //Question 1
        try{
            PdfWriter.getInstance(document, new FileOutputStream("E:\\\\Java\\\\p2\\\\visualization_report.pdf"));
            document.open();
            addMetaData(document);
            addTitlePage(document, "Interview Report");

//            Connection con = DataSourceToLocal.getConnection();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/p2", "root", "letmein");
            PreparedStatement p = null;
            ResultSet rs = null;
            addHeading(document, "Team with Maximum Interviews");
            String sql = "select team, count(*) as total_interviews from interviews\n" +
                    "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                    "group by team order by count(*) desc limit 1;";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();
            System.out.println();

            // Condition check
            while (rs.next()) {
                String team = rs.getString("team");
                String total_interviews = rs.getString("total_interviews");
//                System.out.println(team+*-*-" "+total_interviews);
                createTable(document, new String[]{"Team", "Total Interviews"}, new String[][]{new String[]{team, total_interviews}});
            }

            document.add(new Paragraph("\n"));
            //Question 2
            addHeading(document, "Team with Minimum Interviews");
            sql = "select team, count(*) as total_interviews from interviews\n" +
                    "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                    "group by team order by count(*) limit 1;";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();
            System.out.println();
            // Condition check
            while (rs.next()) {
                String team = rs.getString("team");
                String total_interviews = rs.getString("total_interviews");
//                System.out.println(team+" "+total_interviews);
                createTable(document, new String[]{"Team", "Total Interviews"}, new String[][]{new String[]{team, total_interviews}});
            }
//            document.add(new Paragraph("\n"));
            //Question 3
            addHeading(document, "Top 3 Panels for the month of October and November 2023");

            sql = "select panelname, count(*) as no_of_interviews from interviews\n" +
                    "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                    "group by panelname order by no_of_interviews desc limit 3;";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            AxisChart my_output_chart = getChart("Panel Names", rs, "panelname");

            //----------------------

//
//            String[] xAxisLabels = new String[3];  // Assuming three labels for simplicity
//            String yAxisTitle = "Interview Count";
//            String title = " "; // Chart title
//            DataSeries dataSeries = new DataSeries(xAxisLabels, "Panel name", yAxisTitle, title);
//
//            double[][] data = new double[1][3];  // Assuming three labels for simplicity
//            int i = 0;
//            while (rs.next() && i < 3) {
//                xAxisLabels[i] = rs.getString(1);
//                data[0][i] = rs.getInt("no_of_interviews");
//                i++;
//            }
//
//            String[] legendLabels = {""};
//            Paint[] paints = TestDataGenerator.getRandomPaints(1);
//            PieChartProperties pieChartProperties = new PieChartProperties();
//            AxisChartDataSet axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints, ChartType.PIE, pieChartProperties);
//            dataSeries.addIAxisPlotDataSet(axisChartDataSet);
//
//            ChartProperties chartProperties = new ChartProperties();
//            AxisProperties axisProperties = new AxisProperties();
//            LegendProperties legendProperties = new LegendProperties();
//            AxisChart t=  new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 450, 280);
//
//
//
//




            //---------------------------------------
            Image newimage = chartToImage(my_output_chart);
            document.add(newimage);
            document.newPage();

            //Question 4
            addHeading(document, "Top 3 Skills for the month of October and November 2023");

            sql = "select skill, count(*) as no_of_interviews from interviews\n" +
                    "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                    "group by skill order by no_of_interviews desc limit 3;";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            my_output_chart = getChart("Panel Names", rs, "skill");

            newimage = chartToImage(my_output_chart);
            document.add(newimage);
//            document.add(new Paragraph("\n"));

            //Question 5
            addHeading(document, "Top 3 Skills for which the interviews were conducted in the\n" +
                    "Peak Time");

            sql = "select skill, count(skill) as no_of_interviews from interviews where month(interviewdate) =\n" +
                    "(\n" +
                    "with month_with_interview_count as\n" +
                    "(select month(interviewdate) as peak, count(*) as interviewcount from interviews group by month(interviewdate))\n" +
                    "select peak from month_with_interview_count where interviewcount = (select max(interviewcount) from month_with_interview_count)\n" +
                    ")\n" +
                    "group by skill order by no_of_interviews desc limit 3;";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            my_output_chart = getChart("Skill", rs, "skill");

            newimage = chartToImage(my_output_chart);
            document.add(newimage);
            document.add(new Paragraph("\n"));

        }catch (SQLException | DocumentException | FileNotFoundException e){
            e.printStackTrace();
        }finally {
            document.close();
        }

    }
    public static AxisChart getChart(String xAxisTitle, ResultSet rs, String heading){
        String[] xAxisLabels = {"Team 1", "Team 2", "Team 3"};
//        String xAxisTitle = "Panel Names";
        String yAxisTitle = "Interview Count"; /*Y-Axis label */
        String title = " "; /* Chart title */
        DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);

        double[][] data = new double[][]{{0, 0, 0}};
        int i = 0;
        while(true){
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                xAxisLabels[i] = rs.getString(heading);
                data[0][i] = rs.getInt("no_of_interviews");
                i += 1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        String[] legendLabels = {" "};
        Paint[] paints = TestDataGenerator.getRandomPaints(1);
        BarChartProperties barChartProperties = new BarChartProperties();
        AxisChartDataSet axisChartDataSet = null;
        try {
            axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints, ChartType.BAR, barChartProperties);
        } catch (ChartDataException e) {
            throw new RuntimeException(e);
        }
        dataSeries.addIAxisPlotDataSet(axisChartDataSet);

        /* Step -3 - Create the chart */
        ChartProperties chartProperties = new ChartProperties(); /* Special chart properties, if any */
        AxisProperties axis_Properties = new AxisProperties();
        LegendProperties legend_Properties = new LegendProperties(); /* Dummy Axis and legend properties class */
        return new AxisChart(dataSeries, chartProperties, axis_Properties, legend_Properties, 450, 280); /* Create Chart object */
    }
    public static Image chartToImage(AxisChart chart){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            JPEGEncoder.encode(chart, 1, outputStream);
            return new Jpeg(outputStream.toByteArray());
        } catch (ChartDataException | PropertyException | IOException | BadElementException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("iText PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText, jCharts");
        document.addAuthor(System.getProperty("user.name"));
        document.addCreator(System.getProperty("user.name"));
    }

    private static void addTitlePage(Document document, String str)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph(str, catFont));

        addEmptyLine(preface, 1);
        document.add(preface);
    }
    private static void addHeading(Document document, String str)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph(str, subFont));

        addEmptyLine(preface, 1);
        document.add(preface);
    }

    private static void createTable(Document document, String[] headings, String[][] rows)
            throws DocumentException {
        Paragraph paragraph = new Paragraph();
        int cols = rows[0].length;
        PdfPTable table = new PdfPTable(cols);
        table.setPaddingTop(2);

        for(int i = 0; i < headings.length; i++){
            PdfPCell c1 = new PdfPCell(new Phrase(headings[i]));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        table.setHeaderRows(1);
        for(int i = 0; i < rows.length; i++){
            for(int j = 0; j < cols; j++){
                PdfPCell cell = new PdfPCell(new Phrase(rows[i][j]));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);
            }
        }
        paragraph.add(table);
        document.add(paragraph);
    }
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
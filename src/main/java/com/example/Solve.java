package com.example;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Solve {

    public static void main(String[] args) {
        try {
            // Connect to the database (replace with your database details)
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/p2", "root", "letmein");

            // Create a PDF document
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("visualization_report.pdf"));
            document.open();

            // Query and visualize the team with maximum interviews
            visualizeMaxInterviews(connection, document);

            // Query and visualize the team with minimum interviews
            visualizeMinInterviews(connection, document);

            // Query and visualize the top 3 panels
            visualizeTop3Panels(connection, document);

            // Query and visualize the top 3 skills
            visualizeTop3Skills(connection, document);

            // Query and visualize the top 3 skills during peak time
            visualizeTop3SkillsDuringPeakTime(connection, document);

            // Close the document and the database connection
            document.close();
            connection.close();

        } catch (SQLException | DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void visualizeMaxInterviews(Connection connection, Document document) throws SQLException, DocumentException {
        // Your SQL query for question 1
        String query = "select team, count(*) as total_interviews from interviews\n" +
                "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                "group by team order by count(*) desc limit 1;";

        ResultSet resultSet = connection.createStatement().executeQuery(query);

        // Visualize the result using a pie chart
        visualizeResultSetAsPieChart(resultSet, document, "Team with Maximum Interviews");
    }

    private static void visualizeMinInterviews(Connection connection, Document document) throws SQLException, DocumentException {
        // Your SQL query for question 2
        String query = "select team, count(*) as total_interviews from interviews\n" +
                "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                "group by team order by count(*) limit 1;";

        ResultSet resultSet = connection.createStatement().executeQuery(query);

        // Visualize the result using a pie chart
        visualizeResultSetAsPieChart(resultSet, document, "Team with Minimum Interviews");
    }

    private static void visualizeTop3Panels(Connection connection, Document document) throws SQLException, DocumentException {
        // Your SQL query for question 3
        String query = "select panelname, count(*) as no_of_interviews from interviews\n" +
                "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                "group by panelname order by no_of_interviews desc limit 3;";

        ResultSet resultSet = connection.createStatement().executeQuery(query);

        // Visualize the result using a pie chart
        visualizeResultSetAsPieChart(resultSet, document, "Top 3 Panels");
    }

    private static void visualizeTop3Skills(Connection connection, Document document) throws SQLException, DocumentException {
        // Your SQL query for question 4
        String query = "select skill, count(*) as no_of_interviews from interviews\n" +
                "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                "group by skill order by no_of_interviews desc limit 3;";

        ResultSet resultSet = connection.createStatement().executeQuery(query);

        // Visualize the result using a pie chart
        visualizeResultSetAsPieChart(resultSet, document, "Top 3 Skills");
    }

    private static void visualizeTop3SkillsDuringPeakTime(Connection connection, Document document) throws SQLException, DocumentException {
        // Your SQL query for question 5
        String query = "select skill, count(skill) as no_of_interviews from interviews where month(interviewdate) =\n" +
                "(\n" +
                "with month_with_interview_count as\n" +
                "(select month(interviewdate) as peak, count(*) as interviewcount from interviews group by month(interviewdate))\n" +
                "select peak from month_with_interview_count where interviewcount = (select max(interviewcount) from month_with_interview_count)\n" +
                ")\n" +
                "group by skill order by no_of_interviews desc limit 3;";

        ResultSet resultSet = connection.createStatement().executeQuery(query);

        // Visualize the result using a pie chart
        visualizeResultSetAsPieChart(resultSet, document, "Top 3 Skills During Peak Time");
    }

    private static void visualizeResultSetAsPieChart(ResultSet resultSet, Document document, String chartTitle)
            throws SQLException, DocumentException {
        DefaultPieDataset dataset = new DefaultPieDataset();

        while (resultSet.next()) {
            String category = resultSet.getString(1);
            int value = resultSet.getInt(2);
            dataset.setValue(category, value);
        }

        // Create a pie chart from the dataset
        JFreeChart pieChart = ChartFactory.createPieChart(
                chartTitle,
                dataset,
                true,
                true,
                false
        );

        // Save the chart as an image
        String imagePath = saveChartAsImage(pieChart, chartTitle.replaceAll("\\s", "") + "_chart.png",document);

        // Add the chart image to the PDF document
        document.add(createImage(imagePath));
        document.newPage();  // Move to a new page for the next visualization
    }

    private static String saveChartAsImage(JFreeChart chart, String filename,Document document) {
        try {
//            ChartPanel chartPanel = new ChartPanel(chart);
//            chartPanel.setSize(500, 500);
//
//            FileOutputStream fos = new FileOutputStream(filename);
//
//            chartPanel.createImage(500, 500)
//
//            Image chartImage = Image.getInstance(chartPanel.createImage(500,500));
//            chartImage.scalePercent(75); // Adjust the scale if needed
//            chartImage.setAlignment(Image.MIDDLE);
//            document.add(chartImage);
//
//            fos.close();
//            return filename;
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setSize(500, 500);

            BufferedImage chartImageBuffer = (BufferedImage) chartPanel.createImage(500, 500);
            if (chartImageBuffer != null) {
                FileOutputStream fos = new FileOutputStream(filename);
                Image chartImage = Image.getInstance(chartImageBuffer, null);
                chartImage.scalePercent(75); // Adjust the scale if needed
                chartImage.setAlignment(Image.MIDDLE);
                document.add(chartImage);

                fos.close();
                return filename;
            } else {
                System.out.println("Chart image is null. Check your chart creation process.");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Image createImage(String imagePath) {
        try {
            return Image.getInstance(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
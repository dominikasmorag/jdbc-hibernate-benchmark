package com.smorag.jdbc_hibernate_benchmark.utils;

import enums.QueryType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    private final String fileName;
    private static final String REPORTS_PATH = "D:\\Studia\\jdbc-hibernate-benchmark\\jdbc-hibernate-benchmark\\jdbc-hibernate-benchmark\\reports\\";

    public CsvWriter(String fileName) {
        this.fileName = fileName;
    }

    public void initColumnNames(String columnNames) {
        appendRow(",");
        appendRow(columnNames);
    }

    public void appendRow(int iteration, String framework, String entityName, QueryType queryType, int recordsSize, long duration, String method) {
        String csvLine = iteration + "," + framework + "," + entityName + "," + queryType + "," + recordsSize + "," + method + "," + duration;
        try {
            File file = new File(REPORTS_PATH + fileName);
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.append(csvLine +"\n");
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void appendRow(String csvLine) {
        try {
            File file = new File(REPORTS_PATH + fileName);
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.append(csvLine +"\n");
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

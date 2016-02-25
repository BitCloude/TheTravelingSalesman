package com.appers.ayvaz.thetravelingsalesman.utils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by D on 023 02 23.
 */
public class MyCsvWriter extends FileWriter {
    public MyCsvWriter(File file) throws IOException {
        super(file);
    }

    final String DIVIDER = ", ";
    final String NEW_LINE = "\n";

    public void addRow(String[] row) throws IOException {
        for (String s : row) {
            append(StringEscapeUtils.escapeCsv(s));
            append(DIVIDER);
        }
        append(NEW_LINE);
    }
}

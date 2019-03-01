package com.trentfaris.randopt;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import shared.Instance;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class Utility {

  private Utility() {}

  public static Instance[] getInstances(String file) throws IOException {
    Reader reader = Files.newBufferedReader(Paths.get(file));
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

    List<CSVRecord> csvRecords = csvParser.getRecords();

    Instance[] instances = new Instance[csvRecords.size()];
    for (int i = 0; i < csvRecords.size(); i++) {
      CSVRecord csvRecord = csvRecords.get(i);

      double[] data = new double[csvRecord.size()];
      for (int j = 0; j < csvRecord.size() - 1; j++) {
        data[j] = Double.parseDouble(csvRecord.get(j));
      }

      int label = Math.round(Float.parseFloat(csvRecord.get(csvRecord.size() - 1)));
      if (label == -1) {
        label = 0;
      }

      Instance instance = new Instance(data);
      instance.setLabel(new Instance(label));

      instances[i] = instance;
    }

    return instances;
  }
}

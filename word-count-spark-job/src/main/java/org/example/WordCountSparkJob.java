package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.*;


public class WordCountSparkJob {
    public static String getWordCount(String contents, SparkSession sparkSession) {
        Dataset<String> text = sparkSession.createDataset(Arrays.asList(contents.split("[\r\n]+")), Encoders.STRING()).repartition(16);
        Dataset<String> wordsDataset = text.flatMap((FlatMapFunction<String, String>) line -> {
            List<String> words = new ArrayList<>();
            for (String word : line.split(" ")) {
                String normalizedWord = word.replaceAll("[^a-zA-Z0-9]", "").trim().toLowerCase();
                if (!normalizedWord.isEmpty()) {
                    words.add(normalizedWord);
                }
            }
            return words.iterator();
        }, Encoders.STRING());

        // Count the number of occurrences of each word
        Dataset<Row> wordCounts = wordsDataset.groupBy("value")
                .agg(count("*").as("count"))
                .orderBy(desc("count"));

        // Convert the word count results to a List of Rows
        List<Row> wordCountsList = wordCounts.collectAsList();

        StringBuilder resultStringBuffer = new StringBuilder();

        // Build the final string representation
        for (Row row : wordCountsList) {
            resultStringBuffer.append(row.getString(0)).append(": ").append(row.getLong(1)).append("\n");
        }

        return resultStringBuffer.toString();
    }
}

package com.hbaker;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TrafficCounterSummaryTest {

    @Test
    void givenGoodInputDataWhenSummaryConstructedThenCreateSuccessfully() throws Exception {
        List<String> sampleInput = readResourceFile("sample.txt");

        assertDoesNotThrow(() -> {
            new TrafficCounterSummary(sampleInput);
        });
    }

    @Test
    void givenBadInputDataWhenSummaryConstructedThenThrowException() {
        List<String> sampleInput = Collections.singletonList("ABCDEFG");

        assertThrows(IllegalArgumentException.class, () -> {
            new TrafficCounterSummary(sampleInput);
        });
    }

    @Test
    void givenGoodInputWhenTotalCalledThenOutputCorrectTotal() throws Exception {
        List<String> sampleInput = readResourceFile("sample2.txt");
        TrafficCounterSummary summary = new TrafficCounterSummary(sampleInput);

        assertEquals(253, summary.getTotal());
    }

    @Test
    void givenGoodInputWhenDaysCalledThenOutputCorrectLines() throws Exception {
        List<String> sampleInput = readResourceFile("sample2.txt");
        TrafficCounterSummary summary = new TrafficCounterSummary(sampleInput);

        List<String> expected = Arrays.asList(
                "2024-11-30 10",
                "2025-01-01 21",
                "2025-02-14 28",
                "2025-03-01 28",
                "2025-04-10 14",
                "2025-04-11 18",
                "2025-06-05 36",
                "2025-07-20 19",
                "2025-08-01 8",
                "2025-09-10 22",
                "2025-10-01 21",
                "2025-10-02 9",
                "2025-10-03 11",
                "2025-10-04 8"
        );

        assertArrayEquals(expected.toArray(),
                summary.getGrouped(TimeGroup.DAY).toArray());
    }

    @Test
    void givenGoodInputWhenTopNCalledThenOutputCorrectTopN() throws Exception {
        List<String> sampleInput = readResourceFile("sample2.txt");
        TrafficCounterSummary summary = new TrafficCounterSummary(sampleInput);

        List<String> expectedThree = Arrays.asList(
                "2025-09-10T03:30:00 22",
                "2025-10-01T04:00:00 21",
                "2025-06-05T07:30:00 20"
        );

        assertArrayEquals(expectedThree.toArray(),
                summary.getTopN(3).toArray());

        List<String> expectedFive = Arrays.asList(
                "2025-09-10T03:30:00 22",
                "2025-10-01T04:00:00 21",
                "2025-06-05T07:30:00 20",
                "2025-07-20T12:00:00 19",
                "2025-04-11T16:00:00 18"
        );

        assertArrayEquals(expectedFive.toArray(),
                summary.getTopN(5).toArray());
    }

    @Test
    void givenGoodInputWhenSmallestPeriodCalledThenOutputCorrectPeriods() throws Exception {
        List<String> sampleInput = readResourceFile("sample2.txt");
        TrafficCounterSummary summary = new TrafficCounterSummary(sampleInput);

        List<String> expectedThree = Arrays.asList(
                "2025-10-02T10:00:00 9",
                "2025-10-03T00:30:00 11",
                "2025-10-04T01:00:00 8"
        );

        assertArrayEquals(expectedThree.toArray(),
                summary.getSmallestPeriod(3).toArray());

        List<String> expectedFive = Arrays.asList(
                "2024-11-30T23:00:00 10",
                "2025-01-01T00:30:00 12",
                "2025-01-01T01:00:00 9",
                "2025-02-14T06:30:00 15",
                "2025-02-14T07:00:00 13"
        );

        assertArrayEquals(expectedFive.toArray(),
                summary.getSmallestPeriod(5).toArray());
    }

    private List<String> readResourceFile(String resourceName) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
}
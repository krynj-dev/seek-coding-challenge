package com.hbaker;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TrafficCounterSummary {
    private final Map<String, Integer> dateMap;

    private static final Pattern datePattern =
            Pattern.compile("(\\S+)\\s([0-9]+)");

    public TrafficCounterSummary(List<String> inputData) {
        dateMap = inputData.stream().map(this::mapToDateEntry).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue,
                Integer::sum, LinkedHashMap::new
        ));
    }

    private Map.Entry<String, Integer> mapToDateEntry(String rawData) {
        Matcher matcher = datePattern.matcher(rawData);
        if (matcher.find()) {
            String date = matcher.group(1);
            Integer count = Integer.valueOf(matcher.group(2));
            return new AbstractMap.SimpleEntry<>(date, count);
        }
        throw new IllegalArgumentException("Failed to parse line: " + rawData);
    }

    public List<String> getSmallestPeriod(Integer len) {
        int j = len;
        int runningTotal = 0;
        int min = 0;
        List<Map.Entry<String, Integer>> values = new ArrayList<>(dateMap.entrySet());
        for (int i = 0; i < values.size(); i++) {
            runningTotal += values.get(i).getValue();
            if (i < len){
                min += values.get(i).getValue();
            } else {
                runningTotal -= values.get(i-len).getValue();
                if (runningTotal < min) {
                    min = runningTotal;
                    j = i+1;
                }
            }
        }
        return values.subList(j-len, j).stream()
                .map(entry -> entry.getKey() + " " + entry.getValue()).collect(Collectors.toList());
    }

    public List<String> getTopN(Integer n) {
        PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(getComparator());
        maxHeap.addAll(dateMap.entrySet());
        List<String> topN = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Map.Entry<String, Integer> entry = maxHeap.poll();
            topN.add(entry.getKey() + " " + entry.getValue());
        }
        return topN;
    }

    private Comparator<Map.Entry<String, Integer>> getComparator() {
        return (o1, o2) -> Integer.compare(o2.getValue(), o1.getValue());
    }

    public Integer getTotal() {
        return dateMap.values().stream().reduce(0, Integer::sum);
    }

    public List<String> getGrouped(TimeGroup groupBy) {
        Pattern keyPattern;
        switch (groupBy) {
            case SECOND -> keyPattern = Pattern.compile("(.+)");
            case MINUTE -> keyPattern = Pattern.compile("(.+):00$");
            case HOUR -> keyPattern = Pattern.compile("(.+):00:00$");
            case DAY -> keyPattern = Pattern.compile("(.+)T.+");
            case MONTH -> keyPattern = Pattern.compile("([0-9]{4}-[0-9]{2}).+");
            case YEAR -> keyPattern = Pattern.compile("([0-9]{4}).+");
            default -> throw new IllegalStateException("Invalid groupBy.");
        }
        return dateMap.entrySet().stream().map(entry -> {
                    Matcher matcher = keyPattern.matcher(entry.getKey());
                    if (!matcher.find()) throw new IllegalStateException();
                    return new AbstractMap.SimpleEntry<>(matcher.group(1),
                            entry.getValue());
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                Integer::sum, LinkedHashMap::new)).entrySet()
                .stream().map(entry -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.toList());
    }

}

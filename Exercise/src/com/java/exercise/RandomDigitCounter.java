package com.java.exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class RandomDigitCounter {

	public static void main(String[] args) {
		Integer numberOfRandoms = validateAndExtractInput(args);
		if(numberOfRandoms == 0) {
			return;
		}
		Map<Integer, Integer> countOfDigits = countFirstDigitOccurances(numberOfRandoms);
		Map<Integer, Integer> sortedCountOfDigits = sortBaseOnCount(countOfDigits);
		for(Integer digit : sortedCountOfDigits.keySet()) {
			System.out.println("Digit : " + digit + " Key : " + sortedCountOfDigits.get(digit));
		}
	}

	private static Map<Integer, Integer> sortBaseOnCount(
			Map<Integer, Integer> countOfDigits) {
		List<Map.Entry<Integer, Integer>> entries = new ArrayList<Map.Entry<Integer,Integer>>(countOfDigits.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<Integer, Integer>>() {

			@Override
			public int compare(Entry<Integer, Integer> o1,
					Entry<Integer, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		HashMap<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
		for(Map.Entry<Integer, Integer> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	private static Map<Integer, Integer> countFirstDigitOccurances(Integer numberOfRandoms) {
		Map<Integer, Integer> countOfDigits = new HashMap<Integer, Integer>();
		for (int i = 0; i < numberOfRandoms; i++) {
			Random r = new Random();
			double d = 0.0 + r.nextDouble() * 1.0;
			int firstDigit = (int) (d * 10);
			if(countOfDigits.get(firstDigit) == null) {
				countOfDigits.put(firstDigit, 0);
			}
			countOfDigits.put(firstDigit, countOfDigits.get(firstDigit) + 1);
		}
		return countOfDigits;
	}

	private static Integer validateAndExtractInput(String[] args) {
		if (args.length == 0 || args.length > 1) {
			System.out
					.println("Incorrect number of arguments passed. Please pass the number of random numbers to generate.");
			return 0;
		}
		Integer numberOfRandoms = 0;
		try {
			numberOfRandoms = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out
					.println("Incorrect argument passed. Please pass a integer value.");
		}
		return numberOfRandoms;
	}
}

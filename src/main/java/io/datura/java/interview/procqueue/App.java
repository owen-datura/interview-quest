package io.datura.java.interview.procqueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class App {

	private static final String EVENT_START = "start";
	private static final String EVENT_STOP = "stop";

	public static void main(String[] args) {
		Path input = Paths.get(System.getProperty("user.home"), "stack-in.txt");
		try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
			Pattern tokenizer = Pattern.compile(",");

			int lastLevel = 0;
			int level = 0;
			String line = null;
			Deque<Process> pq = new ArrayDeque<>();
			LinkedList<Process> outputList = new LinkedList<>();
			LinkedList<Process> tempList = new LinkedList<>();
			while ((line = reader.readLine()) != null) {
				String[] event = tokenizer.split(line);

				if (EVENT_START.equals(event[0])) {
					Process p = new Process(event, level);
					pq.push(p);
					level++;
				} else if (EVENT_STOP.equals(event[0])) {
					Process p = pq.pop();
					p.setEndTimeStamp(event);
					level--;

					int curLevel = p.getLevel();
					if (curLevel == 0) {
						outputList.addFirst(p);
						break;
					} else if (curLevel >= lastLevel || tempList.isEmpty()) {
						tempList.addLast(p);
					} else {
						tempList.addFirst(p);
					}

					if (pq.peek().getLevel() == 0) {
						while (!tempList.isEmpty()) {
							outputList.addAll(tempList);
							tempList.clear();
						}
					}

					lastLevel = curLevel;
				}
			}

			System.out.println(generateOutput(outputList));
		} catch (IOException ioe) {
			System.err.println("Encountered an I/O error when processing input, stopping.");
			System.exit(1);
		}
	}

	private static StringBuilder generateOutput(LinkedList<Process> processList) {
		StringBuilder output = new StringBuilder();

		for (Process p : processList) {
			addIndentation(output, p.getLevel(), '-');
			output.append(p.getName());
			output.append(",");
			output.append(p.getExecutionTime().toString());
			output.append("\n");
		}

		return output;
	}

	private static void addIndentation(StringBuilder s, int num, char marker) {
		if (num == 0)
			return;

		for (int i = 0; i < num; i++)
			s.append(marker);
	}

}

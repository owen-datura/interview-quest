package io.datura.java.interview.procqueue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class App {

	private static final String EVENT_START = "start";
	private static final String EVENT_STOP = "stop";

	public static void main(String[] args) {
		try {
			Collection<Process> orderedEvents = evaluate(getEvents());
			StringBuilder output = generateOutput(orderedEvents);
			System.out.println(output);
		} catch (IOException ioe) {
			System.err.println("Encountered an I/O exception when processing files. Aborting.");
			System.exit(1);
		}
	}

	private static Collection<String> getEvents() throws IOException {
		Path input = Paths.get(System.getProperty("user.home"), "stack-in.txt");
		return Files.readAllLines(input, StandardCharsets.UTF_8);
	}

	public static LinkedList<Process> evaluate(Collection<String> events) {
		Pattern tokenizer = Pattern.compile(",");

		int lastLevel = 0;
		int level = 0;
		Deque<Process> pq = new ArrayDeque<>();
		LinkedList<Process> outputList = new LinkedList<>();
		LinkedList<Process> tempList = new LinkedList<>();
		for (String eventLine : events) {
			String[] event = tokenizer.split(eventLine);

			if (EVENT_START.equals(event[0])) {
				Process p = new Process(event, level);
				pq.push(p);
				level++;
			} else if (EVENT_STOP.equals(event[0])) {
				Process p = pq.pop();
				p.setEndTimeStamp(event[1]);
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

		return outputList;
	}

	private static StringBuilder generateOutput(Collection<Process> processList) {
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

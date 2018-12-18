package io.datura.java.interview.procqueue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.regex.Pattern;

public class AppRefactored {

	private static final String EVENT_START = "start";
	private static final String EVENT_STOP = "stop";

	public static void main(String[] args) {
		try {
			Process rootProcess = evaluate(getEvents());
			StringBuilder output = generateOutput(rootProcess);
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

	public static Process evaluate(Collection<String> events) {
		Pattern tokenizer = Pattern.compile(",");

		int level = 0;
		Process root = null;
		Deque<Process> pq = new ArrayDeque<>();
		for (String eventLine : events) {
			String[] event = tokenizer.split(eventLine);

			if (EVENT_START.equals(event[0])) {
				Process p = new Process(event, level);

				// since there isn't a notion of overlapping execution
				// happening on the same level (that is, if one process
				// is still running when we see another 'start' event,
				// it's implied that the new start is a subprocess of
				// the running process, not a sibling process), performing
				// a 'peek' at the stack will give us the process' parent
				if (pq.peek() != null)
					pq.peek().addChildProcess(p);
				else
					root = p;

				pq.push(p);
				level++;
			} else if (EVENT_STOP.equals(event[0])) {
				Process p = pq.pop();
				p.setEndTimeStamp(event);
				level--;
			}
		}

		return root;
	}

	public static StringBuilder generateOutput(Process process) {
		StringBuilder output = new StringBuilder();
		addChildrenToOutput(process, output);
		return output;
	}

	private static void addChildrenToOutput(Process parent, StringBuilder buffer) {
		if (parent == null)
			return;

		// output the parent line
		addIndentation(buffer, parent.getLevel(), '-');
		buffer.append(parent.getName());
		buffer.append(',');
		buffer.append(parent.getExecutionTime());
		buffer.append("\n");

		for (Process child : parent.getChildren())
			addChildrenToOutput(child, buffer);
	}

	private static void addIndentation(StringBuilder s, int num, char marker) {
		if (num == 0)
			return;

		for (int i = 0; i < num; i++)
			s.append(marker);
	}
}

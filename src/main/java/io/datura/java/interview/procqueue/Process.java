package io.datura.java.interview.procqueue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Process {
	private final String name;
	private final int level;
	private final BigInteger startTimeStamp;
	private BigInteger endTimeStamp;
	private final List<Process> children;

	public Process(String name, String startTime, int level) {
		this.name = name;
		this.startTimeStamp = new BigInteger(startTime.trim());
		this.level = level;
		this.children = new ArrayList<>(5);
	}

	public Process(String[] tokenizedInput, int level) {
		this(tokenizedInput[1], tokenizedInput[2], level);
	}

	public BigInteger getEndTimeStamp() {
		return endTimeStamp;
	}

	public void setEndTimeStamp(String[] tokenizedInput) {
		setEndTimeStamp(new BigInteger(tokenizedInput[1]));
	}

	public void setEndTimeStamp(BigInteger endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public BigInteger getStartTimeStamp() {
		return startTimeStamp;
	}

	public BigInteger getExecutionTime() {
		return endTimeStamp.subtract(startTimeStamp);
	}

	public void addChildProcess(Process child) {
		this.children.add(child);
	}

	public List<Process> getChildren() {
		return this.children;
	}

	@Override
	public String toString() {
		return "Process [name=" + name + ", level=" + level + ", startTimeStamp=" + startTimeStamp + ", endTimeStamp="
				+ endTimeStamp + "]";
	}
}

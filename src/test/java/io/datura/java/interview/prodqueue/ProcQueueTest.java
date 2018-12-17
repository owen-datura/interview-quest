package io.datura.java.interview.prodqueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.regex.Pattern;

import org.junit.Test;

import io.datura.java.interview.procqueue.Process;

public class ProcQueueTest {
	@Test
	public void processInputString() {
		String input = "start,FELIPA,11029975320746904540";
		Pattern tokenizer = Pattern.compile(",");
		Process p = new Process(tokenizer.split(input), 0);

		assertEquals("FELIPA", p.getName());
		assertEquals(0, p.getLevel());

		BigInteger testValue = new BigInteger("11029975320746904540");
		assertTrue(testValue.compareTo(p.getStartTimeStamp()) == 0);
	}
}

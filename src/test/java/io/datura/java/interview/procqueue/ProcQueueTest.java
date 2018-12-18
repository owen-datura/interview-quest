package io.datura.java.interview.procqueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

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

	@Test
	public void evalProcessOrdering() {
		String[] temp = new String[8];
		temp[0] = "start,login.exe,10667692814595116481";
		temp[1] = "start,domain-login.ps1,10887999980799835056";
		temp[2] = "start,add-user.reg,11029975320746904540";
		// add-user stop
		temp[3] = "stop,11183464898197635685";
		// domain-login stop
		temp[4] = "stop,11185464898197635345";
		temp[5] = "start,temp-cleanup.exe,12185464898197635345";
		// temp-cleanup stop
		temp[6] = "stop,12585464898197635345";
		// login stop (logout?)
		temp[7] = "stop,12685464898197635489";
		List<String> events = Arrays.asList(temp);

		LinkedList<Process> result = io.datura.java.interview.procqueue.App.evaluate(events);
		
		System.out.println(result);
		
		assertTrue(true);
	}
}

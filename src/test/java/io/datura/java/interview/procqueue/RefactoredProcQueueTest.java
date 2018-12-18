package io.datura.java.interview.procqueue;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class RefactoredProcQueueTest {
	@Test
	public void evalProcessOrdering() {
		String[] temp = new String[8];
		temp[0] = "start,login.exe,10667692814595116481";
		// level 1, parent is login
		temp[1] = "start,domain-login.ps1,10887999980799835056";
		// level 2, parent is domain-login
		temp[2] = "start,add-user.reg,11029975320746904540";
		// add-user stop
		temp[3] = "stop,11183464898197635685";
		// domain-login stop
		temp[4] = "stop,11185464898197635345";
		// level 1, parent is login
		temp[5] = "start,temp-cleanup.exe,12185464898197635345";
		// temp-cleanup stop
		temp[6] = "stop,12585464898197635345";
		// login stop (logout?)
		temp[7] = "stop,12685464898197635489";
		List<String> events = Arrays.asList(temp);

		Process root = io.datura.java.interview.procqueue.AppRefactored.evaluate(events);
		assertEquals("login.exe", root.getName());
		// login has two children, domain-login and temp-cleanup
		assertEquals(2, root.getChildren().size());

		// login (the root process) has one child, domain-login, that has its own
		// child, so check for that
		Process domainLogin = root.getChildren().get(0);
		assertEquals("domain-login.ps1", domainLogin.getName());
		
		// check that the child exists
		assertEquals(1, domainLogin.getChildren().size());
		
		// check that domain-login's child, add-user.reg, is present
		Process addUserDotReg = domainLogin.getChildren().iterator().next();
		assertEquals("add-user.reg", addUserDotReg.getName());
	}
}

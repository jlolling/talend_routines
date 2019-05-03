package routines.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import routines.JobUtil;

public class TestJobUtil {
	
	@Test
	public void testRunProcessDontWait() {
		String command = "/var/data/talend/jobs/test_dummy_job_start.sh";
		//JobUtil.runProcess(command);
		JobUtil.runProcess("/bin/bash","-c",command);
		assertTrue(true);
	}
}

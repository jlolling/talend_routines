package routines.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

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
	
	@Test
	public void testRunScript() throws Exception {
		String shellCommand = "/bin/bash -c ls";
		String c1 = "var1=test";
		String c2 = "echo $var1";
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "ls");
		Process shell = pb.redirectErrorStream(true).start();
		OutputStream out = shell.getOutputStream();
		
		Thread st = new Thread(new Runnable() {

			@Override
			public void run() {
	            try {
			        try (BufferedReader input = new BufferedReader(new InputStreamReader(shell.getInputStream()))) {
			            String line = null;
						while ((line = input.readLine()) != null) {
						    System.out.println(line);
						}
			        }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		st.start();
		out.write(c2.getBytes());
		out.flush();
		shell.waitFor();
		st.join(100);
	}
}

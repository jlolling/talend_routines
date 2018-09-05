package routines.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import routines.GlobalMapUtil;

public class TestGlobalMapUtil {


	@Test
	public void testAddCounterThreadSave() throws Exception {
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				GlobalMapUtil.addCounter("job", "test", 1);
			}
		};
		int expected = 100;
		for (int i = 0; i < expected; i++) {
			new Thread(r).start();
		}
		Thread.sleep(10000);
		int actual = GlobalMapUtil.getCounter("job", "test");
		assertEquals(expected, actual);
	}

}

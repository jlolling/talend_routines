package routines.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import routines.SQLParameterReplace;

public class TestSQLParameterReplace {
	
	@Test
	public void testReplaceParameters() throws Exception {
		String sql = "select f1, f2 from tab where f3 = :parameter1 and f4 = :parameter_2 and :irgnorethis: and f5 = :missingParam";
		SQLParameterReplace.addValue("parameter1", "v_p1");
		SQLParameterReplace.addValue("parameter_2", "v_p2");
		String actual = SQLParameterReplace.replaceParameters(sql);
		String expected = "select f1, f2 from tab where f3 = v_p1 and f4 = v_p2 and :irgnorethis: and f5 = :missingParam";
		assertEquals("Result wrong", expected, actual);
		String errors = SQLParameterReplace.checkParametersNotReplaced(actual,",");
		assertEquals("Errors wrong", "missingParam", errors);
	}

}

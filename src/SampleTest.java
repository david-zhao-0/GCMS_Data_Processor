import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class SampleTest {

	@Test
	final void test() {
		Sample s1 = new Sample("LF_1");
		Assert.assertTrue(s1.getSortOrder() == 0);
		Sample s2 = new Sample("LB_1");
		Assert.assertTrue(s2.getSortOrder() == 1);
		Sample s3 = new Sample("HF_1");
		Assert.assertTrue(s3.getSortOrder() == 2);
		Sample s4 = new Sample("HB_1");
		Assert.assertTrue(s4.getSortOrder() == 3);
		Sample s5 = new Sample("aaaa");
		Assert.assertTrue(s5.getSortOrder() == -1);
	}

}

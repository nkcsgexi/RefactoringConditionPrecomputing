package util.tests;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import edu.ncsu.dlf.refactoring.precondition.util.MathUtils;

public class MathUtilTests {

	@Test
	public void randomNumberTest()
	{
		boolean upperHit = false;
		boolean lowerHit = false;
		for (int i = 0; i < 10000; i ++)
		{
			int j = MathUtils.getRondomInteger(0, 10);
			Assert.isTrue(j >= 0);
			Assert.isTrue(j <= 10);
			if(j == 10)
				upperHit = true;
			if(j == 0)
				lowerHit = true;
		}
		Assert.isTrue(upperHit);
		Assert.isTrue(lowerHit);
	}
	
}

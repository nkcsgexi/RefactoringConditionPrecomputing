package dlf.refactoring.precondition.util;

import java.util.Random;

public class MathUtils {
	
	/* Generate an integer between the lower (inclusive) and the upper (inclusive).*/
	public static int getRondomInteger(int min, int max)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	}
}

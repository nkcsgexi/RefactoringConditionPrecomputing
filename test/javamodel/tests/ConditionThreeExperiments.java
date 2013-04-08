package javamodel.tests;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import dlf.refactoring.precondition.util.MathUtils;


public class ConditionThreeExperiments {
	
	private static final String CHAR_LIST = 
	        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	private char getRandomChar()
	{
		  int number = MathUtils.getRondomInteger(0, CHAR_LIST.length() - 1);
          return CHAR_LIST.charAt(number);
	}
	
	private String generateRandomString(int length)
	{
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<length; i++){
            randStr.append(getRandomChar());
        }
        return randStr.toString();
	}
	
	@Test
	public void testGetRandomChar()
	{
		boolean lowerReached = false;
		boolean upperReached = false;
		for(int i = 0 ; i< 1000; i ++)
		{
			char c = getRandomChar();
			if(c == 'a')
				lowerReached = true;
			if(c == '0')
				upperReached = true;
		}
		Assert.isTrue(upperReached);
		Assert.isTrue(lowerReached);
	}
	
	private boolean verifyIdentifier() {
		char[] identifier = generateRandomString(200).toCharArray();
		if(Character.isJavaIdentifierStart(identifier[0]))
		{
			for(char c : identifier)
			{
				if(!Character.isJavaIdentifierPart(c))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Test
	public void verifyIdentifiers()
	{
		long start = System.currentTimeMillis();
		for(int i = 0; i< 1000; i ++)
			verifyIdentifier();
		long time = System.currentTimeMillis() - start;
		System.out.println(time);
	}


	
	
	
	
	
	
	

}

package util.tests;

import org.junit.Test;

import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.enums.RefactoringTypeMessageManager;

public class RefactoringTypeMessageTests {

	
	@Test
	public void method1() throws Exception
	{
		RefactoringTypeMessageManager manager = RefactoringTypeMessageManager.getInstance();
		manager.recordMessage(RefactoringType.EXTRACT_METHOD, "test1");
	}
}

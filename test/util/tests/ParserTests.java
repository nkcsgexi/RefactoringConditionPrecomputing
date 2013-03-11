package util.tests;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Test;

import util.Parser;



public class ParserTests {

	@Test 
	public void method1()
	{
		String source = "import org.eclipse.core.runtime.Assert;\n"
				+ "import org.junit.Test;\n"
				+ "public class ParserTests {\n"
				+ "public void method(){}}";
		ASTNode cu = Parser.Parse2ComilationUnit(source);
		Assert.isNotNull(cu);
	}
	
}

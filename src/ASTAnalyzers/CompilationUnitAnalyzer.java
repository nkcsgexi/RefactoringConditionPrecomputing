package ASTAnalyzers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;


public class CompilationUnitAnalyzer {

	public static List<ASTNode> getTypes(ASTNode cu)
	{
		return ASTAnalyzerUtils.getDecendentByType(cu, ASTNode.TYPE_DECLARATION);
	}
}

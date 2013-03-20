package edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;


public class CompilationUnitAnalyzer {

	public static List<ASTNode> getTypes(ASTNode cu)
	{
		return ASTNodeAnalyzer.getDecendentByType(cu, ASTNode.TYPE_DECLARATION);
	}
}

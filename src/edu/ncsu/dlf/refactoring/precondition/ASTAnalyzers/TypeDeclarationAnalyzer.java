package edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

public class TypeDeclarationAnalyzer {

	public static List<ASTNode> getMethodDeclarations(ASTNode type)
	{
		return ASTNodeAnalyzer.getDecendentByType(type, ASTNode.METHOD_DECLARATION);
	}
	
	public static List<ASTNode> getFieldDeclarations(ASTNode type)
	{
		return ASTNodeAnalyzer.getDecendentByType(type, ASTNode.FIELD_DECLARATION);
	}
}

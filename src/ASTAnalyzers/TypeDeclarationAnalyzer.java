package ASTAnalyzers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

public class TypeDeclarationAnalyzer {

	public static List<ASTNode> getMethodDeclarations(ASTNode type)
	{
		return ASTAnalyzerUtils.getDecendentByType(type, ASTNode.METHOD_DECLARATION);
	}
	
	public static List<ASTNode> getFieldDeclarations(ASTNode type)
	{
		return ASTAnalyzerUtils.getDecendentByType(type, ASTNode.FIELD_DECLARATION);
	}
}

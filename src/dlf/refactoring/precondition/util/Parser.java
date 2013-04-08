package dlf.refactoring.precondition.util;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Parser {
	
	/*
	 * Parse a string of source code into a compilation unit.
	 */
	public static ASTNode Parse2ComilationUnit(String source) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}
	
	/*
	 * Parse a ICompilatioUnit to CompilationUnit.
	 * */
	public static ASTNode Parse2ComilationUnit(ICompilationUnit iu) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(iu);
		parser.setProject(iu.getJavaProject());
		parser.setResolveBindings(true);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}
}
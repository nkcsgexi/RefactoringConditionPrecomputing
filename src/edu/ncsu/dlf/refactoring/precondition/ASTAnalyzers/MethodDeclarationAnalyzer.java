package edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers;

import java.util.Comparator;
import java.util.List;

import org.eclipse.core.internal.dtree.IComparator;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

import edu.ncsu.dlf.refactoring.precondition.util.IPredicate;
import edu.ncsu.dlf.refactoring.precondition.util.Pair;
import edu.ncsu.dlf.refactoring.precondition.util.Tree;
import edu.ncsu.dlf.refactoring.precondition.util.TreeBuilder;


public class MethodDeclarationAnalyzer {
	
	
	public static boolean hasStatements(ASTNode m)
	{
		return ASTNodeAnalyzer.hasDecendent(m,new IPredicate<ASTNode>(){
			@Override
			public boolean IsTrue(ASTNode t) throws Exception {
				return t instanceof Statement;
			}});
	}

	public static Tree<ASTNode> createStatementsTree(ASTNode m) throws Exception
	{		
		List<ASTNode> statements = getAllStatements(m);
		TreeBuilder<ASTNode> builder = new TreeBuilder<ASTNode>(statements, creatIsAncestor());
		return builder.createTree();
	}

	public static List<ASTNode> getAllStatements(ASTNode m) {
		List<ASTNode> statements = ASTNodeAnalyzer.getDecendent(m, new IPredicate<ASTNode>(){
			@Override
			public boolean IsTrue(ASTNode t) throws Exception {
				return t instanceof Statement;
			}});
		return statements;
	}
	
	private static IPredicate<Pair<ASTNode, ASTNode>> creatIsAncestor() 
	{
		IPredicate<Pair<ASTNode, ASTNode>> isAncestor = new IPredicate<Pair<ASTNode, ASTNode>>(){
			@Override
			public boolean IsTrue(Pair<ASTNode, ASTNode> t) throws Exception {
				return ASTNodeAnalyzer.isOneNodeEnclosingAnother(t.getFirst(), t.getSecond());
			}};
		return isAncestor;
	}
}

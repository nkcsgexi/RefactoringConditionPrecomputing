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

	public static Tree<ASTNode> createStatementTree(ASTNode m) throws Exception
	{		
		List<ASTNode> statements = ASTNodeAnalyzer.getDecendent(m, new IPredicate<ASTNode>(){
			@Override
			public boolean IsTrue(ASTNode t) throws Exception {
				return t instanceof Statement;
			}});
			
		TreeBuilder<ASTNode> builder = new TreeBuilder<ASTNode>(statements, creatIsAncestor());
		return builder.createTree();
	}
	

	private static IPredicate<Pair<ASTNode, ASTNode>> creatIsAncestor() 
	{
		IPredicate<Pair<ASTNode, ASTNode>> isAncestor = new IPredicate<Pair<ASTNode, ASTNode>>(){
			@Override
			public boolean IsTrue(Pair<ASTNode, ASTNode> t) throws Exception {
				boolean startOk = t.getFirst().getStartPosition() <= t.getSecond().
						getStartPosition();
				boolean endOk = t.getFirst().getStartPosition() + t.getFirst().getLength() >= 
						t.getSecond().getStartPosition() + t.getSecond().getLength();
				boolean notSame = t.getFirst().getStartPosition() == t.getSecond().
						getStartPosition() && t.getFirst().getLength() == t.getSecond().getLength();
				return startOk && endOk && notSame;
			}};
		return isAncestor;
	}
}

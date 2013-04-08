package dlf.refactoring.precondition.ASTAnalyzers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import dlf.refactoring.precondition.util.Pair;
import dlf.refactoring.precondition.util.Tree;
import dlf.refactoring.precondition.util.TreeBuilder;
import dlf.refactoring.precondition.util.interfaces.IPredicate;


public class ASTNodesAnalyzer {
	
	public static int getNodesStartPosition(List<ASTNode> nodes)
	{
		return Collections.min(nodes, new Comparator<ASTNode>(){
			@Override
			public int compare(ASTNode o1, ASTNode o2) {
				return o1.getStartPosition() - o2.getStartPosition();
			}}).getStartPosition();
	}
	
	public static int getNodesEndPosition(List<ASTNode> nodes)
	{
		return ASTNodeAnalyzer.getEndPosition(
			Collections.max(nodes, new Comparator<ASTNode>(){
			@Override
			public int compare(ASTNode o1, ASTNode o2) {
				return ASTNodeAnalyzer.getEndPosition(o1)- ASTNodeAnalyzer.getEndPosition(o2);
			}})
		);
	}
	
	public static int getNodesLength(List<ASTNode> nodes)
	{
		int start = getNodesStartPosition(nodes);
		int end = getNodesEndPosition(nodes);
		return end - start;
	}
	
	public static Tree<ASTNode> createASTNodesTree(List<ASTNode> nodes) throws Exception
	{
		TreeBuilder<ASTNode> builder = new TreeBuilder<ASTNode>(nodes, creatIsAncestor());
		return builder.createTree();
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

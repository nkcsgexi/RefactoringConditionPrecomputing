package dlf.refactoring.precondition.ASTAnalyzers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.internal.dtree.IComparator;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

import dlf.refactoring.precondition.util.ExpandOperations;
import dlf.refactoring.precondition.util.ListOperations;
import dlf.refactoring.precondition.util.Tree;
import dlf.refactoring.precondition.util.TreeBuilder;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IPredicate;
import dlf.refactoring.precondition.util.interfaces.Pair;



public class MethodDeclarationAnalyzer {
	
	
	public static boolean hasStatements(ASTNode m)
	{
		return ASTNodeAnalyzer.hasDecendent(m,new IPredicate<ASTNode>(){
			@Override
			public boolean IsTrue(ASTNode t) throws Exception {
				return t instanceof Statement;
			}});
	}

	public static List<ASTNode> getAllStatements(ASTNode m) {
		List<ASTNode> statements = ASTNodeAnalyzer.getDecendent(m, new IPredicate<ASTNode>(){
			@Override
			public boolean IsTrue(ASTNode t) throws Exception {
				return t instanceof Statement;
			}});
		return statements;
	}
	
	public static List<List<ASTNode>> getStatementGroups(ASTNode m) throws Exception
	{
		List<ASTNode> statements = getAllStatements(m);
		Tree<ASTNode> tree = ASTNodesAnalyzer.createASTNodesTree(statements);
		ArrayList<Tree<ASTNode>> subTrees = new ArrayList<Tree<ASTNode>>(tree.getSubTrees());
		List<List<ASTNode>> childrenGroups = getFirstLevelSubNodes(subTrees); 
		return getAllSubGroups(childrenGroups);
	}

	private static List<List<ASTNode>> getFirstLevelSubNodes(ArrayList<Tree<ASTNode>> subTrees) 
			throws Exception {
		ExpandOperations<Tree<ASTNode>, List<ASTNode>> expand = new ExpandOperations<Tree<ASTNode>, 
			List<ASTNode>>(){};
		return expand.convert(subTrees, new IConvertor<Tree<ASTNode>, List<ASTNode>>(){
			@Override
			public List<ASTNode> convert(Tree<ASTNode> t) {
				return new ArrayList<ASTNode>(t.getSuccessors(t.getHead()));
		}});
	}
	
	private static List<List<ASTNode>> getAllSubGroups(List<List<ASTNode>> nodeGroups) throws 
		Exception
	{
		ExpandOperations<List<ASTNode>, List<ASTNode>> expand = new ExpandOperations<List<ASTNode>, 
				List<ASTNode>>();
		return expand.expand(nodeGroups, new IMapper<List<ASTNode>, List<ASTNode>>(){
			@Override
			public List<List<ASTNode>> map(List<ASTNode> t) throws Exception {
				ListOperations<ASTNode> operation = new ListOperations<ASTNode>();
				return operation.getAllSublists(t);
			}});
	}
	
	
	
}

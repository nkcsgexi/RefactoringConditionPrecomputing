package ASTAnalyzers;

import java.util.Comparator;
import java.util.List;

import org.eclipse.core.internal.dtree.IComparator;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;

import util.IPredicate;
import util.Tree;

public class MethodDeclarationAnalyzer {

	public static Tree<ASTNode> createStatementTree(ASTNode m)
	{		
		List<ASTNode> statements = ASTAnalyzerUtils.getDecendent(m, new IPredicate<ASTNode>(){
			@Override
			public boolean IsTrue(ASTNode t) throws Exception {
				return t instanceof Statement;
			}});
		Comparator<ASTNode> comparator = new Comparator<ASTNode>(){
			@Override
			public int compare(ASTNode arg0, ASTNode arg1) {
				return 0;
			}};
		return null;
	}
}

package edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

import edu.ncsu.dlf.refactoring.precondition.util.XLoggerFactory;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IPredicate;


public class ASTNodeAnalyzer {

	private static Logger logger = XLoggerFactory.GetLogger(ASTNodeAnalyzer.class);
	
	public static List<ASTNode> getDecendent(ASTNode parent, final IPredicate<ASTNode> predicate)
	{
		final List<ASTNode> targets = new ArrayList<ASTNode>();
		parent.accept(new ASTVisitor(){
			@Override
			public void postVisit(ASTNode node)
			{
				try {
					if(predicate.IsTrue(node))
					{
						targets.add(node);
					}
				} catch (Exception e) {
					logger.fatal("Get decendent errors.");
				}
			}
		});
		return targets;
	}
	
	public static boolean hasDecendent(ASTNode parent, final IPredicate<ASTNode> predicate)
	{
		return !getDecendent(parent, predicate).isEmpty();
	}
	
	
	public static List<ASTNode> getDecendentByType(ASTNode parent, final int type)
	{
		return getDecendent(parent, new IPredicate<ASTNode>(){
			@Override
			public boolean IsTrue(ASTNode t) throws Exception {
				return t.getNodeType() == type;
			}});
	}

	public static int getEndPosition(ASTNode node) {
		return node.getStartPosition() + node.getLength();
	}
	
	
	public static boolean isOneNodeEnclosingAnother(ASTNode node1, ASTNode node2)
	{
		boolean startOk = node1.getStartPosition() <= node2.getStartPosition();
		boolean endOk = node1.getStartPosition() + node1.getLength() >= node2.getStartPosition() + 
				node2.getLength();
		boolean same = node1.getStartPosition() == node2.getStartPosition() && node1.getLength() == 
				node2.getLength();
		return startOk && endOk && !same;
	}
}

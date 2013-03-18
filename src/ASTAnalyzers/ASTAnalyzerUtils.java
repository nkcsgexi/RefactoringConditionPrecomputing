package ASTAnalyzers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

import util.IPredicate;
import util.XLoggerFactory;

public class ASTAnalyzerUtils {

	private static Logger logger = XLoggerFactory.GetLogger(ASTAnalyzerUtils.class);
	
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
	
	public static List<ASTNode> getDecendentByType(ASTNode parent, final int type)
	{
		return getDecendent(parent, new IPredicate<ASTNode>(){
			@Override
			public boolean IsTrue(ASTNode t) throws Exception {
				return t.getNodeType() == type;
			}});
	}
	
	
	
}

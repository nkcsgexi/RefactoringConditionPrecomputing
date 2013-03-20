package edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

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
	
}

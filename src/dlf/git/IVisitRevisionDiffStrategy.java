package dlf.git;

import org.eclipse.jgit.treewalk.TreeWalk;

public interface IVisitRevisionDiffStrategy {
	void visitDiffTree(TreeWalk tree) throws Exception;
}

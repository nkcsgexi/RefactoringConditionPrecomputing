package dlf.git;

import org.eclipse.jgit.treewalk.TreeWalk;

public interface IVisitRevisionDiffStrategy {
	void visitDiffTrees(TreeWalk tree) throws Exception;
}

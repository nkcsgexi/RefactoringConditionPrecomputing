package dlf.git;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import dlf.refactoring.precondition.util.XLoggerFactory;

public class GitProject {
	
	private final String projectName;
	private final String directory;
	private final String localPath;
	private final Git git;
	private final Repository repo;
	private final Logger logger;
	
	public GitProject(String directory, String projectName,String remotePath) throws Exception
	{
		this.projectName = projectName;
		this.directory = directory;
		this.localPath = this.directory + "/" + this.projectName;
	   	CloneCommand clone = Git.cloneRepository();
    	clone.setBare(false);
    	clone.setCloneAllBranches(true);
    	clone.setDirectory(new File(localPath)).setURI(remotePath);
    	this.git = clone.call();
    	this.repo = git.getRepository();
    	this.logger = XLoggerFactory.GetLogger(this.getClass());
	}
	
	
	public GitProject(String directory, String projectName) throws Exception
	{
		this.projectName = projectName;
		this.directory = directory;
		this.localPath = this.directory + "/" + this.projectName;
		this.repo = new FileRepository(this.localPath + "/.git");
		this.git = new Git(repo);
    	this.logger = XLoggerFactory.GetLogger(this.getClass());
	}
	
	// Walk throught each revision from the very head of this repo.
	public void walkRevisionDiffs(IVisitRevisionDiffStrategy strategy) throws Exception
	{	
		RevWalk walk = new RevWalk(repo);
		walk.markStart(walk.parseCommit(repo.resolve("HEAD")));
		Iterator<RevCommit> it = walk.iterator();
		while(it.hasNext())
		{
			RevCommit revision = it.next();
			TreeWalk tree = new TreeWalk(repo);
			tree.addTree(revision.getTree());
		    for(RevCommit parent : revision.getParents())
		    {	
		    	tree.addTree(parent.getTree());
		    }
		    tree.setPostOrderTraversal(false);
		    tree.setFilter(TreeFilter.ANY_DIFF);
		    tree.setRecursive(true);
		    strategy.visitDiffTrees(tree);
		}	
	}
}

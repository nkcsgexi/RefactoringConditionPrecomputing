package util.tests;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.treewalk.TreeWalk;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dlf.git.GitProject;
import dlf.git.IVisitRevisionDiffStrategy;
import dlf.refactoring.precondition.util.FileUtils;
import dlf.refactoring.precondition.util.XLoggerFactory;

public class GitTests {

	private static String directory = "C:\\Users\\xige\\Desktop\\test";
    private static String projectName = "GhostFactor"; 
    private static String remotePath = "git://github.com/nkcsgexi/GhostFactor2.git";
    private static Logger logger = XLoggerFactory.GetLogger(GitTests.class);
    
    
    @BeforeClass
    public static void clean()
    {
    	//FileUtils.deleteFolder(FileUtils.getFile(directory));
    }
    
    @Test
    public void method1() throws Exception
    {
    	GitProject gp = new GitProject(directory, projectName);
    	gp.walkRevisionDiffs(new IVisitRevisionDiffStrategy(){
			@Override
			public void visitDiffTree(TreeWalk tree) throws Exception {
				logger.info("New Tree: " + tree.getTreeCount());
				while(tree.next())
				{
					if(tree.getNameString().endsWith(".cs"))
					{
						logger.info(tree.getNameString());
					}
				}
			}});
    }
}
package util.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.ObjectStream;
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
    private static String projectName = "prechecking"; 
    private static String remotePath = "git://github.com/nkcsgexi/" +
    		"RefactoringConditionPrecomputing.git";
    private static Logger logger = XLoggerFactory.GetLogger(GitTests.class);
    private GitProject git; 
    
    @BeforeClass
    public static void clean()
    {
    	//FileUtils.deleteFolder(FileUtils.getFile(directory));
    }
    
    @Before
    public void setUp() throws Exception
    {
    	git = new GitProject(directory, projectName);
    }
    
    @Test
    public void method1() throws Exception
    {
    	git.walkRevisionDiffs(new IVisitRevisionDiffStrategy(){
			@Override
			public void visitDiffTrees(TreeWalk tree) throws Exception {
				logger.info("New Tree: " + tree.getTreeCount());
				while(tree.next())
				{
					if(tree.getNameString().endsWith(".java"))
					{
						logger.info(tree.getNameString());
					}
				}
			}});
    }
    
    @Test
    public void method2() throws Exception
    {
    	git.walkRevisionDiffs(new IVisitRevisionDiffStrategy(){
			@Override
			public void visitDiffTrees(TreeWalk tree) throws Exception {
				while(tree.next())
				{
					if(tree.getNameString().endsWith("GitTests.java"))
					{
						ObjectReader reader = tree.getObjectReader();
						ObjectId id = tree.getObjectId(1);
						if(reader.has(id)){
							ObjectLoader ob = reader.open(id);
							ObjectStream st = ob.openStream();
							String str = IOUtils.toString(st, "UTF-8");
							logger.info(str);
						}
					}
				}
			}});
    }   
}
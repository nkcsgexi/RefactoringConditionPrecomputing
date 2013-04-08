package edu.ncsu.dlf.refactoring.precondition.checker;

import java.util.List;

import edu.ncsu.dlf.refactoring.precondition.checker.environments.RefactoringContext;
import edu.ncsu.dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import edu.ncsu.dlf.refactoring.precondition.checker.result.ICheckingResult;
import edu.ncsu.dlf.refactoring.precondition.checker.result.RefactoringEnvironmentResults;
import edu.ncsu.dlf.refactoring.precondition.util.XArrayList;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IConvertor;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IMapper;

public class RefactoringCheckersRepository {
	final private XArrayList<RefactoringCheckerSet> checkerSets;
	
	private RefactoringCheckersRepository()
	{
		this.checkerSets = new XArrayList<RefactoringCheckerSet>();
		checkerSets.add(new RenameClassCheckers());
	}
	
	private static RefactoringCheckersRepository instance;
	
	public static RefactoringCheckersRepository getInstance()
	{
		if(instance == null)
			instance = new RefactoringCheckersRepository();
		return instance;
	}
	
	public XArrayList<RefactoringEnvironmentResults> performChecking(final RefactoringContext 
			context) throws Exception
	{
		return checkerSets.select(new IMapper<RefactoringCheckerSet,RefactoringEnvironmentResults>()
		{
			@Override
			public List<RefactoringEnvironmentResults> map(final RefactoringCheckerSet checkers)
					throws Exception {
				XArrayList<IRefactoringEnvironment> environments = checkers.
						getAllRefactoringEnvironments(context);
				return environments.convert(new IConvertor<IRefactoringEnvironment, 
						RefactoringEnvironmentResults>(){
					@Override
					public RefactoringEnvironmentResults convert(IRefactoringEnvironment env) throws 
						Exception {
						RefactoringEnvironmentResults results = new RefactoringEnvironmentResults
								(env);
						results.addMultiCheckingResult(checkers.performAllChecking(env));
						return results;
					}});
				
		}});
	}
	
	
}

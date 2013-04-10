package javamodel.tests;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.IJavaElement;
import org.junit.Before;
import org.junit.Test;

import dlf.refactoring.enums.InputType;
import dlf.refactoring.precondition.checker.RefactoringCheckersRepository;
import dlf.refactoring.precondition.checker.environments.RefactoringContext;
import dlf.refactoring.precondition.checker.result.C2CheckingResult;
import dlf.refactoring.precondition.checker.result.RefactoringEnvironmentResults;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IOperation;

public class PrecheckingTests extends RefactoringExperiment{

	private RefactoringContext context;
	
	@Before
	public void setUp()
	{
		List<IJavaElement> contextUnits = this.getRandomUnits(1);
		this.context = new RefactoringContext();
		context.AddMultiCompilationUnits(contextUnits);
	}
	
	
	@Test
	public void method1() throws Exception
	{	
		RefactoringCheckersRepository repository = RefactoringCheckersRepository.getInstance();
		XArrayList<RefactoringEnvironmentResults> results = repository.performChecking(context);
		Assert.isTrue(!results.isEmpty());
		logger.info("Count of results: " + results.size());
		results.operateOnElement(new IOperation<RefactoringEnvironmentResults>(){
			@Override
			public void perform(RefactoringEnvironmentResults t)
					throws Exception {
				logger.info("Working on refactoring environment");
				Assert.isTrue(t.getReusltsCount() > 0);
				Assert.isTrue(t.getC1Results().empty());
				XArrayList<C2CheckingResult> c2Results = t.getC2ResultByInputType
						(InputType.NEW_NAME);
				Assert.isTrue(c2Results.size() > 0);
				C2CheckingResult result = c2Results.get(0);
				logger.info("A result: " + result.toString());
			}});
	}
}

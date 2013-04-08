package dlf.refactoring.precondition.checker.result;

import java.util.Collection;

import dlf.refactoring.ConditionType;
import dlf.refactoring.RefactoringType;
import dlf.refactoring.interfaces.IHasConditionType;
import dlf.refactoring.interfaces.IHasRefactoringType;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IPredicate;


public class RefactoringEnvironmentResults implements IHasRefactoringType{
		private final IRefactoringEnvironment environment;
		private final XArrayList<ICheckingResult> results;
		
		public RefactoringEnvironmentResults(IRefactoringEnvironment environment)
		{
			this.environment = environment;
			results = new XArrayList<ICheckingResult>();
		}
		
		public void addCheckingResult(ICheckingResult r)
		{
			results.add(r);
		}

		public boolean isEnvironmentCorrect(IRefactoringEnvironment environment)
		{
			return this.environment.equals(environment);
		}

		@Override
		public RefactoringType getRefactoringType() {
			return environment.getRefactoringType();
		}

		public void addMultiCheckingResult(Collection<ICheckingResult> results) {
			results.addAll(results);
		}
		
		public XArrayList<C1CheckingResult> getC1Results() throws Exception
		{
			return extractCheckingResults(results, C1CheckingResult.class);
		}
		
		public XArrayList<C2CheckingResult> getC2Results() throws Exception
		{
			return extractCheckingResults(results, C2CheckingResult.class);
		}
		
		private <C> XArrayList<C> extractCheckingResults(final XArrayList<ICheckingResult> 
			results, final Class C) throws Exception {
			return results.where (new IPredicate<ICheckingResult>(){
			@Override
			public boolean IsTrue(ICheckingResult t) throws Exception {
				return t.equals(C);
			}}).convert(new IConvertor<ICheckingResult, C>(){
				@Override
				public C convert(ICheckingResult m) throws Exception {
					return (C)m;
				}});
	}
}


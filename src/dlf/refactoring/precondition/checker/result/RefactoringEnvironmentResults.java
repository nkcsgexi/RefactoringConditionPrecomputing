package dlf.refactoring.precondition.checker.result;

import java.util.Collection;

import org.apache.log4j.Logger;

import dlf.refactoring.enums.ConditionType;
import dlf.refactoring.enums.InputType;
import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.enums.interfaces.IHasConditionType;
import dlf.refactoring.enums.interfaces.IHasRefactoringType;
import dlf.refactoring.enums.maps.InputConditionType2Map;
import dlf.refactoring.precondition.checker.environments.IRefactoringEnvironment;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IPredicate;


public class RefactoringEnvironmentResults implements IHasRefactoringType{
		
		private final IRefactoringEnvironment environment;
		private final XArrayList<ICheckingResult> allResults;
		private final Logger logger;
		
		public RefactoringEnvironmentResults(IRefactoringEnvironment environment)
		{
			this.environment = environment;
			this.allResults = new XArrayList<ICheckingResult>();
			this.logger = XLoggerFactory.GetLogger(this.getClass());
		}
		
		public void addCheckingResult(ICheckingResult r)
		{
			allResults.add(r);
		}

		public boolean isEnvironmentCorrect(IRefactoringEnvironment environment)
		{
			return this.environment.equals(environment);
		}

		@Override
		public RefactoringType getRefactoringType() {
			return environment.getRefactoringType();
		}

		public void addMultiCheckingResults(Collection<ICheckingResult> nrs) {
			this.allResults.addAll(nrs);
		}
		
		public XArrayList<C2CheckingResult> getC2ResultByInputType(final InputType it) throws 
			Exception
		{
			XArrayList<C2CheckingResult> C2Results = extractC2Results(allResults);
			return C2Results.where(new IPredicate<C2CheckingResult>(){
				@Override
				public boolean IsTrue(C2CheckingResult t) throws Exception {
					return InputConditionType2Map.getInstance().isExisting(it, t.getConditionType());
				}});
		}
		
		public XArrayList<C1CheckingResult> getC1Results() throws Exception
		{
			return extractC1Results(allResults);
		}
	
		
		private XArrayList<C1CheckingResult> extractC1Results(final XArrayList<ICheckingResult> 
			resultList) throws Exception {
				return resultList.where (new IPredicate<ICheckingResult>(){
					@Override
					public boolean IsTrue(ICheckingResult t) throws Exception {
						return t instanceof C1CheckingResult;
					}}).convert(new IConvertor<ICheckingResult, C1CheckingResult>(){
						@Override
						public C1CheckingResult convert(ICheckingResult m) throws Exception {
							return (C1CheckingResult)m;
						}});
		}
		
		private XArrayList<C2CheckingResult> extractC2Results(final XArrayList<ICheckingResult> 
			resultList) throws Exception {
				return resultList.where (new IPredicate<ICheckingResult>(){
					@Override
					public boolean IsTrue(ICheckingResult t) throws Exception {
						return t instanceof C2CheckingResult;
					}}).convert(new IConvertor<ICheckingResult, C2CheckingResult>(){
						@Override
						public C2CheckingResult convert(ICheckingResult m) throws Exception {
							return (C2CheckingResult)m;
						}});
		}
		
		public int getReusltsCount()
		{
			return allResults.size();
		}
		
		
}


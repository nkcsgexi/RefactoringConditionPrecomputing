package javamodel.tests;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.dlf.refactoring.StructuralRefactoringAPIs;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.ITypeAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.util.ExpandCollection;
import edu.ncsu.dlf.refactoring.precondition.util.ListOperations;
import edu.ncsu.dlf.refactoring.precondition.util.XLoggerFactory;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IMapper;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IOperation;

public class PullPushExperiment extends RefactoringExperiment {
	
	
	private List<IJavaElement> methods;
	private List<IJavaElement> fields;
	private final ListOperations<IJavaElement> javaElementListOperations;
	private final ExpandCollection<IJavaElement, IJavaElement> javaElementExpandOperations;
	
	public PullPushExperiment()
	{
		this.logger = XLoggerFactory.GetLogger(this.getClass());
		this.projectIndex = 0;
		this.javaElementListOperations = new ListOperations<IJavaElement>();
		this.javaElementExpandOperations = new ExpandCollection<IJavaElement, IJavaElement>();
	}
	
	@Before
	public void getMembers() throws Exception
	{
		this.methods = this.javaElementExpandOperations.expand(this.types, new IMapper
				<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t) throws Exception {
				return ITypeAnalyzer.getMethods(t);
			}});
		
		this.fields = this.javaElementExpandOperations.expand(this.types, new IMapper
				<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t) throws Exception {
				return ITypeAnalyzer.getFields(t);
			}}); 
	}
	
	private void pullUpExperiment(Refactoring refactoring) {
		
	}
	
	private void pushDownExperiment(Refactoring refactoring)
	{
		
	}
	
	private void pullUpElements(List<IJavaElement> elements) throws Exception
	{
		javaElementListOperations.operationOnElements(elements, new IOperation<IJavaElement>(){
			@Override
			public void perform(IJavaElement t) throws Exception {
				Refactoring refactoring = StructuralRefactoringAPIs.createPullUpRefactoring(new 
						IMember[]{(IMember) t});
				pullUpExperiment(refactoring);
			}
		});
	}
	
	private void pushDownElements(List<IJavaElement> elements) throws Exception
	{
		javaElementListOperations.operationOnElements(elements, new IOperation<IJavaElement>(){
			@Override
			public void perform(IJavaElement t) throws Exception {
				Refactoring refactoring = StructuralRefactoringAPIs.createPushDownRefactoring(new 
						IMember[]{(IMember) t});
				pushDownExperiment(refactoring);
			}
		});
	}
	
	
	@Test
	public void pullupMethods() throws Exception
	{	
		pullUpElements(this.methods);
	}
	
	@Test
	public void pullupFields() throws Exception
	{
		pullUpElements(this.fields);
	}
	
	@Test
	public void pushDownMethods() throws Exception
	{
		pushDownElements(this.methods);
	}
	
	@Test
	public void pushDownFields() throws Exception
	{
		pushDownElements(this.fields);
	}
}

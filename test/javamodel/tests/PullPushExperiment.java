package javamodel.tests;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.junit.Before;
import org.junit.Test;

import dlf.refactoring.StructuralRefactoringAPIs;
import dlf.refactoring.precondition.JavaModelAnalyzers.IMemberAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.ITypeAnalyzer;
import dlf.refactoring.precondition.util.ExpandOperations;
import dlf.refactoring.precondition.util.ListOperations;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IOperation;


public class PullPushExperiment extends RefactoringExperiment {
	
	
	private List<IJavaElement> methods;
	private List<IJavaElement> fields;
	
	private final ListOperations<IJavaElement> javaElementListOperations;
	private final ExpandOperations<IJavaElement, IJavaElement> javaElementExpandOperations;
	
	public PullPushExperiment()
	{
		this.projectIndex = 0;
		this.javaElementListOperations = new ListOperations<IJavaElement>();
		this.javaElementExpandOperations = new ExpandOperations<IJavaElement, IJavaElement>();
	}
	
	@Before
	public void getMembers() throws Exception
	{
		this.methods = this.javaElementExpandOperations.expand(this.typeElements, new IMapper
				<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t) throws Exception {
				return ITypeAnalyzer.getMethods(t);
			}});
		
		this.fields = this.javaElementExpandOperations.expand(this.typeElements, new IMapper
				<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t) throws Exception {
				return ITypeAnalyzer.getFields(t);
			}}); 
	}
	
	
	
	
	private void pullUpExperiment(String name, Refactoring refactoring) throws Exception {
		try {
			long time = this.timeRefactoringChecking(refactoring);
			logger.info("Pulling up " + name + ": " + time);
		} catch(Exception e)
		{}
	}
	
	private void pushDownExperiment(String name, Refactoring refactoring) throws Exception {
		try {
			long time = this.timeRefactoringChecking(refactoring);
			logger.info("Pushing down " + name + ": " + time);
		}catch(Exception e)
		{}
	}
	
	private void pullUpElements(List<IJavaElement> elements) throws Exception
	{
		javaElementListOperations.operationOnElements(elements, new IOperation<IJavaElement>(){
			@Override
			public void perform(IJavaElement t) throws Exception {
				Refactoring refactoring = StructuralRefactoringAPIs.createPullUpRefactoring(new 
						IMember[]{(IMember) t}, getImmediateSuperType(t));
				pullUpExperiment(t.getElementName(), refactoring);
			}
		});
	}
	
	private IJavaElement getImmediateSuperType(IJavaElement member) throws Exception
	{
		IJavaElement type = IMemberAnalyzer.getContainingType(member);
		return ITypeAnalyzer.getSubTypes(type).get(0);
	}
	
	private void pushDownElements(List<IJavaElement> elements) throws Exception
	{
		javaElementListOperations.operationOnElements(elements, new IOperation<IJavaElement>(){
			@Override
			public void perform(IJavaElement t) throws Exception {
				Refactoring refactoring = StructuralRefactoringAPIs.createPushDownRefactoring(new 
						IMember[]{(IMember) t});
				pushDownExperiment(t.getElementName(), refactoring);
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

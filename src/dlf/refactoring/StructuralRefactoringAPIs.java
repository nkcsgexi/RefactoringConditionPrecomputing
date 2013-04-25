package dlf.refactoring;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.codemanipulation.CodeGenerationSettings;
import org.eclipse.jdt.internal.corext.refactoring.JavaRefactoringArguments;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.jdt.internal.corext.refactoring.code.InlineMethodRefactoring;
import org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.PushDownRefactoringProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.ExtractInterfaceProcessor;
import org.eclipse.jdt.internal.corext.refactoring.reorg.IConfirmQuery;
import org.eclipse.jdt.internal.corext.refactoring.reorg.IReorgPolicy.IMovePolicy;
import org.eclipse.jdt.internal.corext.refactoring.reorg.IReorgQueries;
import org.eclipse.jdt.internal.corext.refactoring.reorg.JavaMoveProcessor;
import org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgDestinationFactory;
import org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgPolicyFactory;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementAnalyzer;
import dlf.refactoring.precondition.util.XLoggerFactory;

public class StructuralRefactoringAPIs {
	
	private static Logger logger = XLoggerFactory.GetLogger(StructuralRefactoringAPIs.class);
	
	public static ExtractMethodRefactoring createExtractMethodRefactoring(ICompilationUnit iu, int 
			start, int length)
	{
		return new ExtractMethodRefactoring(iu, start, length);
	}
	
	public static Refactoring createInlineMethodRefactoring(ICompilationUnit iu)
	{
		return InlineMethodRefactoring.create(iu, null, 0, 0);
	}
	
	
	public static Refactoring createPullUpRefactoring(IMember[] members, IJavaElement type)
	{
		CodeGenerationSettings settings = createCodeGenerationSettings();
		PullUpRefactoringProcessor processor = new PullUpRefactoringProcessor(members, settings);
		processor.setDestinationType((IType) type);
		return new ProcessorBasedRefactoring(processor);
	}

	private static CodeGenerationSettings createCodeGenerationSettings() {
		CodeGenerationSettings settings = new CodeGenerationSettings();
		settings.createComments = false;
		settings.useKeywordThis = false;
		settings.overrideAnnotation = true;
		return settings;
	}
	
	public static Refactoring createPushDownRefactoring(IMember[] members)
	{
		PushDownRefactoringProcessor processor = new PushDownRefactoringProcessor(members);
		return new ProcessorBasedRefactoring(processor);
	}
	
	public static ExtractInterfaceProcessor createExtractInterfaceProcessor(IType type)
	{
		return new ExtractInterfaceProcessor(type, createCodeGenerationSettings());
	}
	
	public static MoveRefactoring createMoveRefactoring(IJavaElement element, IJavaElement 
			destination) throws Exception
	{
		IMovePolicy movePolicy= ReorgPolicyFactory.createMovePolicy(new IResource[]{}, 
				new IJavaElement[]{element});
		JavaMoveProcessor moveProcessor = new JavaMoveProcessor(movePolicy);
		moveProcessor.setDestination(ReorgDestinationFactory.createDestination(destination));
		moveProcessor.setReorgQueries(new MockReorgQueries());
		MoveRefactoring refactoring = new MoveRefactoring(moveProcessor);
		return refactoring;
	}
	
	
	public static boolean isAllConditionOK(Refactoring refactoring) throws Exception
	{
		try {
			RefactoringStatus result = refactoring.checkAllConditions(new NullProgressMonitor());
			return result.isOK();
		} catch (Exception e) {
			logger.fatal(refactoring.getName() + '\n' + e);
			return false;
		} 
	}

	
	
	

}

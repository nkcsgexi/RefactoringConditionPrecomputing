package edu.ncsu.dlf.refactoring;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.corext.codemanipulation.CodeGenerationSettings;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.PushDownRefactoringProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.ExtractInterfaceProcessor;


public class StructuralRefactoringAPIs {
	
	public static ExtractMethodRefactoring createExtractMethodRefactoring(ICompilationUnit iu, int 
			start, int length)
	{
		return new ExtractMethodRefactoring(iu, start, length);
	}
	
	public static PullUpRefactoringProcessor createPullUpRefactoringProcessor(IMember[] members)
	{
		CodeGenerationSettings settings = createCodeGenerationSettings();
		return new PullUpRefactoringProcessor(members, settings);
	}

	private static CodeGenerationSettings createCodeGenerationSettings() {
		CodeGenerationSettings settings = new CodeGenerationSettings();
		settings.createComments = false;
		settings.useKeywordThis = false;
		settings.overrideAnnotation = true;
		return settings;
	}
	
	public static PushDownRefactoringProcessor createPushDownRefactoringProcessor(IMember[] members)
	{
		return new PushDownRefactoringProcessor(members); 
	}
	
	public static ExtractInterfaceProcessor createExtractInterfaceProcessor(IType type)
	{
		return new ExtractInterfaceProcessor(type, createCodeGenerationSettings());
	}
	
	

}

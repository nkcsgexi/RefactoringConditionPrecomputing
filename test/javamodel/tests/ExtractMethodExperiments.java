package javamodel.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.jdt.internal.corext.refactoring.structure.ExtractInterfaceProcessor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.junit.Before;
import org.junit.Test;

import dlf.refactoring.StructuralRefactoringAPIs;
import dlf.refactoring.precondition.ASTAnalyzers.ASTNodesAnalyzer;
import dlf.refactoring.precondition.ASTAnalyzers.CompilationUnitAnalyzer;
import dlf.refactoring.precondition.ASTAnalyzers.MethodDeclarationAnalyzer;
import dlf.refactoring.precondition.ASTAnalyzers.TypeDeclarationAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IJavaModelAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentRootAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.IProjectAnalyzer;
import dlf.refactoring.precondition.JavaModelAnalyzers.ITypeAnalyzer;
import dlf.refactoring.precondition.util.ExpandOperations;
import dlf.refactoring.precondition.util.ListOperations;
import dlf.refactoring.precondition.util.Parser;
import dlf.refactoring.precondition.util.Tree;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;
import dlf.refactoring.precondition.util.interfaces.IOperation;
import dlf.refactoring.precondition.util.interfaces.IPredicate;



public class ExtractMethodExperiments extends RefactoringExperiment{

	private List<ASTNode> cus;
	private List<ASTNode> types;
	private List<ASTNode> methods;
	
	private final ExpandOperations<ASTNode, ASTNode> ASTNodeExpand;
	private final ExpandOperations<IJavaElement, IJavaElement> JavaElementExpand;
	
	private final ListOperations<ASTNode> ASTNodeListOperations;
	private final ListOperations<IJavaElement> JavaElementListOperations;
	
	private List<ICompilationUnitInformation> unitInformations;
	
	private final NullProgressMonitor monitor;
	private final String newMethodName = "RandomExtractedMethodName";
	
	
	private class ICompilationUnitInformation
	{
		IJavaElement unit;
		ASTNode unitRoot;
		List<ASTNode> types;
		List<ASTNode> methods;
	}
	
	public ExtractMethodExperiments()
	{
		this.ASTNodeExpand = new ExpandOperations<ASTNode, ASTNode>();
		this.JavaElementExpand = new ExpandOperations<IJavaElement, IJavaElement>();
		this.ASTNodeListOperations = new ListOperations<ASTNode>();
		this.JavaElementListOperations = new ListOperations<IJavaElement>();
		
		this.projectIndex = 0;
		this.monitor = new NullProgressMonitor();
	}
	
	private List<ASTNode> parseIUs(List<IJavaElement> ius) throws Exception
	{
		ExpandOperations<IJavaElement, ASTNode> expand = new ExpandOperations<IJavaElement, 
				ASTNode>();
		return expand.convert(ius, new IConvertor<IJavaElement, ASTNode>(){
			@Override
			public ASTNode convert(IJavaElement t) {
				return Parser.Parse2ComilationUnit((ICompilationUnit)t);
			}});
	}
	
	private List<ASTNode> getTypes(List<ASTNode> cus) throws Exception
	{
		return ASTNodeExpand.expand(cus, new IMapper<ASTNode, ASTNode>(){
			@Override
			public List<ASTNode> map(ASTNode t) throws Exception {
				return CompilationUnitAnalyzer.getTypes(t);
			}});
	}
	
	private List<ASTNode> getMethods(List<ASTNode> types) throws Exception
	{
		return ASTNodeExpand.expand(cus, new IMapper<ASTNode, ASTNode>(){
			@Override
			public List<ASTNode> map(ASTNode t) throws Exception {
				return TypeDeclarationAnalyzer.getMethodDeclarations(t);
			}});
	}
	
	private ICompilationUnitInformation getInformation(IJavaElement t) throws Exception
	{
		ICompilationUnitInformation info = new ICompilationUnitInformation();
		info.unit = t;
		info.unitRoot = Parser.Parse2ComilationUnit((ICompilationUnit)t);
		info.types = CompilationUnitAnalyzer.getTypes(info.unitRoot);
		info.methods = ASTNodeExpand.expand(info.types,new IMapper<ASTNode, ASTNode>(){
			@Override
			public List<ASTNode> map(ASTNode t) throws Exception {
				return TypeDeclarationAnalyzer.getMethodDeclarations(t);
			}});
		return info;
	}
	
	
	@Before
	public void method1() throws Exception
	{
		Assert.isNotNull(project);
		Assert.isNotNull(sourcePackageRoots);
		Assert.isNotNull(sourcePackages);
		Assert.isNotNull(compilationUnits);
		
		Assert.isTrue(!sourcePackageRoots.isEmpty());
		Assert.isTrue(!sourcePackages.isEmpty());
		Assert.isTrue(!compilationUnits.isEmpty());
		
		// only parse 10 compilation units.
		ExpandOperations<IJavaElement, ICompilationUnitInformation> compilationUnitExpand = 
				new ExpandOperations<IJavaElement, ICompilationUnitInformation>();
		
		this.unitInformations = compilationUnitExpand.convert(compilationUnits.subList(0, 20), 
			new IConvertor<IJavaElement,ICompilationUnitInformation>(){
			@Override
			public ICompilationUnitInformation convert(IJavaElement t) throws Exception {
				return getInformation(t);
			}});		
	}
	
	private void printStatements(ASTNode method) {
		List<ASTNode> statements = MethodDeclarationAnalyzer.getAllStatements(method);
		Assert.isTrue(statements.size() > 0);
		for(ASTNode s : statements)
		{
			logger.info(s.toString());
		}
	}
	
	
	//@Test
	public void method3() throws Exception
	{
		
		for(ICompilationUnitInformation info : unitInformations)
		{
			for(ASTNode method : info.methods)
			{
				List<List<ASTNode>> statementGroups;
				try{
					statementGroups = MethodDeclarationAnalyzer.getStatementGroups
						(method);
				}catch(Exception e)
				{
					continue;
				}
				for(List<ASTNode> statements : statementGroups)
				{
					ExtractMethodRefactoring refactoring = createRefactoring((ICompilationUnit) info.
							unit, statements);
					performChecking(refactoring);
				}
			}
		}		
	}

	private ExtractMethodRefactoring createRefactoring(
			ICompilationUnit unit, List<ASTNode> statements) {
		int start = ASTNodesAnalyzer.getNodesStartPosition(statements);
		int length = ASTNodesAnalyzer.getNodesLength(statements); 	
		logger.info("start: " + start + "; length: " + length);
		ExtractMethodRefactoring refactoring = StructuralRefactoringAPIs.
				createExtractMethodRefactoring(unit, start, length);
		return refactoring;
	}

	private void performChecking(ExtractMethodRefactoring refactoring)
			throws CoreException {
		
		refactoring.setMethodName(newMethodName);
		refactoring.setReplaceDuplicates(true);
		long startTime = System.currentTimeMillis();
		try{
		RefactoringStatus result = refactoring.checkInitialConditions(monitor);
		result = refactoring.checkFinalConditions(monitor);
		}catch(Exception e)
		{
			return;
		}
		long endTime = System.currentTimeMillis();
		logger.info("Checking time:" + (endTime - startTime));
	}
	
	
	public void extractMethodExperiment() throws Exception
	{
		 List<IJavaElement> allTypes = this.typesElement;
		 this.JavaElementListOperations.operationOnElements(allTypes, new IOperation
				 <IJavaElement>(){
			@Override
			public void perform(IJavaElement t) throws Exception {
				ExtractInterfaceProcessor processor = StructuralRefactoringAPIs.
						createExtractInterfaceRefactoring((IType)t);
				ProcessorBasedRefactoring refactoring = new ProcessorBasedRefactoring(processor);
				long start;
				long end;
				try{
					start = System.currentTimeMillis();
					refactoring.checkInitialConditions(monitor);
					refactoring.checkFinalConditions(monitor);
					end = System.currentTimeMillis();
				}catch(Exception e)
				{
					return;
				}
				logger.info("Time in checking: " + t.getElementName() + " " + (end - start));
			}});
	}
	
	
	private void moveTest(List<IJavaElement> elements, List<IJavaElement> destinations) throws 
		Exception
	{
		try{
			for(IJavaElement element : elements)
			{
				for(IJavaElement dest : destinations)
				{
					MoveRefactoring refactoring = StructuralRefactoringAPIs.createMoveRefactoring
							(element, dest);
					long startMilli = System.currentTimeMillis();
					refactoring.checkInitialConditions(monitor);
					refactoring.checkFinalConditions(monitor);
					refactoring.createChange(monitor);
					long endMilli = System.currentTimeMillis();
					logger.info(element.getElementName() + " to " + dest.getElementName() + " : " + 
							(endMilli - startMilli));
				}
			}
		}catch(Exception e)
		{
			
		}
	}
	
//	@Test
	public void moveMethods2Types() throws Exception
	{
		/*List<IJavaElement> types = this.getTypes(this.compilationUnits.subList(0, 100));
		List<IJavaElement> methods = this.JavaElementExpand.expand(types, new IMapper<IJavaElement,
				IJavaElement>(){
					@Override
					public List<IJavaElement> map(IJavaElement t)
							throws Exception {
						return ITypeAnalyzer.getMethods(t);
					}});
		this.moveTest(methods, types);*/
	}
	
	@Test
	public void moveTypes2Packages() throws Exception
	{/*
		List<IJavaElement> types = this.getTypes(this.getRandomUnits(80));
		this.moveTest(types, this.sourcePackages);*/
	}
	
//	@Test
	public void moveFields2Types() throws Exception
	{
/*		List<IJavaElement> fields = this.JavaElementExpand.expand(types, new IMapper<IJavaElement,
				IJavaElement>(){
				@Override
				public List<IJavaElement> map(IJavaElement t)
						throws Exception {
					return ITypeAnalyzer.getFields(t);
				}});
		this.moveTest(fields, types);*/
	}
}


	
	
	
	
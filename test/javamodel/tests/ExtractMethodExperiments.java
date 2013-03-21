package javamodel.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.dlf.refactoring.StructuralRefactoringAPIs;
import edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers.ASTNodesAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers.CompilationUnitAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers.MethodDeclarationAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers.TypeDeclarationAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementAnalyzers;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaModelAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentRootAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IProjectAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.util.ExpandCollection;
import edu.ncsu.dlf.refactoring.precondition.util.Parser;
import edu.ncsu.dlf.refactoring.precondition.util.Tree;
import edu.ncsu.dlf.refactoring.precondition.util.XLoggerFactory;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IConvertor;
import edu.ncsu.dlf.refactoring.precondition.util.interfaces.IMapper;


public class ExtractMethodExperiments extends RefactoringExperiment{

	private List<ASTNode> cus;
	private List<ASTNode> types;
	private List<ASTNode> methods;
	private ExpandCollection<ASTNode, ASTNode> ASTNodeExpand;
	private List<ICompilationUnitInformation> unitInformations;
	
	private class ICompilationUnitInformation
	{
		IJavaElement unit;
		ASTNode unitRoot;
		List<ASTNode> types;
		List<ASTNode> methods;
	}
	
	public ExtractMethodExperiments()
	{
		this.ASTNodeExpand = new ExpandCollection<ASTNode, ASTNode>();
		this.projectIndex = 0;
		this.logger = XLoggerFactory.GetLogger(this.getClass());
	}
	
	private List<ASTNode> parseIUs(List<IJavaElement> ius) throws Exception
	{
		ExpandCollection<IJavaElement, ASTNode> expand = new ExpandCollection<IJavaElement, 
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
		return null;
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
		ExpandCollection<IJavaElement, ICompilationUnitInformation> compilationUnitExpand = 
				new ExpandCollection<IJavaElement, ICompilationUnitInformation>();
		
		this.unitInformations = compilationUnitExpand.convert(compilationUnits.subList(0, 10), 
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
	
	
	@Test
	public void method2() throws Exception
	{
		for(ICompilationUnitInformation info : unitInformations)
		{
			for(ASTNode method : info.methods)
			{
				List<List<ASTNode>> statementGroups = MethodDeclarationAnalyzer.getStatementGroups
						(method);
				for(List<ASTNode> statements : statementGroups)
				{
					int start = ASTNodesAnalyzer.getNodesStartPosition(statements);
					int length = ASTNodesAnalyzer.getNodesLength(statements);
					ExtractMethodRefactoring refactoring = StructuralRefactoringAPIs.
							createExtractMethodRefactoring((ICompilationUnit)info.unit, start, 
									length);
				
				}
			}
		}		
	}


	
	
	
	
	
	
	
	
}

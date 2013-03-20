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
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers.CompilationUnitAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.ASTAnalyzers.TypeDeclarationAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.ICompilationUnitAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaElementUtils;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IJavaModelAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IPackageFragmentRootAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.JavaModelAnalyzers.IProjectAnalyzer;
import edu.ncsu.dlf.refactoring.precondition.util.ExpandCollection;
import edu.ncsu.dlf.refactoring.precondition.util.IConvertor;
import edu.ncsu.dlf.refactoring.precondition.util.IMapper;
import edu.ncsu.dlf.refactoring.precondition.util.Parser;
import edu.ncsu.dlf.refactoring.precondition.util.XLoggerFactory;


public class ExtractMethodExperiments extends RefactoringExperiment{

	List<ASTNode> cus;
	List<ASTNode> types;
	List<ASTNode> methods;
	ExpandCollection<ASTNode, ASTNode> ASTNodeExpand;
	
	
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
		
		cus = parseIUs(compilationUnits.subList(0, 9));
		types = getTypes(cus);
		methods = getMethods(types);
		
		Assert.isTrue(cus.size() > 0);
		Assert.isTrue(types.size() > 0);
		Assert.isTrue(methods.size() > 0);
	}
	
	@Test
	public void method2()
	{
		
	}
	
	
	
	
	
	
	
	
	
}

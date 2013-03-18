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

import util.ExpandCollection;
import util.IMapper;
import util.Parser;
import util.XLoggerFactory;
import ASTAnalyzers.CompilationUnitAnalyzer;
import ASTAnalyzers.TypeDeclarationAnalyzer;
import JavaModelAnalyzer.ICompilationUnitAnalyzer;
import JavaModelAnalyzer.IJavaElementUtils;
import JavaModelAnalyzer.IJavaModelAnalyzer;
import JavaModelAnalyzer.IPackageFragmentAnalyzer;
import JavaModelAnalyzer.IPackageFragmentRootAnalyzer;
import JavaModelAnalyzer.IProjectAnalyzer;

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
		return expand.expand(ius, new IMapper<IJavaElement, ASTNode>(){
			@Override
			public List<ASTNode> map(IJavaElement t) throws Exception {
				ASTNode element = Parser.Parse2ComilationUnit((ICompilationUnit)t);
				ArrayList<ASTNode> list = new ArrayList<ASTNode>();
				list.add(element);
				return list;
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

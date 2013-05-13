package javamodel.tests;

import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.eclipse.core.internal.dtree.IComparator;
import org.eclipse.jdt.core.IJavaElement;

import dlf.refactoring.precondition.JavaModelAnalyzers.ITypeAnalyzer;
import dlf.refactoring.precondition.util.BinarySearch;
import dlf.refactoring.precondition.util.StringUtils;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IMapper;

public class GetNamesExperiment extends RefactoringExperiment {
	
	private XArrayList<IJavaElement> methods;
	private XArrayList<String> methodNames;
	private XArrayList<String> typeNames;
	
	@Before
	public void getAllMethods() throws Exception {
		methods = this.typeElements.select(new IMapper<IJavaElement, IJavaElement>(){
			@Override
			public List<IJavaElement> map(IJavaElement t) throws Exception {
				return ITypeAnalyzer.getMethods(t);
			}});
		
		methodNames = methods.convert(new IConvertor<IJavaElement, String>(){
			@Override
			public String convert(IJavaElement t) throws Exception {
				return t.getElementName();
			}});
		
		typeNames = this.typeElements.convert(new IConvertor<IJavaElement, String>(){
			@Override
			public String convert(IJavaElement t) throws Exception {
				return t.getElementName();
			}});
	}
	
	@Test
	public void method1() {
		methodNames.orderBy(new Comparator<String>(){
			@Override
			public int compare(String arg0, String arg1) {
				return arg0.compareTo(arg1);
			}});
		String[] names = methodNames.toArray(new String[0]);
		
		StringUtils.createRandomString(StringUtils.getJavaVariableCharacters(), 250);
		 
		for(int j = 0; j < 100; j ++) {
			long start = System.currentTimeMillis();
			for(int i = 0; i < 1000; i ++) {
				String name = StringUtils.createRandomString(StringUtils.getJavaVariableCharacters(), 250);
				BinarySearch.binarySearch(names, name);
			}
			long time = System.currentTimeMillis() - start;
			logger.info(time);
		}
	}
	
	
}

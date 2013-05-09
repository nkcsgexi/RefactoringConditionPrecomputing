package javamodel.tests;

import java.io.BufferedReader;
import java.io.IOException;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;


import dlf.refactoring.enums.RefactoringType;
import dlf.refactoring.precondition.util.ExcelUtils;
import dlf.refactoring.precondition.util.ExcelUtils.CellContent;
import dlf.refactoring.precondition.util.FileUtils;
import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IIndexedIteration;
import dlf.refactoring.precondition.util.interfaces.IOperation;
import dlf.refactoring.precondition.util.interfaces.IPredicate;


public class LogFileAnalysis{
	
	private final String allTimeContains = "Finish a refactoring context";

		
	private String getContainedString(RefactoringType type) {
		return "Checking conditions for " + type.toString();
	}
	
	private XArrayList<Integer> ruleOutSmallTime(XArrayList<Integer> times, final int limit) throws Exception {
		return times.where(new IPredicate<Integer>(){
			@Override
			public boolean IsTrue(Integer t) throws Exception {
				return t > limit;
			}});
	}
	
	
	@Test
	public void method1() throws Exception {
		XArrayList<Integer> times = getTimes(allTimeContains);
		times = ruleOutSmallTime(times, 100);
		createExcel("time.xls", "Overall", 0, times);
		
		times = getTimes(getContainedString(RefactoringType.PULL_UP_METHOD));
		times = ruleOutSmallTime(times, 10);
		createExcel("pullUp.xls", RefactoringType.PULL_UP_METHOD.toString(), 0, times);
		
		times = getTimes(getContainedString(RefactoringType.PUSH_DOWN_METHOD));
		times = ruleOutSmallTime(times, 10);
		createExcel("pushDown.xls", RefactoringType.PUSH_DOWN_METHOD.toString(), 0, times);
		
		times = getTimes(getContainedString(RefactoringType.RENAME_METHOD));
		times = ruleOutSmallTime(times, 10);
		createExcel("reMethod.xls", RefactoringType.RENAME_METHOD.toString(), 0, times);
		
		times = getTimes(getContainedString(RefactoringType.RENAME_CLASS));
		times = ruleOutSmallTime(times, 10);
		createExcel("reClass.xls", RefactoringType.RENAME_CLASS.toString(), 0, times);
	}


	private XArrayList<Integer> getTimes(String contains) throws Exception, IOException {
		XArrayList<Integer> times = new XArrayList<Integer>();
		BufferedReader reader = XLoggerFactory.getAllLogReader();
		Assert.isNotNull(reader);
		String line;
		while ((line = reader.readLine()) != null) {
			if(line.contains(contains)) {
				String[] subLines = line.split(":");
				int time = Integer.parseInt(subLines[subLines.length-1]);
				times.add(time);
			}
		}
		return times;
	}
	

	private void createExcel(String file, String columnName, final int columnIndex, XArrayList<Integer> times) throws Exception {
		String filePath = FileUtils.getDesktopPath() + file;
		final XArrayList<CellContent> cells = new XArrayList<CellContent>();
		times.indexedLoop(new IIndexedIteration<Integer>(){
			@Override
			public void iterate(Integer t, int i) {
				cells.add(new CellContent(i + 1, columnIndex, t.toString(), true));
			}});
		ExcelUtils.createExcel(filePath, new String[]{columnName},  cells);
	}

}

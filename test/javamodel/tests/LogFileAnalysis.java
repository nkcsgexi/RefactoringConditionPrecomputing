package javamodel.tests;

import java.io.BufferedReader;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;


import dlf.refactoring.precondition.util.XArrayList;
import dlf.refactoring.precondition.util.XLoggerFactory;
import dlf.refactoring.precondition.util.interfaces.IConvertor;
import dlf.refactoring.precondition.util.interfaces.IOperation;


public class LogFileAnalysis{
	
	@Test
	public void method1() throws Exception
	{
		XArrayList<Integer> times = new XArrayList<Integer>();
		BufferedReader reader = XLoggerFactory.getAllLogReader();
		Assert.isNotNull(reader);
		String line;
		while ((line = reader.readLine()) != null) {
			if(line.contains("Finish a refactoring context")) {
				String[] subLines = line.split(":");
				int time = Integer.parseInt(subLines[subLines.length-1]);
				times.add(time);
			}
		}
		
		Double average = times.sum(new IConvertor<Integer, Double>(){
			@Override
			public Double convert(Integer t) throws Exception {
				return t.doubleValue();
			}})/times.size();
		System.out.println(times.size());
		System.out.println(average);
	}

}

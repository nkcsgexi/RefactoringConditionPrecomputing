package dlf.refactoring.precondition.util;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import dlf.refactoring.precondition.util.interfaces.IOperation;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtils {
	
	public static void createExcel(String inputFile, String[] labels, XArrayList<CellContent> contents) 
			throws Exception {
		new InternalExcelCreator(inputFile, labels, contents);
	}	
	
	public static class CellContent {
		public final int column;
		public final int row;
		public final String content;
		
		public CellContent(int row, int column, String content) {
			this.column = column;
			this.row = row;
			this.content = content;
		}
	}
	
	private static class InternalExcelCreator {

		private WritableCellFormat timesBoldUnderline;
		private WritableCellFormat times;

		private InternalExcelCreator(final String inputFile, String[] labels, XArrayList<CellContent> contents) throws Exception {
			File file = new File(inputFile);
			WorkbookSettings wbSettings = new WorkbookSettings();
			wbSettings.setLocale(new Locale("en", "EN"));

			final WritableWorkbook workbook = Workbook.createWorkbook(file,wbSettings);
			workbook.createSheet("Report", 0);
			final WritableSheet firstSheet = workbook.getSheet(0);
			
			WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
			this.times = new WritableCellFormat(times10pt);
			times.setWrap(true);
			WritableFont times10ptBoldUnderline = new WritableFont(
					WritableFont.TIMES, 10, WritableFont.BOLD, false,
					UnderlineStyle.SINGLE);
			this.timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
			timesBoldUnderline.setWrap(true);
			
			createLabel(firstSheet, labels);
			
			contents.operateOnElement(new IOperation<CellContent>(){
				@Override
				public void perform(CellContent t) throws Exception {
					addLabel(firstSheet, t.column, t.row, t.content);
				}});
			
			workbook.write();
			workbook.close();
		}
		

		private void createLabel(WritableSheet sheet, String[] labels) throws Exception {
			CellView cv = new CellView();
			cv.setFormat(times);
			cv.setFormat(timesBoldUnderline);
			cv.setAutosize(true);

			for(int i = 0; i< labels.length; i ++) {
				addCaption(sheet, i, 0, labels[i]);
			}
		}


		private void addCaption(WritableSheet sheet, int column, int row, String s) throws Exception {
			Label label;
			label = new Label(column, row, s, timesBoldUnderline);
			sheet.addCell(label);
		}

		private void addNumber(WritableSheet sheet, int column, int row, Integer integer) throws Exception {
			Number number;
			number = new Number(column, row, integer, times);
			sheet.addCell(number);
		}

		private void addLabel(WritableSheet sheet, int column, int row, String s) throws Exception {
			Label label;
			label = new Label(column, row, s, times);
			sheet.addCell(label);
		}
	}
}

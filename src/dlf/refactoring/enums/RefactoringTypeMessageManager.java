package dlf.refactoring.enums;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

import dlf.refactoring.precondition.util.FileUtils;

public class RefactoringTypeMessageManager {

	private final String directory;
	private final HashMap<RefactoringType, BufferedWriter> writers;
	private static RefactoringTypeMessageManager manager;
	
	public static RefactoringTypeMessageManager getInstance() throws Exception {
		if(manager == null) {
			manager = new RefactoringTypeMessageManager();
		}
		return manager;
	}
	
	private RefactoringTypeMessageManager() throws Exception {
		this.directory = FileUtils.getDesktopPath() + "\\messages";
		this.writers = new HashMap<RefactoringType, BufferedWriter>();
		for(RefactoringType type : RefactoringType.values()) {
			String path = this.directory + "\\" + type.name() + ".log";
			FileUtils.createFileIfNotExist(path);
			this.writers.put(type, new BufferedWriter(new FileWriter(path, true)));
		}
	}
	
	public void finalize() throws Exception {
		for(BufferedWriter w : writers.values()) {
			w.close();
		}
	}
	
	public void recordMessage(RefactoringType type, String message) throws Exception {
		writers.get(type).append(message);
		writers.get(type).newLine();
	}
	
}

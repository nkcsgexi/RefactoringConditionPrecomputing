package dlf.refactoring.precondition.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;

public class FileUtils {

	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	public static File getFile(String path)
	{
		return new File(path);
	}
	
	public static void removeFilesInDirectory(String directory) throws Exception
	{
		File fin = new File(directory);
		for (File file : fin.listFiles()) {
		    FileDeleteStrategy.FORCE.delete(file);
		}   
	}
}

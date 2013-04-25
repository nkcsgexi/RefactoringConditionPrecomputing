package dlf.refactoring.precondition.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;

public class FileUtils {

	public static void deleteDirectory(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteDirectory(f);
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
	
	public static void append2File(String path, String text) throws Exception
	{
		BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
		output.append(text);
		output.close();
	}
	
	public static String getDesktopPath()
	{
		return "C:\\Users\\xige\\Desktop\\test";
	}
	
	public static void createFileIfNotExist(String path) throws Exception
	{
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs(); 
			f.createNewFile();
		}
	}
	
}

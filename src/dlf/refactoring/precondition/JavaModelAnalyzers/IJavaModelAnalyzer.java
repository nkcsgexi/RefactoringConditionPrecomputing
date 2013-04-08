package dlf.refactoring.precondition.JavaModelAnalyzers;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/*
 * The java model instance is equal to a java work space, which is the root of all the java model.
 * */
public class IJavaModelAnalyzer {

	public static IJavaProject[] getJavaProjects(IJavaModel model) throws JavaModelException
	{
		return model.getJavaProjects();
	}
	
	public static IJavaProject[] getCurrentJavaProjects() throws JavaModelException
	{	
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IJavaModel model = JavaCore.create(ws.getRoot());
		return model.getJavaProjects();	
	}
}

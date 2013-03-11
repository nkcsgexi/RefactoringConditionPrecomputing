package condition;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;

public class JavaElementChangeListener implements IElementChangedListener{

	@Override
	public void elementChanged(ElementChangedEvent event) {
		IJavaElementDelta delta = event.getDelta();

	}

}

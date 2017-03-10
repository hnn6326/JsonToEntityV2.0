package jsontoentity.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class OperHandler extends AbstractHandler {
	private static CreateEntityFrame frame;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		 IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		if(frame==null)
		{
			frame= new CreateEntityFrame("Json to entity");
		}
		frame.setVisible(true);
		return null;
	}
}
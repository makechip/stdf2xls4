package com.makechip.util.widgets.console.widget;
import java.awt.event.*;
import com.makechip.util.widgets.console.Console;

public abstract class NormalAction extends GenericAction implements ActionListener
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6611799131801056209L;

	protected NormalAction(Console f, Class<?> iconPackageClass)
    {
        super(f, iconPackageClass);
    }

    public void actionPerformed(ActionEvent e)
    {
        f.exec(getCommand());
    }

    protected abstract String getCommand();

    protected abstract String getActionName();

    protected abstract String getIconName();

    protected abstract String getToolTip();

}

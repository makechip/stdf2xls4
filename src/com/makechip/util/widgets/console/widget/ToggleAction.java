package com.makechip.util.widgets.console.widget;
import java.awt.event.*;
import javax.swing.*;
import com.makechip.util.widgets.console.Console;

public abstract class ToggleAction extends GenericAction implements ActionListener
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5739742370292012422L;

	protected ToggleAction(Console f, Class<?> iconPackageClass)
    {
        super(f, iconPackageClass);
    }

    public void actionPerformed(ActionEvent e)
    {
        AbstractButton b = AbstractButton.class.cast(e.getSource());
        if (b.isSelected()) f.exec(getOnCommand());
        else f.exec(getOffCommand());
    }

    protected abstract String getOnCommand();

    protected abstract String getOffCommand();

    protected abstract String getActionName();

    protected abstract String getIconName();

    protected abstract String getToolTip();

}

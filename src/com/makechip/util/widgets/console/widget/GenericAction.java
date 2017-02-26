package com.makechip.util.widgets.console.widget;
import javax.swing.*;
import java.net.*;
import com.makechip.util.widgets.console.Console;

public abstract class GenericAction extends AbstractAction
{
    private static final long serialVersionUID = 1;
    protected Console f;

    protected GenericAction(Console f, Class<?> iconPackageClass)
    {
        this.f = f;
        putValue(Action.NAME, getActionName());
        String icon = getIconName();
        if (icon != null)
        {
            URL imageURL = iconPackageClass.getResource(icon);
            putValue(Action.LARGE_ICON_KEY, new ImageIcon(imageURL));
        }
        putValue(Action.SHORT_DESCRIPTION, getToolTip());
        setEnabled(true);
    }

    protected abstract String getActionName();

    protected abstract String getIconName();

    protected abstract String getToolTip();

    protected static void check(String name, Action action)
    {
        if (action == null)
        {
            throw new RuntimeException(name + " action not initialized");
        }
    }

}


package util;

import org.tbot.methods.Widgets;
import org.tbot.wrappers.WidgetChild;

public final class WidgetsUtil {
    public static WidgetChild chatBoxWidget() {
        return Widgets.getWidget(162, 41);
    }

    /**
     * Returns the interact box that comes when you use items on each other
     *
     * @return an interactable widget
     */
    public static WidgetChild interactWidget() {
        return Widgets.getWidget(309, 6);
    }

    public static WidgetChild levelUpWidget() {
        return Widgets.getWidget(233, 1);
    }

    public static WidgetChild humidifiyWidget() {
        return Widgets.getWidget(218, 101);
    }
}

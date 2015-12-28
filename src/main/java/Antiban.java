import org.tbot.methods.Random;
import util.Widgets2;

public class Antiban {
    public Antiban() {

    }

    public boolean openRandomTab() {
        int randomTab = Random.randomFromArray(Widgets2.tabIDs());

        return Widgets2.openTab(randomTab);
    }


}

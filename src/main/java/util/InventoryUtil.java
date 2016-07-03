package util;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Bank;
import org.tbot.methods.Time;
import org.tbot.methods.tabs.Inventory;
import org.tbot.util.Filter;
import org.tbot.wrappers.Item;

public final class InventoryUtil {
    private static void withdrawItems(int numToWithdraw, String... withdrawItems) {
        if(Bank.isOpen()) {
            for (String item : withdrawItems) {
                LogHandler.log("Withdrawing " + numToWithdraw + " " + item + "s");

                while (!Inventory.contains(item)) {
                    if (numToWithdraw > 28)
                        Bank.withdrawAll(item);
                    else
                        Bank.withdraw(item, numToWithdraw);
                    Time.sleepUntil(() -> Inventory.contains(item), 5000);
                    Time.sleep(2000, 3500);
                }
            }
        }
    }

    public static void BankAllAndWithdraw(Filter<Item> depositFilter, int numToWithDraw, String... withdrawItems) {
        Bank.openNearestBank();

        BotscriptsUtil.sleepConditionWithExtraWait(Bank::isOpen, 0, 500);

        if (Bank.isOpen()) {
            Time.sleep(750, 1500);

            Bank.depositAll(depositFilter);

            Time.sleepUntil(() -> Inventory.getFirst(depositFilter) == null, 5000);

            Time.sleep(500, 1500);

            if (!Bank.containsAll(withdrawItems)) {
                LogHandler.log("Bank does not contain all items required, pausing script");
                BotscriptsUtil.pauseScript();
            }

            withdrawItems(numToWithDraw, withdrawItems);

            while (Bank.isOpen())
                Bank.close();

            Time.sleep(250, 2000);
        }
    }

    public static void BankAllAndWithdraw(int numToWithDraw, String... withdrawItems) {
        Bank.openNearestBank();

        BotscriptsUtil.sleepConditionWithExtraWait(Bank::isOpen, 0, 500);

        if (Bank.isOpen()) {
            Time.sleep(200, 500);

            Bank.depositAll();

            if (!Bank.containsAll(withdrawItems)) {
                LogHandler.log("Bank does not contain all items required, pausing script");
                BotscriptsUtil.pauseScript();
            }

            withdrawItems(numToWithDraw, withdrawItems);

            while (Bank.isOpen())
                Bank.close();

            Time.sleep(250, 2000);
        }
    }
}

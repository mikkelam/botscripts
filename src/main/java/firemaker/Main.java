package firemaker;

import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.Bank;
import org.tbot.methods.Random;
import org.tbot.methods.Time;
import org.tbot.wrappers.Timer;

import javax.swing.*;
import java.awt.*;

@Manifest(
        name = "Martin's Firemaker",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Burns any logs from the nearest bank",
        category = ScriptCategory.FIREMAKING
)
public class Main extends AbstractScript implements PaintListener {
    private final MouseTrail mt = new MouseTrail();
    private final Timer t = new Timer();
    private JFrame frame;
    private String logToBurn;

    @Override
    public boolean onStart() {
        log("Starting Martin's Firemaker");

        setupGUI();

        // Wait until user has finished typing value in
        while(frame.isVisible()) {
            Time.sleep(200);
        }

        if(logToBurn == null) {
            log("You have to pick a log to burn, exiting!");
            return false;
        }

        log("ALL DONE, starting log type: '" + logToBurn + "'");

        return true;
    }

    private void setupGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton startButton = new JButton();
        startButton.setText("Start");

        JLabel wassup = new JLabel("wassup");
        JTextField logType = new JTextField(30);

        panel.add(wassup);
        panel.add(logType);
        panel.add(startButton);

        frame = new JFrame("Martin's Firemaker");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.setSize(300, 300);
        //frame.pack();
        frame.setVisible(true);

        startButton.addActionListener(e -> {
            logToBurn = logType.getText();
            frame.dispose();
        });
    }

    @Override
    public int loop() {
        if(Bank.isOpen()) {
            log("Woo bank open!!!!!!!!!!!");
        }
        else {
            log("opening nearest bank");
            Bank.openNearestBank();
        }


        return Random.nextInt(120, 260); // wait random amount of time until next loop
    }

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.red);

        graphics.setColor(Color.white);
        //graphics.drawString("Martin's Firemaker", 100, 100);
        graphics.drawString("Time elapsed: " + t.getTimeRunningString(), 50, 100);


    }
}

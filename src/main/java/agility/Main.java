package agility;

import org.tbot.bot.TBot;
import org.tbot.concurrency.LoopTask;
import org.tbot.concurrency.Task;
import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.event.events.InventoryEvent;
import org.tbot.internal.event.listeners.InventoryListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Walking;
import org.tbot.wrappers.*;
import org.tbot.wrappers.Timer;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

import agility.Obstacles.*;
import util.Antiban;
import util.SkillTracker;

/**
 * Code stolen from https://gist.github.com/anonymous/504724c2bc69948151a9
 * Inserted by Saph
 * Needs a lot of fixing.
 *
 */
@Manifest(authors = "Mad", name = "Rooftop Agility")
public class Main extends AbstractScript implements PaintListener, InventoryListener {

    private final Timer hopTimer = new Timer(Random.nextInt(2700000, (int) 4.5e+6));
    private static boolean walkedToGnome = false;
    private int marksLooted;
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.AGILITY);
    private final GameUtil util = new GameUtil();
    private HashMap<LinkedHashMap<Tile, Runnable>, Boolean> hashMaps = new HashMap<>();
    private LinkedHashMap<Tile, Runnable> currentCourse = new LinkedHashMap<>();
    private LinkedHashMap<Tile, Runnable> draynorMap = new LinkedHashMap<>();
    private LinkedHashMap<Tile, Runnable> faladorMap = new LinkedHashMap<>();
    private LinkedHashMap<Tile, Runnable> seersMap = new LinkedHashMap<>();
    private LinkedHashMap<Tile, Runnable> rellekaMap = new LinkedHashMap<>();
    private LinkedHashMap<Tile, Runnable> ardyMap = new LinkedHashMap<>();
    private LinkedHashMap<Tile, Runnable> varrockMap = new LinkedHashMap<>();
    private LinkedHashMap<Tile, Runnable> canifisMap = new LinkedHashMap<>();
    private LinkedHashMap<Boolean, Runnable> gnomeMap = new LinkedHashMap<>();

    private int currentLevel() {
        return Skills.getCurrentLevel(Skills.Skill.AGILITY);
    }

    private void populateHash() {
        hashMaps.put(draynorMap, currentLevel() >= 10 && currentLevel() < 30);
        hashMaps.put(varrockMap, currentLevel() >= 30  && currentLevel() < 40);
        hashMaps.put(canifisMap, currentLevel() >= 40  && currentLevel() < 50);
        hashMaps.put(faladorMap, currentLevel() >= 50 && currentLevel() < 60);
        hashMaps.put(seersMap, currentLevel() >= 60 && currentLevel() < 80);
        // Preferably Pollnivneach Agility Course from 70 to 80, and Seers to 70
        hashMaps.put(rellekaMap, currentLevel() >= 80 && currentLevel() < 90);
        hashMaps.put(ardyMap, currentLevel() >= 90);
    }

    public boolean onStart() {
        TBot.getBot().getScriptHandler().invoke(new LoopTask() {
            @Override
            public int loop() {
                try {
                    if (TBot.getBot().getScriptHandler().getScript() == null || !TBot.getBot().getScriptHandler().getScript().isRunning()) {
                        return -1;
                    }
                    if (hopTimer.isFinished()) {
                        Game.instaHopRandomP2P();
                        hopTimer.reset();
                        Time.sleepUntil(Game::isLoggedIn, 10000);
                    }
                } catch (ArrayIndexOutOfBoundsException ae) {
                    ae.printStackTrace();
                }
                return Random.nextInt(120,260);
            }
        });

        populateGnome();
        populateDraynor();
        populateVarrock();
        populateCanifis();
        populateFalador();
        populateSeers();
        populateRelleka();
        populateArdy();
        populateHash();
        return true;
    }

    @Override
    public int loop() {

        if(currentLevel() >= 10) {
            hashMaps.forEach((tileRunnableLinkedHashMap, aBool) -> tileRunnableLinkedHashMap.forEach((tile, runnable) -> {
                if (aBool) {
                    currentCourse.putIfAbsent(tile, runnable);
                }
            }));

            if(!util.needToEat() || util.needToEat() && Game.getPlane() != 0) {
                currentCourse.forEach((tile, runnable) -> {
                    if (util.markGrace() == null) {
                        if ((Walking.canReach(tile) && !Players.getLocal().isMoving() &&  Players.getLocal().getAnimation() != 1) || Game.getPlane() == 0 && tile.getPlane() == 0){
                            if (!tile.isOnScreen() && tile.isLoaded())
                                getContainer().invoke(new Task() {//This might cause multiple camera movements
                                    @Override
                                    public void execute() {
                                        Camera.turnTo(tile);
                                    }
                                });
                            runnable.run();
                        }

                    }
                });
            } else handleFood();

            if (util.markGrace() != null && !Players.getLocal().isMoving()) {
                if (util.markGrace().isOnScreen() ? util.markGrace().pickUp() : Walking.walkTileMM(util.markGrace().getLocation()))
                    Time.sleepUntil(() -> util.markGrace() == null, Random.nextInt(1200, 1650));
            }

        } else {
            if(!walkedToGnome) {
                walkToGnome();
            } else {
                gnomeMap.forEach((bool, runnable) -> {
                    if (bool && !Players.getLocal().isMoving()) {
                        runnable.run();
                    }
                });
            }


        }
        return Random.nextInt(240, 460);
    }

    private void populateGnome() {
        gnomeMap.put(inArea(new Area(new Tile[]  {new Tile(2478,3434,0),new Tile(2470,3434,0),new Tile(2471,3438,0),new Tile(2478,3439,0)})), () -> util.interact(new GObject("Log balance").getObject(), "Walk-across"));
        gnomeMap.put(inArea(new Area(new Tile[]{new Tile(2480, 3429, 0), new Tile(2479, 3423, 0), new Tile(2470, 3423, 0), new Tile(2470, 3430, 0)})), () -> util.interact(new GObject("Obstacle net").getObject(), "Climb-over"));
        gnomeMap.put(Game.getPlane() == 1, () -> util.interact(new GObject("Tree branch").getObject(), "Climb"));
        gnomeMap.put(Walking.canReach(new Tile(2474, 3420, 2)), () -> util.interact(new GObject("Balancing rope").getObject(), "Cross"));
        gnomeMap.put(Walking.canReach(new Tile(2484, 3420, 2)), () -> util.interact(new GObject("Tree branch").getObject(), "Climb-down"));
        gnomeMap.put(inArea(new Area(new Tile[]{new Tile(2481, 3417, 0), new Tile(2481, 3426, 0), new Tile(2490, 3426, 0), new Tile(2489, 3417, 0)})),
                () -> util.interact(new GObject("Obstacle net", new Tile(2485,3426,0)).getObject(), "Climb-over"));
        gnomeMap.put(inArea(new Area(new Tile[]{new Tile(2490, 3427, 0), new Tile(2482, 3427, 0), new Tile(2481, 3434, 0), new Tile(2490, 3435, 0)})), () -> util.interact(new GObject("Obstacle pipe").getObject(), "Squeeze-through"));
    }

    private void populateDraynor() {
        draynorMap.put(Draynor.roughWallTile, () -> util.handleWall(Draynor.roughWallTile, "Rough wall", "Climb"));
        draynorMap.put(Draynor.firstRopeTile, () -> util.interact(new GObject("Tightrope", new Tile(3098, 3277, 3)).getObject(), "Cross"));
        draynorMap.put(Draynor.secondRopeTile, () -> util.interact(new GObject("Tightrope", new Tile(3092, 3276, 3)).getObject(), "Cross"));
        draynorMap.put(Draynor.narrowWallTile, () -> util.interact(new GObject("Narrow wall").getObject(), "Balance"));
        draynorMap.put(Draynor.bigWallTile, () -> util.interact(new GObject("Wall").getObject(), "Jump-up"));
        draynorMap.put(Draynor.jumpGapTile, () -> util.interact(new GObject("Gap").getObject(), "Jump"));
        draynorMap.put(Draynor.climbCrateTile, () -> util.interact(new GObject("Crate").getObject(), "Climb-down"));
    }

    private void populateVarrock() {
        varrockMap.put(Varrock.wallTile, () -> util.handleWall(Varrock.wallTile, "Rough wall", "Climb"));
        varrockMap.put(Varrock.firstClothesLineTile, () -> util.interact(new GObject("Clothes line").getObject(), "Cross"));
        varrockMap.put(Varrock.firstGapTile, () -> util.interact(new GObject("Gap").getObject(), "Leap"));
        varrockMap.put(Varrock.balanceWallTile, () -> util.interact(new GObject("Wall").getObject(), "Balance"));
        varrockMap.put(Varrock.secondGapTile, () -> util.interact(new GObject("Gap").getObject(), "Leap"));
        varrockMap.put(Varrock.thirdGapTile, () -> util.interact(new GObject("Gap", new Tile(3209,3397,3)).getObject(), "Leap"));
        varrockMap.put(Varrock.fourthGapTile, () -> util.interact(new GObject("Gap", new Tile(3233,3402,3)).getObject(), "Leap"));
        varrockMap.put(Varrock.ledgeTile, () -> util.interact(new GObject("Ledge").getObject(), "Hurdle"));
        varrockMap.put(Varrock.lastEdgeTile, () -> util.interact(new GObject("Edge").getObject(), "Jump-off"));
    }

    private void populateCanifis() {
        canifisMap.put(Canifis.treeTile, () -> util.handleWallWalk(Canifis.treeTile,Canifis.treeWalkTile, "Tall tree", "Climb"));
        canifisMap.put(Canifis.firstGapTile, () -> util.interact(new GObject("Gap").getObject(), "Jump"));
        canifisMap.put(Canifis.secondGapTile, () -> util.interact(new GObject("Gap", new Tile(3496,3504,2)).getObject(), "Jump"));
        canifisMap.put(Canifis.thirdGapTile, () -> util.interact(new GObject("Gap", new Tile(3486,3499,2)).getObject(), "Jump"));
        canifisMap.put(Canifis.fourthGapTile, () -> util.interact(new GObject("Gap", new Tile(3478,3492,3)).getObject(), "Jump"));
        canifisMap.put(Canifis.poleTile, () -> util.interact(new GObject("Pole-vault", new Tile(3480,3483,2)).getObject(), "Vault"));
        canifisMap.put(Canifis.fifthGapTile, () -> util.interact(new GObject("Gap", new Tile(3503,3476,3)).getObject(), "Jump"));
        canifisMap.put(Canifis.sixthGapTile, () -> util.interact(new GObject("Gap", new Tile(3510,3483,2)).getObject(), "Jump"));
    }

   private void populateFalador() {
        faladorMap.put(Falador.roughWallTile, () -> util.handleWall(Falador.roughWallTile, "Rough wall", "Climb"));
        faladorMap.put(Falador.firstRopeTile, () -> util.interact(new GObject("Tightrope").getObject(),"Cross"));
        faladorMap.put(Falador.handHoldsTile, () -> util.interact(new GObject("Hand holds", new Tile(3050, 3351,3)).getObject(), "Cross"));
        faladorMap.put(Falador.firstGapTile, () -> util.interact(new GObject("Gap", new Tile(3048,3359,3)).getObject(), "Jump"));
        faladorMap.put(Falador.secondGapTile, () -> util.interact(new GObject("Gap", new Tile(3044,3362,3)).getObject(), "Jump"));
        faladorMap.put(Falador.secondRopeTile, () -> util.interact(new GObject("Tightrope", new Tile(3034,3361,3)).getObject(), "Cross"));
        faladorMap.put(Falador.thirdRopeTile, () -> util.interact(new GObject("Tightrope", new Tile(3026,3353,3)).getObject(), "Cross"));
        faladorMap.put(Falador.thirdGapTile, () -> util.interact(new GObject("Gap", new Tile(3017, 3352, 3)).getObject(), "Jump"));
        faladorMap.put(Falador.firsLedgeTile, () -> util.interact(new GObject("Ledge", new Tile(3015, 3345,3)).getObject(), "Jump"));
        faladorMap.put(Falador.secondLedgeTile, () -> util.interact(new GObject("Ledge", new Tile(3012, 3343,3)).getObject(), "Jump"));
        faladorMap.put(Falador.thirdLedgeTile, () -> util.interact(new GObject("Ledge", new Tile(3012, 3334,3)).getObject(), "Jump"));
        faladorMap.put(Falador.fourthLedgeTile, () -> util.interact(new GObject("Ledge", new Tile(3018, 3333,3)).getObject(), "Jump"));
        faladorMap.put(Falador.fifthLedgeTile, () -> util.interact(new GObject("Edge", new Tile(3025,3334,3)).getObject(), "Jump"));
    }

    private void populateSeers() {
        seersMap.put(Seers.wallTile, () -> util.handleWallWalk(Seers.wallTile,Seers.wallWalkTile, "Wall", "Climb-up"));
        seersMap.put(Seers.firstGapTile, () -> util.interact(new GObject("Gap").getObject(), "Jump"));
        seersMap.put(Seers.firstRopeTile, () -> util.interact(new GObject("Tightrope").getObject(), "Cross"));
        seersMap.put(Seers.secondGapTile, () -> util.interact(new GObject("Gap").getObject(), "Jump"));
        seersMap.put(Seers.thirdGapTile, () -> util.interact(new GObject("Gap", new Tile(2701,3469,3)).getObject(), "Jump"));
        seersMap.put(Seers.edgeTile, () -> util.interact(new GObject("Edge").getObject(), "Jump"));
    }

    private void populateRelleka() {
        rellekaMap.put(Relleka.wallTile, () -> util.handleWall(Relleka.wallTile, "Rough wall", "Climb"));
        rellekaMap.put(Relleka.firstGapTile, () -> util.interact(new GObject("Gap").getObject(), "Jump"));
        rellekaMap.put(Relleka.firstTightRopeTile, () -> util.interact(new GObject("Tightrope").getObject(), "Cross"));
        rellekaMap.put(Relleka.secondGapTile, () -> util.interact(new GObject("Gap").getObject(), "Jump"));
        rellekaMap.put(Relleka.thirdGapTile, () -> util.interact(new GObject("Gap", new Tile(2643, 3654,3)).getObject(), "Jump"));
        rellekaMap.put(Relleka.secondTightRopeTile, () -> util.interact(new GObject("Tightrope").getObject(), "Cross"));
        rellekaMap.put(Relleka.pileOfFishTile, () -> util.interact(new GObject("Pile of fish").getObject() , "Jump-in"));
    }

    private void populateArdy() {
        ardyMap.put(Ardy.wallTile, () -> util.handleWall(Ardy.wallTile, "Wooden Beams", "Climb-up"));
        ardyMap.put(Ardy.firstGap, () -> util.interact(new GObject("Gap").getObject(), "Jump"));
        ardyMap.put(Ardy.plank, () -> util.interact(new GObject("Plank").getObject(), "Walk-on"));
        ardyMap.put(Ardy.secondGap, () -> util.interact(new GObject("Gap").getObject(), "Jump"));
        ardyMap.put(Ardy.thirdGap, () -> util.interact(new GObject("Gap", new Tile(2653,3309,3)).getObject(), "Jump"));
        ardyMap.put(Ardy.steepRoof, () -> util.interact(new GObject("Steep roof").getObject(), "Balance-across"));
        ardyMap.put(Ardy.fourthGap, () -> util.interact(new GObject("Gap").getObject(), "Jump"));
    }


    final Tile[] pathToCourse = new Tile[] {new Tile(2519, 3568, 0), new Tile(2519, 3563, 0), new Tile(2522, 3559, 0), new Tile(2518, 3556, 0), new Tile(2516, 3551, 0), new Tile(2515, 3546, 0), new Tile(2516, 3541, 0), new Tile(2518, 3536, 0), new Tile(2519, 3531, 0), new Tile(2518, 3526, 0), new Tile(2523, 3524, 0), new Tile(2525, 3519, 0), new Tile(2526, 3514, 0), new Tile(2525, 3509, 0), new Tile(2527, 3504, 0), new Tile(2529, 3499, 0), new Tile(2530, 3494, 0), new Tile(2531, 3489, 0), new Tile(2531, 3484, 0), new Tile(2531, 3479, 0), new Tile(2534, 3476, 0), new Tile(2538, 3473, 0), new Tile(2541, 3474, 0), new Tile(2544, 3476, 0), new Tile(2548, 3475, 0), new Tile(2553, 3472, 0), new Tile(2554, 3467, 0), new Tile(2552, 3462, 0), new Tile(2549, 3458, 0), new Tile(2546, 3454, 0), new Tile(2542, 3451, 0), new Tile(2538, 3448, 0), new Tile(2536, 3443, 0), new Tile(2537, 3438, 0), new Tile(2537, 3433, 0), new Tile(2537, 3428, 0), new Tile(2537, 3423, 0), new Tile(2537, 3418, 0), new Tile(2537, 3413, 0), new Tile(2540, 3409, 0), new Tile(2542, 3404, 0), new Tile(2539, 3400, 0), new Tile(2534, 3400, 0), new Tile(2529, 3400, 0), new Tile(2524, 3399, 0), new Tile(2519, 3400, 0), new Tile(2514, 3398, 0), new Tile(2510, 3395, 0), new Tile(2508, 3390, 0), new Tile(2504, 3387, 0), new Tile(2499, 3386, 0), new Tile(2494, 3387, 0), new Tile(2489, 3388, 0), new Tile(2484, 3388, 0), new Tile(2482, 3388, 0), new Tile(2474, 3388, 0), new Tile(2470, 3387, 0), new Tile(2467, 3384, 0), new Tile(2462, 3381, 0), new Tile(2459, 3381, 0)};
    final Tile[] pathTwo = new Tile[] {new Tile(2461, 3387, 0), new Tile(2459, 3392, 0), new Tile(2458, 3397, 0), new Tile(2459, 3402, 0), new Tile(2461, 3407, 0), new Tile(2461, 3412, 0), new Tile(2458, 3416, 0), new Tile(2461, 3421, 0), new Tile(2462, 3426, 0), new Tile(2462, 3431, 0), new Tile(2466, 3434, 0), new Tile(2471, 3436, 0), new Tile(2474, 3437, 0)};
    final String[] gamesNecks = {"Games necklace(1)", "Games necklace(2)", "Games necklace(3)", "Games necklace(4)", "Games necklace(5)", "Games necklace(6)", "Games necklace(7)", "Games necklace(8)"};

    private void handleFood() {
        Item food = Inventory.getItemClosestToMouse("Tuna");
        if (Inventory.isOpen()) {
            if (food != null) {
                final int currentHP = Skills.getCurrentLevel(Skills.Skill.HITPOINTS);
                if (food.click()) {
                    Time.sleepUntil(() -> Skills.getCurrentLevel(Skills.Skill.HITPOINTS) > currentHP, 1200);
                }
            }
        } else Inventory.openTab();

    }

    private boolean onPath() {
        for(Tile t : pathToCourse) {
            if(t.isOnMiniMap() && t.distance() <= 6) {
                return true;
            }
        }
        return false;
    }

    private boolean onPathTwo() {
        for(Tile t : pathTwo) {
            if(t.isOnMiniMap() && t.distance() <= 4) {
                return true;
            }
        }
        return false;
    }

    private boolean isPathTwoOnMM() {
        for(Tile t : pathTwo) {
            if(t.isOnMiniMap()) {
                return true;
            }
        }
        return false;
    }

    private boolean inArea(Area area) {
        return area.contains(Players.getLocal().getLocation());
    }

    private void walkToGnome() {
        if(!onPath() && !onPathTwo()) {
            if (Inventory.containsOneOf(gamesNecks)) {
                WidgetChild barbOutPost = Widgets.getWidgetByTextIncludingGrandChildren("Barbarian Outpost.");
                if (Bank.isOpen()) if (Bank.close()) Time.sleepUntil(() -> !Bank.isOpen(), 1200);
                for (String s : gamesNecks) {
                    Item neck = Inventory.getItemClosestToMouse(s);
                    if (neck != null && !Bank.isOpen() && barbOutPost == null) {
                        if (neck.interact("Rub")) {
                            Time.sleepUntil(() -> barbOutPost != null && barbOutPost.isVisible(), 1200);
                            break;
                        }
                    }
                    if (barbOutPost != null && barbOutPost.isVisible()) {
                        if (barbOutPost.click(true)) {
                            Time.sleep(Random.nextInt(2400,3200));
                            break;
                        }
                    }
                }
            } else if (Bank.isOpen()) {
                for (String s : gamesNecks) {
                    if (Inventory.containsOneOf(gamesNecks)) {
                        break;
                    }
                    if (Bank.contains(s) && !Inventory.containsOneOf(gamesNecks)) {
                        if (Bank.withdraw(s, 1)) {
                            Time.sleepUntil(() -> Inventory.containsOneOf(gamesNecks), 1200);
                            break;
                        }
                    }
                }
            } else if (Bank.openNearestBank())
                LogHandler.log("Going to bank.");
            Time.sleepUntil(Bank::isOpen, 1200);


        }

        if(onPath() && !onPathTwo()) {
            util.walkPath(false, pathToCourse);
            Time.sleepUntil(() -> Walking.getDestination() != null, 1200);
        }

        if(isPathTwoOnMM() && !onPathTwo()) {
            GameObject gate = GameObjects.getNearest("Gate");
            if (!NPCChat.isChatOpen()) {
                util.interact(gate, "Open");
                Time.sleepUntil(NPCChat::isChatOpen, 1200);
            } else {
                if(NPCChat.canContinue()) {
                    NPCChat.clickContinue();
                } else {
                    org.tbot.methods.input.keyboard.Keyboard.sendKey('2');
                }
            }
        }
        else if(onPathTwo()) {
            if(Inventory.containsOneOf(gamesNecks)) {
                for(String s : gamesNecks) {
                    Item neck = Inventory.getItemClosestToMouse(s);
                    if(neck !=null) {
                        neck.interact("Wear");
                        Time.sleepUntil(() -> !Inventory.containsOneOf(gamesNecks), 1200);
                    }
                }
            }
            util.walkPath(false, pathTwo);
            Time.sleepUntil(() -> Walking.getDestination() != null, 1200);
        }
        if(new Tile(2472,3436,0).isOnMiniMap()) {
            walkedToGnome = true;
        }
    }

    private final MouseTrail mt = new MouseTrail();

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.ORANGE);
        graphics.drawString("Time running: " + tracker.getFormattedTimeTracking(), 8, 15);
        graphics.drawString("XP/H: " + tracker.getExperiencePerHour(), 8, 30);
        graphics.drawString("Marks Looted: " + marksLooted, 8, 45);
        graphics.drawString("Time to level: " + tracker.getTimeToLevel(), 8, 60);
    }

    @Override
    public void itemsAdded(InventoryEvent inventoryEvent) {
        if(inventoryEvent.getItem().getName().equals("Mark of grace")) {
            marksLooted++;
        }
    }

    @Override
    public void itemsRemoved(InventoryEvent inventoryEvent) {

    }
}

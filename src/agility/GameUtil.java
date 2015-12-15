package agility;

import org.tbot.bot.TBot;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.Menu;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Filter;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.GroundItem;
import org.tbot.wrappers.Locatable;
import org.tbot.wrappers.Tile;

import java.awt.*;

/**
 * Created by Jonross on 6/3/2015.
 */
public class GameUtil {

    public final GroundItem markGrace() {
        return GroundItems.getNearest(groundItem -> Walking.canReach(groundItem.getLocation()) && groundItem.getName().equals("Mark of grace") ||
                groundItem.getName().equals("Mark of grace") && groundItem.distance() <= 1);
    }


    public boolean needToEat() {
        return (int) (((double) Skills.getCurrentLevel(Skills.Skill.HITPOINTS) / (double) Skills
                .getRealLevel(Skills.Skill.HITPOINTS)) * 100) < 60;
    }


    public boolean walkPath(boolean reverse, Tile... tiles) {
        int endIndex = reverse ? 0 : tiles.length - 1;
        if (tiles[endIndex].distance() < 4) {
            return true;
        } else {
            int startIndex = reverse ? tiles.length - 1 : 0;
            for (int i = endIndex; i != startIndex; i += (endIndex > startIndex ? -1 : 1)) {
                if (tiles[i].isOnMiniMap()) {
                    Tile destination = Walking.getDestination();
                    if (!Walking.isMoving() || destination == null || destination.distance(tiles[i]) > 3) {
                        Walking.walkTileMM(tiles[i]);
                    }
                    break;
                }
            }
        }
        return false;
    }

    public Tile getNearestTileTo(Locatable source, Locatable destination, Filter<Tile> filter) {
        int[][] groundData = TBot.getBot().getClient().getGroundData()[source.getLocation().getPlane()].getFlags();
        Tile closest = destination.getLocation();
        int distance = Integer.MAX_VALUE;

        int sourceX = source.getLocation().getX(), sourceY = source.getLocation().getY();
        if (!closest.isOnMiniMap()) {
            for (int x = -18; x < 18; x++) {
                for (int y = -18; y < 18; y++) {
                    if (groundData[sourceX + x - Game.getBaseX()][sourceY + y - Game.getBaseY()] == 0) {
                        Tile tile = new Tile(sourceX + x, sourceY + y);
                        if (filter != null && !filter.accept(tile))
                            continue;
                        int dist = tile.distance(source);
                        if (dist <= distance) {
                            closest = tile;
                            distance = dist;
                        }
                    }
                }
            }
        }
        return closest;
    }

    private void sleepDistance() {
        if(Walking.getDestinationDistance() > 4 && Players.getLocal().isMoving()) {
            Time.sleepUntil(() -> Walking.getDestinationDistance() <= 4, Random.nextInt(800,1650));
        }
    }

    public boolean handleWall(Tile tileArg, String objName, String action) {
        final Tile tile = tileArg;
        final GameObject wall = GameObjects.getNearest(object -> object.getName().equals(objName) && object.getLocation().equals(tile));
            if (wall != null) {
                if (wall.isOnScreen() && wall.distance() <= 4) {
                    if (wall.interact(action)) {
                        if (Mouse.getClickState() == Mouse.CURSOR_STATE_RED) {
                            Time.sleepUntil(() -> Players.getLocal().isMoving(), 1200);
                            if (!Players.getLocal().isMoving()) {
                                Time.sleepUntil(() -> Players.getLocal().isMoving(), Random.nextInt(800, 1200));
                            } else {
                                Time.sleepUntil(() -> Game.getPlane() != 0, Random.nextInt(2200, 2650));
                            }
                        }
                    }
                } else if (wall.distance() <= 4) {
                        Camera.turnTo(wall);
                    } else {
                        Path path = Walking.findPath(tile);
                        if (path != null) {
                            if (path.traverse()) {
                                Time.sleep(100, 200);
                                if (Walking.getDestinationDistance() > 3 && Players.getLocal().isMoving()) {
                                    Time.sleepUntil(() -> Walking.getDestinationDistance() <= 3, Random.nextInt(800, 1200));
                                }
                            }
                        }

                    }
            }
            if (wall == null) {
                Path path = Walking.findPath(tile);
                if (path != null) {
                    if (path.traverse()) {
                        Time.sleep(100, 200);
                        if (Walking.getDestinationDistance() > 3 && Players.getLocal().isMoving()) {
                            Time.sleepUntil(() -> Walking.getDestinationDistance() <= 3, Random.nextInt(800, 1200));
                        }
                    }

                }
            }
        return true;
    }


    public boolean setRun() {
        if(!Walking.isRunEnabled() && Walking.getRunEnergy() >= Random.nextInt(34,52)) {
           return Walking.setRun(true);
        }
        return false;
    }

    public boolean interact(final GameObject object, final String action) {
        if(object !=null) {
            if(object.isOnScreen()) {
                if(object.getModel() !=null && object.getModelLocation() !=null && object.getModelLocation().getBounds() !=null) {
                    int x = (int) object.getModelLocation().getBounds().getBounds().getCenterX();
                    int y = (int) object.getModelLocation().getBounds().getBounds().getCenterY();
                    Mouse.setLocation(x + Random.nextInt(2,6),y - Random.nextInt(3,9));
                    if(Menu.getUpText().contains(action + " " + object.getName())) {
                        Mouse.click(true);
                        Time.sleep(50, 75);
                        if (Mouse.getClickState() == Mouse.CURSOR_STATE_RED)
                            Time.sleepUntil(() -> Players.getLocal().isMoving(), Random.nextInt(1650, 2100));

                    } else if (Mouse.click(x - Random.nextInt(2,12),y + Random.nextInt(3,15), true)) {
                        Time.sleep(40, 75);
                        if (Mouse.getClickState() == Mouse.CURSOR_STATE_RED) {
                            Time.sleepUntil(() -> Players.getLocal().isMoving(), Random.nextInt(1650, 2100));
                        } else {
                            Mouse.click(x,y, true);
                            if (Mouse.getClickState() == Mouse.CURSOR_STATE_RED) {
                                Time.sleepUntil(() -> Players.getLocal().isMoving(), Random.nextInt(1650, 2100));
                            } else Camera.turnTo(object);

                        }
                    }
                } else if(Walking.walkTileMM(getNearestTileTo(Players.getLocal(), object.getLocation(), null))) {
                    setRun();
                    Time.sleepUntil(() -> Players.getLocal().isMoving(), 1200);
                }
                sleepDistance();
            } else if(object.distance() <= 4) {
                MouseCam.setCamera(Camera.getYaw() - Random.nextInt(60,100), Camera.getPitch() + Random.nextInt(40,60));
            } else {
                if(getNearestTileTo(Players.getLocal(), object.getModelLocation(), null).isOnMiniMap()) {
                   Walking.walkTileMM(getNearestTileTo(Players.getLocal(), object.getLocation(), null));
                    setRun();
                    Time.sleepUntil(() -> Players.getLocal().isMoving(), 1200);
                    sleepDistance();
                } else {
                    Path path = Walking.findLocalPath(getNearestTileTo(Players.getLocal(), object.getLocation(), null));
                    if(path !=null && path.traverse()) {
                        setRun();
                        sleepDistance();
                    }
                }
            }
        }
        return false;
    }

   /* public boolean interact(String obstacleName, Tile[] tiles, String action) {

            if (Menu.getUpText().contains(action + " " + obstacleName)) {
                Mouse.click(true);
                Time.sleep(50, 75);
                if (Mouse.getClickState() == Mouse.CURSOR_STATE_RED)
                    Time.sleepUntil(() -> Players.getLocal().isMoving(), Random.nextInt(1650, 2100));
            } else {
                for (Tile t : tiles) {
                    if (t != null && t.isOnScreen() && t.distance() <= 4) {
                        if(t.getBounds() !=null && t.getBounds().getBounds() !=null) {
                            int x = (int) t.getBounds().getBounds().getCenterX();
                            int y = (int) t.getBounds().getBounds().getCenterY();
                            Point point = new Point(x, y);
                            if (Mouse.move(point) && Menu.getUpText().contains(action + " " + obstacleName)) {
                                break;
                            } else {
                                Mouse.click(true);
                                Time.sleep(30, 45);
                                if (Mouse.getClickState() == Mouse.CURSOR_STATE_RED) {
                                    Time.sleepUntil(() -> Players.getLocal().isMoving(), Random.nextInt(1650, 2100));
                                }
                            }
                        } else {
                            Mouse.click(t.getMinimapPoint(), true);
                            Time.sleepUntil(() -> t.getBounds() != null, 1200);
                        }
                    } else {
                        if (t != null) {
                            if (t.distance() > 4) {
                                Path path = Walking.findLocalPath(getNearestTileTo(Players.getLocal(), t, null));
                                if ((t.isOnMiniMap()) ? Walking.walkTileMM(getNearestTileTo(Players.getLocal(), t, null)) : (path != null && path.traverse())) {
                                    sleepDistance();
                                }
                            } else {
                              Camera.tiltRandomly();
                            }
                        }
                    }
                }
            }
        return true;
    } */


}

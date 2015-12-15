package agility;


import org.tbot.methods.GameObjects;
import org.tbot.methods.walking.Walking;
import org.tbot.wrappers.Area;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Tile;

/**
 * Created by Mad on 5/15/2015.
 */
public class Obstacles {

    public static class Gnome {

        public static final Area startArea = new Area(new Tile[]{new Tile(2489, 3435, 0), new Tile(2487, 3439, 0), new Tile(2472, 3439, 0), new Tile(2469, 3436, 0), new Tile(2469, 3434, 0), new Tile(2478, 3433, 0), new Tile(2482, 3433, 0)});

        public static final Tile courseStart = new Tile(2474, 3436, 0);
        public static final Tile logBalanceEnd = new Tile(2474, 3429, 0);

        public static GameObject logBalance() {
            return GameObjects.getNearest("Log balance");
        }
        //Walk-across

        public static GameObject obstacleNetOne() {
            return GameObjects.getNearest("Obstacle net");
        }
        //Climb-over

        public static GameObject treeBranchOne() {
            return GameObjects.getNearest("Tree branch");
        }
        //Climb

        public static GameObject balanceRope() {
            return GameObjects.getNearest("Balancing rope");
        }

        public static GameObject treeBranchTwo() {
            return GameObjects.getNearest("Tree branch");
        }
        //climb down

        public static final Tile obstacleNetTwoTile = new Tile(2485, 3425, 0);
        //climb-over

        public static final Tile obstaclePipeTile = new Tile(2486, 3427, 0);

        public static GameObject obstaclePipe() {
            return GameObjects.getNearest("Obstacle pipe");
        }

        public static final Tile obstaclePipeEndTile = new Tile(2485, 3438, 0);
        //Squeeze-through

        public static final GameObject[] obstacles = {logBalance(), obstacleNetOne(), treeBranchOne(), balanceRope(), treeBranchTwo(), obstaclePipe()};
    }


    public static class Draynor {
        
        public static final Tile roughWallTile = new Tile(3103, 3279, 0);
        public static final Tile firstRopeTile = new Tile(3099, 3277, 3);
        public static final Tile secondRopeTile = new Tile(3090, 3275, 3);
        public static final Tile narrowWallTile = new Tile(3091, 3265, 3);
        public static final Tile bigWallTile = new Tile(3088, 3257, 3);
        public static final Tile jumpGapTile = new Tile(3094, 3255, 3);
        public static final Tile climbCrateTile = new Tile(3100, 3260, 3);
  
    }

    public static class Ally {

        public static final Tile firstRopeTile = new Tile(3273, 3183, 3);
        public static final Tile cableTile = new Tile(3269, 3169, 3);
        public static final Tile teethLineTile = new Tile(3301, 3163, 3);
        public static final Tile treeTile = new Tile(3317, 3164, 1);
        public static final Tile beamsTile = new Tile(3317, 3174, 2);
        public static Tile secondRopeTile = new Tile(3314, 3186, 3);
        public static Tile jumpGapTile = new Tile(3300, 3191, 3);


    }

    public static class Varrock {

        public static final Tile wallTile = new Tile(3221, 3414, 0);
        public static final Tile firstClothesLineTile = new Tile(3214, 3412, 3); //Cross
        public static final Tile firstGapTile = new Tile(3202, 3416, 3); //Leap
        public static final Tile balanceWallTile = new Tile(3194, 3416, 1); //Balance
        public static final Tile secondGapTile = new Tile(3197, 3403, 3); //Leap
        public static final Tile thirdGapTile = new Tile(3208, 3396, 3); //Leap
        public static final Tile fourthGapTile = new Tile(3230, 3402, 3); //Leap
        public static final Tile ledgeTile = new Tile(3237, 3408, 3); //Hurdle
        public static final Tile lastEdgeTile = new Tile(3236, 3413, 3); //Jump-off


    }

    public static class Falador {

        public static final Tile roughWallTile = new Tile(3036, 3341, 0);

        public static final Tile firstRopeTile = new Tile(3037, 3343, 3); //cross
        public static final Tile handHoldsTile = new Tile(3049, 3349, 3); //cross
        public static final Tile firstGapTile = new Tile(3049, 3357, 3); //Jump
        public static final Tile secondGapTile = new Tile(3046, 3363, 3); //Jump
        public static final Tile secondRopeTile = new Tile(3036, 3362, 3); //Cross
        public static final Tile thirdRopeTile = new Tile(3028, 3353, 3);
        public static final Tile thirdGapTile = new Tile(3018, 3354, 3); //Jump
        public static final Tile firsLedgeTile = new Tile(3017, 3348, 3); //Jump
        public static final Tile secondLedgeTile = new Tile(3013, 3346, 3);
        public static final Tile thirdLedgeTile = new Tile(3012, 3339, 3);
        public static final Tile fourthLedgeTile = new Tile(3015, 3332, 3);
        public static final Tile fifthLedgeTile = new Tile(3021, 3332, 3);

    }

    public static class Seers {
        public static final Tile wallTile = new Tile(2729, 3489, 0);
        public static final Tile firstGapTile = new Tile(2724, 3493, 3); //Jump
        public static final Tile firstRopeTile = new Tile(2709, 3490, 2); //Cross
        public static final Tile secondGapTile = new Tile(2713, 3480, 2); //Jump
        public static final Tile thirdGapTile = new Tile(2702, 3472, 3);
        public static final Tile edgeTile = new Tile(2700, 3463, 2); //Jump

    }

    public static class Relleka {

        public static final Tile wallTile = new Tile(2625, 3677, 0);
        public static final Tile firstGapTile = new Tile(2622, 3674, 3); //jump
        public static final Tile firstTightRopeTile = new Tile(2621, 3659, 3); //cross
        public static final Tile secondGapTile = new Tile(2627, 3654, 3); //Leap
        public static final Tile thirdGapTile = new Tile(2640, 3651, 3);//Hurdle
        public static final Tile secondTightRopeTile = new Tile(2648, 3662, 3); //Cross
        public static final Tile pileOfFishTile = new Tile(2656, 3677, 3); //Jump-in
        
    }

    public static class Ardy {

        public static final Tile wallTile = new Tile(2673, 3298, 0);
        public static final Tile firstGap = new Tile(2671, 3309, 3);
        public static final Tile plank = new Tile(2662, 3318, 3);
        public static final Tile secondGap = new Tile(2654, 3318, 3);
        public static final Tile thirdGap = new Tile(2653, 3310, 3);
        public static final Tile steepRoof = new Tile(2653, 3301, 3);
        public static final Tile fourthGap = new Tile(2656, 3297, 3);


    }
}

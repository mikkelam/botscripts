package util;
import org.tbot.methods.Skills;
import org.tbot.methods.Skills.Skill;
import org.tbot.wrappers.Timer;

/**
 * Created by Hexagon on 5/22/2015.
 */
public class SkillTracker {

    private final Skill skill;
    private final Timer startTimer;
    private final int startLevel;
    private final int startExperience;

    /**
     * Starts the skill tracker.
     *
     * @param skill
     *            The skill to be tracked.
     */

    public SkillTracker(Skill skill) {
        this.skill = skill;
        this.startLevel = Skills.getRealLevel(skill);
        this.startExperience = Skills.getExperience(skill);
        this.startTimer = new Timer(0);
    }

    /**
     *
     * Gets the skill being tracked.
     *
     * @return The <code>Skill</code> being tracked.
     */

    public Skill getTrackedSkill() {
        return skill;
    }

    /**
     * Gets the total time tracking since the tracker was initialized.
     *
     * @return The total time tracked as a <code>long</code>.
     */

    public long getTimeTracking() {
        return startTimer.getElapsed();
    }

    /**
     * Gets the formatted total time tracking since the tracker was initialized.
     *
     * @return The total time tracked as a <code>String</code> formatted in HH:MM:SS.
     */

    public String getFormattedTimeTracking() {
        return Timer.format(getTimeTracking());
    }

    /**
     * Gets the remaining experience to the next level.
     *
     * @return The experience amount to level as an {int}.
     */

    public int getExperienceToLevel() {
        return getExperienceToLevel(Skills.getRealLevel(skill) + 1);
    }

    /**
     * Gets the aproximate time to the next level.
     *
     * @return The aproximate remaining time to the next level as a <code>String</code> formatted in HH:MM:SS.
     */

    public String getTimeToLevel() {
        if (getExperiencePerHour() > 0)
            return Timer.format((long) ((getExperienceToLevel() * 3600000.0) / getExperiencePerHour()));
        else return "Unknown";
    }

    /**
     * Gets the current tracked skill level.
     *
     * @return The current level as an <code>int</code>.
     */

    public int getCurrentLevel() {
        return Skills.getRealLevel(skill);
    }

    /**
     * Gets the amount of levels gained since the tracker was initialized.
     *
     * @return The amount of levels gained as an <code>int</code>.
     */

    public int getLevelsGained() {
        return getCurrentLevel() - startLevel;
    }

    /**
     * Gets the amount of experience gained since the tracker was initialized.
     *
     * @return The amount of experience gained as an <code>int</code>.
     */

    public int getExperienceGained() {
        return Skills.getExperience(skill) - startExperience;
    }

    /**
     * Gets the aproximate hourly experience gain on the skill.
     *
     * @return The aproximate hourly experience gain as an <code>int</code>.
     */

    public int getExperiencePerHour() {
        return (int) (getExperienceGained() * 3600000.0D / getTimeTracking());
    }

    /**
     * Gets the amount of experience to an specific level.
     *
     * @param level
     *            - The level to achieve.
     * @return The amount of experience to the level as an <code>int</code>.
     */

    public int getExperienceToLevel(int level) {
        if (level > 99) {
            return 0;
        }

        int experience = Skills.getExperience(skill);
        return Skills.experienceAtLevel(level) - experience;
    }

    /**
     * Gets the percentage to the next level.
     *
     * @return The percentage to the next level as an <code>int>/code>.
     */

    public int getPercentToNextLevel() {
        int current = getCurrentLevel();
        int experienceAtCurrent = Skills.experienceAtLevel(getCurrentLevel());

        if (getCurrentLevel() == 99) {
            return 100;
        }

        return 100 * (Skills.getExperience(skill) - experienceAtCurrent / (Skills.experienceAtLevel(current + 1) - experienceAtCurrent));
    }

}

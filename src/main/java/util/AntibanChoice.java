package util;

/**
 * Return values for the antiban class to let callers know what decision the
 * antiban class took.
 */
public enum AntibanChoice {
    OPEN_RANDOM_TAB("Opened a random tab"),
    CAMERA_TILT("Tilted the camera randomly"),
    NO_CHOICE("Didn't make any random choice"),
    MOUSE_MOVE("Move the mouse somewhere")
    ;

    private final String description;

    AntibanChoice(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}

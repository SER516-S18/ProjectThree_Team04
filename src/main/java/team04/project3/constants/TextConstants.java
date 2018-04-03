package team04.project3.constants;

import java.awt.*;

/**
 * Class containing program-wide constant Fonts and Strings
 * @author David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-22 
 */

public class TextConstants {
    public static final Font LARGE_FONT = new Font("Monospaced", Font.PLAIN, 20);
    public static final Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 16);
    public static final Font SMALL_FONT = new Font("Monospaced", Font.PLAIN, 13);

    public static final String HIGHEST_VALUE_STRING = "<html>Highest<br>value:</html>";
    public static final String LOWEST_VALUE_STRING = "<html>Lowest<br>value:</html>";
    public static final String AVERAGE_VALUE_STRING = "<html>Average:</html>";
    public static final String CHANNELS_VALUE_STRING = "<html>Channels:</html>";
    public static final String FREQUENCY_VALUE_STRING = "<html>Frequency<br>(Hz):</html>";

    public static final String EMO_STATE_INTERVAL_STRING = "<html>EmoState<br>Interval (Sec):</html>";

    public static final String CLIENT_TITLE_VALUE = "EmoCompose";
    public static final String SERVER_TITLE_VALUE = "Emotiv Xavier Control Panel";

    public static final String UPPER_FACE = "Upperface";
    public static final String DOWN_FACE = "Lowerface";
    public static final String EYE = "Eye";
    public static final String PERFORMANCE_METRICS = "Performance Metrics";
}
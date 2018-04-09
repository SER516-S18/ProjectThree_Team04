package team04.project3.util;

/**
 * A utilities class for misc. functions
 * @author  David Henderson (dchende2@asu.edu)
 */

public class Util {
    public static final int DEFAULT_PORT = 1726;

    /**
     * Returns if a String s is a valid integer
     * @param s String to check if is integer
     * @return boolean Whether String is an integer
     */
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    /**
     * See @isInteger
     * @param s String to check if is integer
     * @param radix Radix to parse by
     * @return boolean Whether string is integer
     */
    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}

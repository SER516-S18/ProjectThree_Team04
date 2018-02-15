package edu.asu.ser516.projecttwo.team04.util;

/**
 * Util, a utilities class for misc. functions
 */

public class Util {
    // For Log without a class (namely uncaught exceptions)
    public static class ERROR {
        private ERROR() {}
    }

    /**
     * isInteger, returns if a String s is a valid integer
     * @param s String to check if is integer
     * @return boolean
     */
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    /**
     * See @isInteger
     * @param s
     * @param radix
     * @return boolean
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

package edu.ccrm.util;

public final class Validators {
    private Validators() {}
    public static boolean isEmail(String s){ return s != null && s.contains("@"); }
}

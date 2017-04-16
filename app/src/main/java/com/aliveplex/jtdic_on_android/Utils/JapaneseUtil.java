package com.aliveplex.jtdic_on_android.Utils;

import android.util.Log;

import java.lang.Character;

/**
 * Created by Aliveplex on 8/4/2560.
 */

public class JapaneseUtil {
    public static boolean isJapanese(String keyword) {
        if (keyword.length() == 0) {
            return false;
        }
        for (char c : keyword.toCharArray()) {
            Character.UnicodeBlock unicode = Character.UnicodeBlock.of(c);
            if (unicode.equals(Character.UnicodeBlock.HIRAGANA) || unicode.equals(Character.UnicodeBlock.KATAKANA) || unicode.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)) {
                continue;
            }
            else {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllKana(String keyword) {
        if (keyword.length() == 0) {
            return false;
        }
        for (char c : keyword.toCharArray()) {
            Character.UnicodeBlock unicode = Character.UnicodeBlock.of(c);
            if (unicode.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllKanji(String keyword) {
        if (keyword.length() == 0) {
            return false;
        }
        for (char c : keyword.toCharArray()) {
            Character.UnicodeBlock unicode = Character.UnicodeBlock.of(c);
            if (unicode.equals(Character.UnicodeBlock.HIRAGANA) || unicode.equals(Character.UnicodeBlock.KATAKANA)) {
                return false;
            }
        }

        return true;
    }
}

package com.example.aliveplex.jtdic_on_android.Models;

/**
 * Created by Aliveplex on 6/4/2560.
 */

public class JTDicWord {
    private int id;
    private String kanji;
    private String kana;
    private String type;
    private String meaning;

    public JTDicWord(int id, String kanji, String kana, String meaning, String type) {
        this.id = id;
        this.kanji = kanji;
        this.kana = kana;
        this.type = type;
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getType() {
        return type;
    }

    public String getKana() {
        return kana;
    }

    public String getKanji() {
        return kanji;
    }

    public int getId() {
        return id;
    }
}

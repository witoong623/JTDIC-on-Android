package com.aliveplex.jtdic_on_android;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.aliveplex.jtdic_on_android.Database.JTDicDb;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordDetailActivity extends AppCompatActivity {
    @BindView(R.id.kanji_header_detail)TextView kanji_detail;
    @BindView(R.id.kana_detail)TextView kana_detail;
    @BindView(R.id.meaning_detail)TextView meaning_detail;
    @BindView(R.id.type_detail)TextView type_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JTDicDb jtdicDb = new JTDicDb(getApplicationContext());
        SQLiteDatabase db = jtdicDb.getReadableDatabase();
        Intent startIntent = getIntent();
        String wordId = Integer.toString(startIntent.getIntExtra(Constant.JTDICWord_ID, 0));

        Cursor c = db.rawQuery("select * from jtdic where id = ?", new String[]{ wordId });

        while (c.moveToNext()) {
            String kanji = c.getString(1);
            String kana = c.getString(2);
            String meaning = c.getString(3);
            String type = c.getString(4);

            kanji_detail.setText(kanji);
            kana_detail.setText(kana);
            meaning_detail.setText(meaning);
            type_detail.setText(type);
        }

        c.close();
        db.close();
    }
}

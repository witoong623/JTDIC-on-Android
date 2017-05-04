package com.aliveplex.jtdic_on_android;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.aliveplex.jtdic_on_android.Adapters.WordTagWordDetailAdapter;
import com.aliveplex.jtdic_on_android.Database.AppDb;
import com.aliveplex.jtdic_on_android.Models.JTDicWord;
import com.aliveplex.jtdic_on_android.Models.Tag;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordDetailActivity extends AppCompatActivity {
    @BindView(R.id.kanji_header_detail)TextView kanji_detail;
    @BindView(R.id.kana_detail)TextView kana_detail;
    @BindView(R.id.meaning_detail)TextView meaning_detail;
    @BindView(R.id.type_detail)TextView type_detail;
    @BindView(R.id.tag_dropdown)Spinner tagSpinner;
    @BindView(R.id.wordtag_list)RecyclerView wordtag_recycler;
    @BindView(R.id.add_tag_to_word_but)Button addTagToWord;
    private JTDicWord currentJtdicWord;
    private List<Tag> currentAvailableTag;
    private WordTagWordDetailAdapter wordTagadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent startIntent = getIntent();
        int wordId = startIntent.getIntExtra(Constant.JTDICWord_ID, 0);
        String kanji = startIntent.getStringExtra(Constant.JTDICWord_KANJI);
        String kana = startIntent.getStringExtra(Constant.JTDICWord_KANA);
        String meaning = startIntent.getStringExtra(Constant.JTDICWord_MEANING);
        String type = startIntent.getStringExtra(Constant.JTDICWord_TYPE);
        currentJtdicWord = new JTDicWord(wordId, kanji, kana, meaning, type);

        kanji_detail.setText(kanji);
        kana_detail.setText(kana);
        meaning_detail.setText(meaning);
        type_detail.setText(type);
        // populate spinner of tag
        LoadDataAsync loadDataAsync = new LoadDataAsync();
        loadDataAsync.execute((Void)null);

        // This RecyclerView holds Tags that were added to this word
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        wordtag_recycler.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(wordtag_recycler.getContext(), DividerItemDecoration.VERTICAL);
        wordtag_recycler.addItemDecoration(mDividerItemDecoration);
        wordTagadapter = new WordTagWordDetailAdapter(new ArrayList<Tag>());
        wordtag_recycler.setAdapter(wordTagadapter);
        addTagToWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = tagSpinner.getSelectedItem().toString();
                AddTagToWordTask addTagToWordTask = new AddTagToWordTask();
                addTagToWordTask.execute(tagName);
            }
        });
    }

    private class LoadDataAsync extends AsyncTask<Void, Void, LoadDataAsync.LoadedItems> {

        @Override
        protected LoadDataAsync.LoadedItems doInBackground(Void... params) {
            List<Tag> availableTagList = new ArrayList<>();
            List<Tag> tagOfWordList = new ArrayList<>();

            try {
                AppDb appDb = new AppDb(WordDetailActivity.this);
                SQLiteDatabase db = appDb.getReadableDatabase();
                // Available tag that can be added to this word
                Cursor c = db.rawQuery("select * from tags", null);

                while (c.moveToNext()) {
                    Tag tag = new Tag(c.getString(1), c.getString(2));
                    tag.setId(c.getInt(0));
                    availableTagList.add(tag);
                }

                c.close();

                // Tags that were added to this word
                c = db.rawQuery("select tag_text from wordtags inner join tags on tagId = _id where wordId = ?", new String[] { Integer.toString(currentJtdicWord.getId()) });

                while (c.moveToNext()) {
                    tagOfWordList.add(new Tag(c.getString(0), null));
                }

                c.close();
                db.close();
            } catch (Exception e) {
                Log.d("WordDetailActivity", "Exception: " + e.getMessage());
            }

            return new LoadedItems(availableTagList, tagOfWordList);
        }

        @Override
        protected void onPostExecute(LoadedItems loadedItems) {
            // Populate available tag to spinner
            currentAvailableTag = loadedItems.getAvailableTag();

            List<String> strings = new ArrayList<>(loadedItems.getAvailableTag().size());

            for (Tag tag : loadedItems.getAvailableTag()) {
                strings.add(tag.toString());
            }

            ArrayAdapter<String> tagArrayAdapter = new ArrayAdapter<>(WordDetailActivity.this, android.R.layout.simple_spinner_item, strings);
            tagArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tagSpinner.setAdapter(tagArrayAdapter);

            // Populate RecyclerView in which tags were added
            wordTagadapter.getTagList().addAll(loadedItems.getTagOfWord());
            wordTagadapter.notifyDataSetChanged();
        }

        public class LoadedItems {
            public List<Tag> getAvailableTag() {
                return availableTag;
            }

            public List<Tag> getTagOfWord() {
                return tagOfWord;
            }

            private List<Tag> availableTag;
            private List<Tag> tagOfWord;


            public LoadedItems(List<Tag> availableTag, List<Tag> tagOfWord) {
                this.availableTag = availableTag;
                this.tagOfWord = tagOfWord;
            }
        }
    }

    private class AddTagToWordTask extends AsyncTask<String, Void, Boolean> {
        private int tagId;

        @Override
        protected Boolean doInBackground(String... params) {
            String tagName = params[0];
            // Find id of tag from list first
            tagId = 0;

            for (Tag tag : currentAvailableTag) {
                if (tag.getTagName().equals(tagName)) {
                    tagId = tag.getId();
                    break;
                }
            }

            // For some reason we can't find tag id from tag name
            if (tagId == 0) {
                return false;
            }

            try {
                AppDb appdb = new AppDb(WordDetailActivity.this);
                SQLiteDatabase db = appdb.getWritableDatabase();

                // check if this tag was added to this word before
                Cursor c = db.rawQuery("select * from wordtags where wordId = ? and tagId = ?", new String[] { Integer.toString(currentJtdicWord.getId()), Integer.toString(tagId) });
                if (c.getCount() > 0) {
                    c.close();
                    db.close();

                    return false;
                }

                ContentValues row = new ContentValues();
                row.put("wordId", currentJtdicWord.getId());
                row.put("tagId", tagId);
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                row.put("added_date", fmt.print(new DateTime()));
                long numRow = db.insertOrThrow(AppDb.WordTagEntity.TABLE_NAME, null, row);

                if (numRow == 0) {
                    return false;
                }

                c.close();
                db.close();

                return true;
            } catch (Exception e) {
                Log.d("WordDetailActivity", "Exception: " + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (!aBoolean) {
                return;
            }

            // Get latest tag that was add to this word by search array of available tag
            Tag newTag = null;

            for (Tag tag : currentAvailableTag) {
                if (tag.getId() == tagId) {
                    newTag = tag;
                    break;
                }
            }

            wordTagadapter.getTagList().add(newTag);
            wordTagadapter.notifyDataSetChanged();
        }
    }
}

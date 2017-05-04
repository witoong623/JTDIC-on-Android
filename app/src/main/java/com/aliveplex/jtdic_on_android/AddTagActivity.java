package com.aliveplex.jtdic_on_android;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.aliveplex.jtdic_on_android.Database.AppDb;
import com.aliveplex.jtdic_on_android.Models.Tag;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTagActivity extends AppCompatActivity {
    public static final String TAG_ID = "tag_id";
    public static final String TAG_NAME = "tag_name";
    public static final String TAG_DESCRIPTION = "tag_description";

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.tag_name_et)TextInputEditText tagNameEt;
    @BindView(R.id.tag_description_et)TextInputEditText tagDescriptionEt;
    @BindView(R.id.activity_add_tag_root)CoordinatorLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("เพิ่มแท็ก");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_tag_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_tag:
                hideKeyboard();
                addNewTag();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNewTag() {
        String tagName = tagNameEt.getText().toString();
        String tagDescription = tagDescriptionEt.getText().toString();
        //Snackbar.make(rootLayout, "Name:" + tagName + " Description:" + tagDescription, Snackbar.LENGTH_LONG).show();
        AddTagTask addTask = new AddTagTask(this);
        addTask.execute(new Tag(tagName, tagDescription));
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private static class AddTagTask extends AsyncTask<Tag, Void, AddTagTask.AddNewTagMessage> {
        private AddTagActivity addTagActivity;

        public AddTagTask(AddTagActivity addTagActivity) {
            this.addTagActivity = addTagActivity;
        }

        @Override
        protected AddNewTagMessage doInBackground(Tag... params) {
            Tag tag = params[0];
            AppDb appdb = new AppDb(addTagActivity);
            SQLiteDatabase db = appdb.getWritableDatabase();

            try {
                Cursor cursor = db.rawQuery("select * from tags where tag_text = ?", new String[] { tag.getTagName() });
                boolean isExist = cursor.getCount() > 0;
                cursor.close();

                if (isExist) {
                    return new AddNewTagMessage("Tag name: " + tag.getTagName() + " already exist.", false, tag);
                }

                ContentValues row = new ContentValues();
                row.put("tag_text", tag.getTagName());
                row.put("tag_description", tag.getDescription());
                long numRow = db.insertOrThrow("tags", null, row);

                if (numRow == -1) {
                    return new AddNewTagMessage("DB error, can't add new tag.", false, tag);
                }

                tag.setId((int)numRow);
                return new AddNewTagMessage(null, true, tag);

            } catch (Exception e) {
                Log.d("AddTagActivity", "Exception: " + e.getMessage());
                return new AddNewTagMessage(e.getMessage(), false, tag);
            } finally {
                db.close();
            }
        }

        @Override
        protected void onPostExecute(AddNewTagMessage addNewTagMessage) {
            if (!addNewTagMessage.isSuccess()) {
                Snackbar.make(addTagActivity.rootLayout, addNewTagMessage.getMessage(), Snackbar.LENGTH_LONG).show();
                return;
            }

            Intent output = new Intent();
            output.putExtra(TAG_ID, addNewTagMessage.getTag().getId());
            output.putExtra(TAG_NAME, addNewTagMessage.getTag().getTagName());
            output.putExtra(TAG_DESCRIPTION, addNewTagMessage.getTag().getDescription());
            addTagActivity.setResult(RESULT_OK, output);
            addTagActivity.finish();
        }

        public static class AddNewTagMessage {
            public AddNewTagMessage(String message, boolean isSuccess, Tag tag) {
                this.tag = tag;
                this.message = message;
                this.isSuccess = isSuccess;
            }

            public String getMessage() {
                return message;
            }

            public boolean isSuccess() {
                return isSuccess;
            }

            private String message;
            private boolean isSuccess;

            public Tag getTag() {
                return tag;
            }

            private Tag tag;


        }
    }
}

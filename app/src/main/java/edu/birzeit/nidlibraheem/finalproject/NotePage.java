package edu.birzeit.nidlibraheem.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NotePage extends AppCompatActivity {

    boolean isEditing = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);

        ORM orm = ORM.getInstance(this);


        EditText title = (EditText)findViewById(R.id.noteTitle);
        EditText content = (EditText)findViewById(R.id.noteContent);
        EditText tags = (EditText)findViewById(R.id.noteTag);

        TextView createdOn = (TextView)findViewById(R.id.createdOn);

        title.setText(NotesActivity.selectedNote.getTitle());
        content.setText(NotesActivity.selectedNote.getContent());
        tags.setText(NotesActivity.selectedNote.getTags());

        createdOn.setText( ORM.dateFormat.format(NotesActivity.selectedNote.getCreationDate()));

        Button deleteBtn = (Button)findViewById(R.id.deleteBtn);

        deleteBtn.setOnClickListener(v -> {
            //delete note
            orm.deleteNote(
                    NotesActivity.selectedNote);
            NotesActivity.selectedNote = null;
            finish();
        });

        Button editBtn = (Button)findViewById(R.id.editBtn);

        title.setEnabled(false);
        content.setEnabled(false);
        tags.setEnabled(false);

        editBtn.setOnClickListener(v -> {

            if (isEditing){
                isEditing = false;
                editBtn.setText("Edit");
                title.setEnabled(false);
                content.setEnabled(false);
                tags.setEnabled(false);

                NotesActivity.selectedNote.setTitle(title.getText().toString());
                NotesActivity.selectedNote.setContent(content.getText().toString());
                NotesActivity.selectedNote.setTags(tags.getText().toString());

                orm.updateNote(
                        NotesActivity.selectedNote
                );

                Toast toast = Toast.makeText(getApplicationContext(), "Note updated", Toast.LENGTH_SHORT);
                toast.show();

            }else{
                isEditing = true;
                editBtn.setText("Save");
                title.setEnabled(true);
                content.setEnabled(true);
                tags.setEnabled(true);
            }
        });

        ImageView favBtn = (ImageView)findViewById(R.id.likeBtn);

        if (NotesActivity.selectedNote.isFavorite()){
            TransitionDrawable transitionDrawable = (TransitionDrawable) favBtn.getDrawable();
            transitionDrawable.reverseTransition(1);
        }

        favBtn.setOnClickListener(v -> {

            NotesActivity.selectedNote.setIsFavorite(!NotesActivity.selectedNote.isFavorite());

            orm.updateIsFavorite(
                    NotesActivity.selectedNote
            );

            TransitionDrawable transitionDrawable = (TransitionDrawable) favBtn.getDrawable();
            transitionDrawable.reverseTransition(600);

            Toast toast = Toast.makeText(getApplicationContext(), NotesActivity.selectedNote.isFavorite() ? "Added to favorite" : "Removed from favorite", Toast.LENGTH_SHORT);
            toast.show();

        });

        Button backBtn = (Button)findViewById(R.id.backFromNoteBtn);

        backBtn.setOnClickListener(v -> {
            finish();
        });

        Button sendAsEmailBtn = (Button)findViewById(R.id.sendEmailBtn);
        sendAsEmailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent gmailIntent =new Intent();
                gmailIntent.setAction(Intent.ACTION_SENDTO);
                gmailIntent.setType("message/rfc822");
                gmailIntent.setData(Uri.parse("mailto:"));
                gmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"nidal_ibraheem@andriod.com"});
                gmailIntent.putExtra(Intent.EXTRA_SUBJECT, NotesActivity.selectedNote.getTitle());
                gmailIntent.putExtra(Intent.EXTRA_TEXT, NotesActivity.selectedNote.getContent());
                startActivity(gmailIntent);
            }
        });

    }
}
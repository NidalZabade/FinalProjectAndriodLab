package edu.birzeit.nidlibraheem.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import edu.birzeit.nidlibraheem.finalproject.models.Note;

public class AddNoteActivity extends AppCompatActivity {
    EditText titleET, contentET;
    Button saveBtn;

    ORM orm = ORM.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        titleET = findViewById(R.id.titleET);
        contentET = findViewById(R.id.contentET);
        saveBtn = findViewById(R.id.save);


        saveBtn.setOnClickListener(v -> {
            String title = titleET.getText().toString();
            String content = contentET.getText().toString();
            if (title.isEmpty() || content.isEmpty()) {
                titleET.setError("Please enter title");
                contentET.setError("Please enter content");
            } else {
                Note note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.setCreationDate(new Date());
                note.setIsFavorite(false);
                note.setOwner(MainActivity.loggedInUser);
                orm.insertNote(note);
                Toast.makeText(this, "Note added successfully with id: " + note.getId(), Toast.LENGTH_SHORT).show();
                finish();
            }
            });
    }

}
package edu.birzeit.nidlibraheem.finalproject.ui;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.nidlibraheem.finalproject.NotePage;
import edu.birzeit.nidlibraheem.finalproject.NotesActivity;
import edu.birzeit.nidlibraheem.finalproject.ORM;
import edu.birzeit.nidlibraheem.finalproject.R;
import edu.birzeit.nidlibraheem.finalproject.models.Note;


public class NotesListAdapter extends BaseAdapter {

    ArrayList<Note> notes;
    private static LayoutInflater inflater = null;

    public NotesListAdapter(Context context, ArrayList<Note> notes) {
        this.notes = notes;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (vi == null)
            vi = inflater.inflate(R.layout.note_list_item, null);

        Note note = notes.get(position);

        TextView text = (TextView) vi.findViewById(R.id.noteTitleTV);
        text.setText(note.getTitle());


        text.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NotesActivity.selectedNote = note;
                        Intent intent = new Intent(v.getContext(), NotePage.class);

                        startActivity(v.getContext(), intent, null);

                    }
                }
        );

        text = (TextView) vi.findViewById(R.id.tagTV);

        if (note.getTags() != null && !note.getTags().isEmpty())
            text.setText("Tag: " + note.getTags());
        else
            text.setText("Tag: " + "Uncategorized");


        ImageView fav_btn = (ImageView) vi.findViewById(R.id.likeBtnListItem);

        ORM orm = ORM.getInstance(vi.getContext());

        if (note.isFavorite()){
            TransitionDrawable transitionDrawable = (TransitionDrawable) fav_btn.getDrawable();
            transitionDrawable.reverseTransition(1);
        }

        fav_btn.setOnClickListener(v -> {

            note.setIsFavorite(!note.isFavorite());

            orm.updateIsFavorite(
                    note
            );

            TransitionDrawable transitionDrawable = (TransitionDrawable) fav_btn.getDrawable();
            transitionDrawable.reverseTransition(600);

            Toast toast = Toast.makeText(v.getContext(), note.isFavorite() ? "Added to favorites" : "Removed from favorites", Toast.LENGTH_SHORT);
            toast.show();

        });


        return vi;
    }

    void refresh(ArrayList<Note> notes) {
        this.notes = notes;

        notifyDataSetChanged();
    }
}
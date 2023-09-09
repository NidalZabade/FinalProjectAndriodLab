package edu.birzeit.nidlibraheem.finalproject.ui.favorite;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import edu.birzeit.nidlibraheem.finalproject.MainActivity;
import edu.birzeit.nidlibraheem.finalproject.ORM;
import edu.birzeit.nidlibraheem.finalproject.databinding.FragmentAllBinding;
import edu.birzeit.nidlibraheem.finalproject.databinding.FragmentFavoriteBinding;
import edu.birzeit.nidlibraheem.finalproject.models.Note;
import edu.birzeit.nidlibraheem.finalproject.ui.NotesListAdapter;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavoriteViewModel favoriteViewModel =
                new ViewModelProvider(this).get(FavoriteViewModel.class);

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText searchField = (EditText) binding.searchEditText;

        searchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                refreshData();

            }
        });


        refreshData();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    interface refreshCommunicator {
        public void refresh();
    }

    public void refreshData() {

        ListView listView = (ListView) binding.notesList;

        ORM orm = ORM.getInstance(getContext());

        String query = binding.searchEditText.getText().toString();

        ArrayList<Note> notes = orm.searchAllFavouriteNotesForUser(query, MainActivity.loggedInUser);

        NotesListAdapter notesAdapter = new NotesListAdapter(getContext(), notes);

        listView.setAdapter(notesAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package edu.birzeit.nidlibraheem.finalproject.ui.sorted;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import edu.birzeit.nidlibraheem.finalproject.MainActivity;
import edu.birzeit.nidlibraheem.finalproject.ORM;
import edu.birzeit.nidlibraheem.finalproject.databinding.FragmentSortedBinding;
import edu.birzeit.nidlibraheem.finalproject.models.Note;
import edu.birzeit.nidlibraheem.finalproject.ui.NotesListAdapter;

public class SortedFragment extends Fragment {

    private FragmentSortedBinding binding;

    private boolean sortByTitle = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SortedViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SortedViewModel.class);

        binding = FragmentSortedBinding.inflate(inflater, container, false);
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

        Switch orderBySwitch = (Switch) binding.orderBySwitch;


        orderBySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sortByTitle = isChecked;
            orderBySwitch.setText(isChecked ? "Title " : "Creation Date ");
            refreshData();
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

        ArrayList<Note> notes = orm.searchAllNotesForUserSortedBy(query, MainActivity.loggedInUser, sortByTitle);

        NotesListAdapter notesAdapter = new NotesListAdapter(getContext(), notes);

        listView.setAdapter(notesAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package edu.birzeit.nidlibraheem.finalproject.ui.all;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import edu.birzeit.nidlibraheem.finalproject.MainActivity;
import edu.birzeit.nidlibraheem.finalproject.ORM;
import edu.birzeit.nidlibraheem.finalproject.databinding.FragmentAllBinding;

public class AllFragment extends Fragment {

    private FragmentAllBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AllViewModel allViewModel =
                new ViewModelProvider(this).get(AllViewModel.class);

        binding = FragmentAllBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAll;

        Cursor cursor;
        cursor = ORM.getInstance(getContext()).getAllNotes(MainActivity.loggedInUser);
        String notes = "";
        while (cursor.moveToNext()) {
            notes += cursor.getString(1) + "Note: \n";
        }
        textView.setText(notes);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        System.out.println("\nonResume...\n");
    }

    interface refreshCommunicator {
        public void refresh();
    }

    public void refreshData(){
        Cursor cursor;
        cursor = ORM.getInstance(getContext()).getAllNotes(MainActivity.loggedInUser);
        String notes = "";
        while (cursor.moveToNext()) {
            notes += cursor.getString(1) + "Note: \n";
        }
        binding.textAll.setText(notes);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
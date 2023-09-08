package edu.birzeit.nidlibraheem.finalproject.ui.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.birzeit.nidlibraheem.finalproject.MainActivity;
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
        textView.setText("Welcome " + MainActivity.loggedInUser.getFirstName());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
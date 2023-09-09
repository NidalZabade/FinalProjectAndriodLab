package edu.birzeit.nidlibraheem.finalproject.ui.sorted;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import edu.birzeit.nidlibraheem.finalproject.databinding.FragmentSortedBinding;

public class SortedFragment extends Fragment {

    private FragmentSortedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SortedViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SortedViewModel.class);

        binding = FragmentSortedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSorted;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
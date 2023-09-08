package edu.birzeit.nidlibraheem.finalproject.ui.profile;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.birzeit.nidlibraheem.finalproject.MainActivity;
import edu.birzeit.nidlibraheem.finalproject.ORM;
import edu.birzeit.nidlibraheem.finalproject.R;
import edu.birzeit.nidlibraheem.finalproject.SignUpActivity;
import edu.birzeit.nidlibraheem.finalproject.databinding.FragmentProfileBinding;
import edu.birzeit.nidlibraheem.finalproject.models.User;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel favoriteViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText firstNameTF = binding.profileFirstname;
        EditText lastNameTF = binding.profileLastname;
        EditText passwordTF = binding.profilePassword;
        EditText confirmPasswordTF = binding.profileConfirmPassword;
        Button saveButton = binding.profileSave;
        Button logoutButton = binding.profileLogout;

        ORM orm = ORM.getInstance(getContext());

        firstNameTF.setText(MainActivity.loggedInUser.getFirstName());
        lastNameTF.setText(MainActivity.loggedInUser.getLastName());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameTF.getText().toString();
                String lastName = lastNameTF.getText().toString();
                String password = passwordTF.getText().toString();
                String confirmPassword = confirmPasswordTF.getText().toString();

                boolean error_flag = false;

                if( firstName.isEmpty() ) {
                    firstNameTF.setError("This field is required");
                    error_flag = true;
                }

                if( lastName.isEmpty() ) {
                    lastNameTF.setError("This field is required");
                    error_flag = true;
                }

                if( !password.isEmpty() || !confirmPassword.isEmpty() ) {
                    if( !password.equals(confirmPassword) ) {
                        passwordTF.setError("Passwords do not match");
                        confirmPasswordTF.setError("Passwords do not match");
                        error_flag = true;
                    }
                }


                try {
                    User.validateName(firstName, "First Name");
                } catch (IllegalArgumentException e) {
                    firstNameTF.setError(e.getMessage());
                    error_flag = true;

                }

                try {
                    User.validateName(lastName, "Last Name");
                } catch (IllegalArgumentException e) {
                    lastNameTF.setError(e.getMessage());
                    error_flag = true;
                }

                try {
                    User.validatePassword(password, confirmPassword);
                } catch (IllegalArgumentException e) {
                    passwordTF.setError(e.getMessage());
                    error_flag = true;
                }

                if( error_flag ) {
                    return;
                }

                ORM orm = ORM.getInstance(getContext());
                MainActivity.loggedInUser = orm.updateUser(MainActivity.loggedInUser.getEmail(), firstName, lastName, password);
                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.loggedInUser = null;
                getActivity().finish();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
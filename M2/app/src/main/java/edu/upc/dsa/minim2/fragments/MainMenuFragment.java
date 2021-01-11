package edu.upc.dsa.minim2.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.minim2.MainActivity;
import edu.upc.dsa.minim2.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class MainMenuFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;
    EditText userInbox;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return view;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setBackActivated(false);

        userInbox = view.findViewById(R.id.editTextTextPersonName);

        if (!mainActivity.isNetworkConnected()) {

            Navigation.findNavController(view).navigate(R.id.noInternetConnectionFragment);
            return;

        }

        //TextView tvHello = view.findViewById(R.id.tvHello);
        //tvHello.setText(getString(R.string.hello_string) + " " + mainActivity.getSavedUsername() + "!");
        view.findViewById(R.id.btnExploreRepositories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = userInbox.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("username", username);

                Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_profileFragment, bundle);
            }
        });
    }

}
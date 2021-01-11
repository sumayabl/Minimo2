package edu.upc.dsa.minim2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.upc.dsa.minim2.MainActivity;
import edu.upc.dsa.minim2.R;
import edu.upc.dsa.minim2.models.ProfileResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class SplashScreenFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setBackActivated(true);

        if (!mainActivity.isNetworkConnected()) {

            Navigation.findNavController(view).navigate(R.id.noInternetConnectionFragment);
            return;

        }

        ProfileRequest();

    }

    public void ProfileRequest() {

        mainActivity.setLoadingData(true);

        Call<List<ProfileResponse>> resp = mainActivity.getProfileService().getProfile();

        resp.enqueue(new Callback<List<ProfileResponse>>() {

            @Override
            public void onResponse(Call<List<ProfileResponse>> call, Response<List<ProfileResponse>> response) {

                mainActivity.setLoadingData(false);

                switch (response.code()) {

                    case 250:

                        List<ProfileResponse> ProfileResponse = response.body();

                        Toast.makeText(getContext(), "Se ha recibido código 250", Toast.LENGTH_SHORT).show();
                        break;

                    case 601:
                        break;

                    case 603:
                        break;

                    default:
                        Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);
                        break;

                }

            }

            @Override
            public void onFailure(Call<List<ProfileResponse>> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });

    }
}
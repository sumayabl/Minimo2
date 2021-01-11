package edu.upc.dsa.minim2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.upc.dsa.minim2.MainActivity;
import edu.upc.dsa.minim2.MyAdapter;
import edu.upc.dsa.minim2.R;
import edu.upc.dsa.minim2.models.ProfileResponse;
import edu.upc.dsa.minim2.models.RepositoriResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
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

        profileRequest();

    }

    public void getRepositoris() {

        String username = getArguments().getString("username");

        mainActivity.setLoadingData(true);

        Call<List<RepositoriResponse>> resp = mainActivity.getProfileService().getRepos(username);

        resp.enqueue(new Callback<List<RepositoriResponse>>() {

            @Override
            public void onResponse(Call<List<RepositoriResponse>> call, Response<List<RepositoriResponse>> response) {

                mainActivity.setLoadingData(false);

                switch (response.code()) {

                    case 200:
                        List<RepositoriResponse> repositoriResponses = response.body();

                        RecyclerView recyclerView;

                        recyclerView = view.findViewById(R.id.recycler_view);

                        recyclerView.setHasFixedSize(true);

                        RecyclerView.LayoutManager layoutManager;
                        layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);

                        // define an adapter

                        RecyclerView.Adapter mAdapter;
                        mAdapter = new MyAdapter(repositoriResponses);
                        recyclerView.setAdapter(mAdapter);

                        break;

                    default:
                        Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);
                        break;

                }

            }

            @Override
            public void onFailure(Call<List<RepositoriResponse>> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });


    }

    private void profileRequest() {

        String username = getArguments().getString("username");

        //Barra de "cargando"
        mainActivity.setLoadingData(true);

        Call<ProfileResponse> resp = mainActivity.getProfileService().getUser(username);

        resp.enqueue(new Callback<ProfileResponse>() {

            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                mainActivity.setLoadingData(false);

                switch (response.code()) {

                    case 200:

                        ProfileResponse profileResponse = response.body();

                        TextView tvFollowing = view.findViewById(R.id.Following);
                        TextView username = view.findViewById(R.id.User);
                        TextView followers = view.findViewById(R.id.Followers);

                        ImageView ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);

                        tvFollowing.setText("Following: " + String.valueOf(profileResponse.getFollowing()));
                        username.setText("Username: " + String.valueOf(profileResponse.getLogin()));
                        followers.setText("Followers: " + String.valueOf(profileResponse.getFollowers()));

                        Picasso.get().load(profileResponse.getAvatar_url()).into(ivProfilePhoto);

                        getRepositoris();

                        break;

                    case 404:
                        Toast.makeText(getContext(), "User not exists", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);
                        break;

                }

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

                mainActivity.setLoadingData(false);
                Navigation.findNavController(view).navigate(R.id.connectionErrorFragment);

            }

        });
    }

}
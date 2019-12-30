package com.app.ashesha.v3care.Menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.ashesha.v3care.MainActivity;
import com.app.ashesha.v3care.R;
import com.app.ashesha.v3care.Utils.PrefManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MenuFragment extends Fragment {
    View rootView;
    ListView list;
    GoogleApiClient mGoogleApiClient;
    PrefManager prefManager;
    String[] maintitle = {
             "Refferal ID",
            "Contact Us", "Terms And Conditions", "Rate Us Our app ",
            "SIGNOUT",
    };


    Integer[] imgid = {
            R.drawable.ic_person_outline_black_24dp,
            R.drawable.ic_contact_phone_black_24dp, R.drawable.ic_book_black_24dp, R.drawable.ic_star_black_24dp,
             R.drawable.ic_lock_black_24dp
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account, container, false);

        prefManager = new PrefManager(getActivity());
        MyListAdapter adapter = new MyListAdapter(getActivity(), maintitle, imgid);
        list = (ListView) rootView.findViewById(R.id.listView);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Fragment fragment = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                // TODO Auto-generated method stub
                 if (position == 0) {
                    Intent intent = new Intent(getActivity(), GenerateReferalCode.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(getActivity(), TermsAndConditionsActivity.class);
                    startActivity(intent);

                } else if (position == 3) {
                     final String appPackageName = "com.app.ashesha.v3care"; // package name of the app
                     try {
                         startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                     } catch (android.content.ActivityNotFoundException anfe) {
                         startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                     }
                }  else if (position == 4) {
                     prefManager.logout();
                     Intent loginscreen = new Intent(getActivity(), MainActivity.class);
                     loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(loginscreen);
                     LoginManager.getInstance().logOut();

                     Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                             new ResultCallback<Status>() {
                                 @Override
                                 public void onResult(Status status) {

                                     Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                     Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                     startActivity(i);
                                 }
                             });
                     getActivity().finish();
                }
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }
}

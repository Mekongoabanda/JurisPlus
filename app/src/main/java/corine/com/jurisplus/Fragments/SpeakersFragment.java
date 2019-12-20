package corine.com.jurisplus.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import corine.com.jurisplus.R;

public class SpeakersFragment extends Fragment {

    private View mMainView;

    public SpeakersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On gonfle le layout ce fragment et la retourne à la fin de la fonction principale
        mMainView = inflater.inflate( R.layout.activity_speakers_fragment, container, false );


        return mMainView;
    }
}

package corine.com.jurisplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import corine.com.jurisplus.Fragments.AffaireFragment;
import corine.com.jurisplus.Fragments.SpeakersFragment;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mApp_name, title_frag, case_count;
    private Button logOut_btn;
    //L'on déclare tous les fragments qui pourront être afficher lors du switch de nos items de la bottomBar
    private AffaireFragment affaireFragment;
    private SpeakersFragment speakersFragment;
    private DatabaseReference mAffaireDatabase, mRootRef, mUserDatabase;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private FirebaseUser currentUser;

    //déclaration de notre Frame layout qui va contenir nos fragments
    private FrameLayout mMainFrame;

    //déclaration de notre bottom navigation view
    private BottomNavigationView mMainNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_principal );

        //****** on initialise notre frame layout et notre bottom view navigation******//
        mMainFrame = (FrameLayout) findViewById( R.id.main_frame );
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);
        //*****************************************************************************//

        mApp_name = (TextView) findViewById( R.id.app_name );
        case_count = (TextView) findViewById( R.id.case_count );
        title_frag = (TextView) findViewById( R.id.title_frag );
        logOut_btn = (Button) findViewById( R.id.log_out );
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.keepSynced( true );
        mAffaireDatabase = FirebaseDatabase.getInstance().getReference().child( "Affaire" );
        mAffaireDatabase.keepSynced( true );
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = mRootRef.child( "Actor" );
        currentUser = mAuth.getCurrentUser();
        mCurrentUserId = currentUser.getUid();

        //Polices
        Typeface faceArtBrewery = Typeface.createFromAsset(this.getAssets(), "fonts/Art Brewery.ttf");
        mApp_name.setTypeface(faceArtBrewery, Typeface.BOLD);
        Typeface face = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Bold.ttf");
        title_frag.setTypeface(face, Typeface.BOLD);

        //Chargement de notre ressource d'animation
        Animation myanim = AnimationUtils.loadAnimation( this, R.anim.mytransition );
        mApp_name.startAnimation( myanim );
        logOut_btn.startAnimation( myanim );

        //******l'on initialise nos fragments déclarés plus haut******//
        affaireFragment = new AffaireFragment();
        speakersFragment = new SpeakersFragment();
        //***********************************************************//

        logOut_btn.setOnClickListener( this );

        //On indique le fragment par défaut
        setFragment( affaireFragment );
        title_frag.setText( " - Affaire/Case" );

        mMainNav.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //action lorsque clic sur un item de notre bottom bar
                switch (item.getItemId()) {

                    case R.id.nav_affaire:
                        //attribution d'un fragment
                        setFragment(affaireFragment);
                        title_frag.startAnimation( myanim );
                        title_frag.setText( " - Affaire/Case" );
                        mAffaireDatabase.addValueEventListener( new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                case_count.setText( "(" + dataSnapshot.getChildrenCount() + ")" );

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );

                        return true;

                    case R.id.nav_intervenants:
                        //attribution d'un fragment
                        setFragment( speakersFragment );
                        title_frag.startAnimation( myanim );
                        title_frag.setText( " - Intervenants/Speakers" );

                        return true;


                }
                return false;
            }
        } );

    }

    //--------------------------------------------------------DEBUT  METHODES PRIVEES pour assigner les fragments------------------------------------------------------------------------------------------

    //Méthode privée pour assigner le fragment dans le FrameLayout
    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View v) {
        if (v == logOut_btn){

            mAuth.signOut();
            Toast.makeText( this, "Vous êtes déconnecté", Toast.LENGTH_SHORT ).show();
            Intent i = new Intent( this, MainActivity.class );
            startActivity( i );
            finish();

        }
    }

    //--------------------------------------------------------FIN METHODES PRIVEES pour assigner les fragments------------------------------------------------------------------------------------------

}

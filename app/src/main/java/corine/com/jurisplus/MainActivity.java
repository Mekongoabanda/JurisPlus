package corine.com.jurisplus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.fabric.sdk.android.Fabric;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView mTitle, long_date, mheure, mApp_name, republique;
    private ImageView sceau_cmr;
    FirebaseAuth firebaseAuth;
    DatabaseReference mUsersRef;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Fabric.with(this, new Crashlytics());
        setContentView( R.layout.activity_main );

        firebaseAuth = FirebaseAuth.getInstance();

        mTitle = (TextView) findViewById( R.id.title );
        long_date = (TextView) findViewById( R.id.long_date );
        mheure = (TextView) findViewById( R.id.heure );
        mApp_name = (TextView) findViewById( R.id.app_name );
        republique = (TextView) findViewById( R.id.republique );
        sceau_cmr = (ImageView) findViewById( R.id.sceau_cmr );

        //Polices
        Typeface faceArtBrewery = Typeface.createFromAsset(this.getAssets(), "fonts/Art Brewery.ttf");
        mApp_name.setTypeface(faceArtBrewery, Typeface.BOLD);
        Typeface face = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Bold.ttf");
        mTitle.setTypeface(face, Typeface.BOLD);
        long_date.setTypeface(face, Typeface.ITALIC);
        mheure.setTypeface(face, Typeface.ITALIC);
        republique.setTypeface(face, Typeface.ITALIC);

        //objet de type date
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat( "HH:mm" );
        String currentDate = f.format( d );
        //Capture la date actuelle
        final String currentLongDate = DateFormat.getDateTimeInstance().format( new Date());

        mheure.setText( currentDate );
        long_date.setText( currentLongDate );

        //Chargement de notre ressource d'animation
        Animation myanim = AnimationUtils.loadAnimation( this, R.anim.mytransition );
        mApp_name.startAnimation( myanim );
         mTitle.startAnimation( myanim );
        sceau_cmr.startAnimation( myanim );

            final Intent i = new Intent (this, PrincipalActivity.class);
            final Intent in = new Intent(this, WhoIsItActivity.class);
            Thread timer = new Thread(  ){

                public void run (){
                    try{
                        sleep( 5000 );
                    }
                    catch(InterruptedException e){

                        e.printStackTrace();
                    }
                    finally {
                        if (firebaseAuth.getCurrentUser() != null ) {
                            //Si l'utilisateur est déja connecté voici ce qui se passe
                            //on cherche l'ID de l'utilisateur courant
                            mUsersRef = FirebaseDatabase.getInstance().getReference().child( "Actor" ).child( firebaseAuth.getCurrentUser().getUid() );
                            //Toast.makeText( MainActivity.this, "Content de vous savoir avec nous", Toast.LENGTH_SHORT ).show();
                            //quand on appuie sur retour ça quitte l'app si on est coonnecté
                            i.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                            startActivity( i );
                            finish();
                        }else{
                            startActivity( in );
                            finish();
                        }
                    }
                }

            };
            timer.start();



    }
}

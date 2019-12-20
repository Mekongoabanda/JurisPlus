package corine.com.jurisplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WhoIsItActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView republique, ministere_english, ministere_french, app_name, whoIsIt;
    private Button magistrat, avocat, greffier, documentaliste;
    String TYPE_JURISTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_who_is_it );

        republique = (TextView) findViewById( R.id.republique );
        ministere_english = (TextView) findViewById( R.id.ministere_english );
        ministere_french = (TextView) findViewById( R.id.ministere_french );
        app_name = (TextView) findViewById( R.id.app_name );
        whoIsIt = (TextView) findViewById( R.id.whoIsIt );
        magistrat = (Button) findViewById( R.id.magistrat_btn );
        avocat = (Button) findViewById( R.id.avocat_btn );
        greffier = (Button) findViewById( R.id.greffiers_btn );
        documentaliste = (Button) findViewById( R.id.documentalistes_btn );

        //Polices
        Typeface faceArtBrewery = Typeface.createFromAsset(this.getAssets(), "fonts/Art Brewery.ttf");
        app_name.setTypeface(faceArtBrewery, Typeface.BOLD);
        Typeface face = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Bold.ttf");
        republique.setTypeface(face, Typeface.BOLD);
        ministere_french.setTypeface(face, Typeface.ITALIC);
        ministere_english.setTypeface(face, Typeface.ITALIC);
        whoIsIt.setTypeface(face, Typeface.ITALIC);

        magistrat.setOnClickListener( this );
        avocat.setOnClickListener( this );
        greffier.setOnClickListener( this );
        documentaliste.setOnClickListener( this );


    }

    @Override
    public void onClick(View v) {

        if( v == magistrat){

            TYPE_JURISTE = "magistrat";
            Intent intent = new Intent( this, SignInActivity.class );
            intent.putExtra( "type de compte", TYPE_JURISTE );
            createToast( TYPE_JURISTE );
            startActivity( intent );


        }
        if( v == avocat){

            TYPE_JURISTE = "avocat";
            Intent intent = new Intent( this, SignInActivity.class );
            intent.putExtra( "type de compte", TYPE_JURISTE );
            createToast( TYPE_JURISTE );
            startActivity( intent );
        }

        if( v == greffier){
            TYPE_JURISTE = "greffier";
            Intent intent = new Intent( this, SignInActivity.class );
            intent.putExtra( "type de compte", TYPE_JURISTE );
            createToast( TYPE_JURISTE );
            startActivity( intent );

        }

        if( v == documentaliste){
            TYPE_JURISTE = "documentaliste";
            Intent intent = new Intent( this, SignInActivity.class );
            intent.putExtra( "type de compte", TYPE_JURISTE );
            createToast( TYPE_JURISTE );
            startActivity( intent );

        }

    }

    public void createToast(String message){

        Toast.makeText( this, message, Toast.LENGTH_SHORT ).show();
    }
}

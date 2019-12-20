package corine.com.jurisplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView republique, ministere_french, app_name, mJurisType, mSignIn, whoIsIt;
    private Button mValideBtn;
    private EditText editEmail, editKey, editPass;
    private ImageView mIcone;
    String type;
    private ProgressDialog progressdialog;
    private FirebaseAuth firebaseAuth;

    //déclaration pour utiliser la base de données
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up );

        type = getIntent().getStringExtra( "type de compte" );

        //on initilaise notre firebaseAuth dévlrée là haut, en vue de l'authentification à travers firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //pour notre progressBar déclarée là haut
        progressdialog = new ProgressDialog( this );

        republique = (TextView) findViewById( R.id.republique );
        ministere_french = (TextView) findViewById( R.id.ministere_french );
        app_name = (TextView) findViewById( R.id.app_name );
        mJurisType = (TextView) findViewById( R.id.juris_type_txt );
        mSignIn = (TextView) findViewById( R.id.iNotSignUp );
        whoIsIt = (TextView) findViewById( R.id.whoIsIt );
        mValideBtn = (Button) findViewById( R.id.valider_btn );
        mIcone = (ImageView) findViewById( R.id.icone );
        editEmail = (EditText) findViewById( R.id.editemail );
        editKey = (EditText) findViewById( R.id.editCode );
        editPass = (EditText) findViewById( R.id.editPassword );

        //Polices
        Typeface faceArtBrewery = Typeface.createFromAsset(this.getAssets(), "fonts/Art Brewery.ttf");
        app_name.setTypeface(faceArtBrewery, Typeface.BOLD);
        Typeface face = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Bold.ttf");
        republique.setTypeface(face, Typeface.BOLD);
        ministere_french.setTypeface(face, Typeface.ITALIC);
        whoIsIt.setTypeface(face, Typeface.ITALIC);
        mJurisType.setTypeface( face, Typeface.BOLD );
        mSignIn.setTypeface( face, Typeface.ITALIC);
        mValideBtn.setTypeface( face, Typeface.BOLD);

        mSignIn.setOnClickListener( this );
        mValideBtn.setOnClickListener( this );

        //Chargement de notre ressource d'animation
        Animation myanim = AnimationUtils.loadAnimation( this, R.anim.mytransition );
        app_name.startAnimation( myanim );
        mIcone.startAnimation( myanim );

        changeIcon();
    }

    private void changeIcon() {

        mJurisType.setText( this.type );
        switch (this.type) {
            case "magistrat":

                mIcone.setImageResource( R.drawable.magistrate_ic );

                break;
            case "avocat":

                mIcone.setImageResource( R.drawable.lawyer );

                break;
            case "greffier":

                mIcone.setImageResource( R.drawable.clerk );

                break;
            case "documentaliste":

                mIcone.setImageResource( R.drawable.librarian );

                break;
        }

    }

    @Override
    public void onClick(View v) {

        if (v == mSignIn){
            Intent intent = new Intent( this, SignInActivity.class );
            intent.putExtra( "type de compte", this.type );
            startActivity( intent );
            finish();

        }

        if(v == mValideBtn){

            registerUser(this.type);

        }
    }

    private void registerUser(String type) {
        final String Email = editEmail.getText().toString().trim();
        final String Key = editKey.getText().toString().trim();
        final String Mot_de_passe = editPass.getText().toString().trim();

        //On crée une variable pour la vibration
        final Vibrator vibrator = (Vibrator) getSystemService( VIBRATOR_SERVICE );

        //On crée une variable de type long qu'on appelle pattern (objet)
        final long[] pattern = {200, 300}; //Pause pendant 200 millisecondes (0.2 sec) et vibre pendant 300 milliseconds

        if (TextUtils.isEmpty(Key)){
            vibrator.vibrate( pattern,-1 );// 0 signifie repéter chaque fois et -1 juste une fois
            //si le champ de l'Email est vide
            Toast.makeText(this, "Veuillez entrer la clé privée", Toast.LENGTH_SHORT).show();
            //arreter l'execution de la fonction
            return;
        }
        if (TextUtils.isEmpty(Mot_de_passe)){
            vibrator.vibrate( pattern,-1 );// 0 signifie repéter chaque fois et -1 juste une fois
            //si le champ du mot de passe est vide
            Toast.makeText(this, "Veuillez entrer votre Mot de passe", Toast.LENGTH_SHORT).show();
            //arreter l'execution de la fonction
            return;
        }
        if (TextUtils.isEmpty(Email)){
            vibrator.vibrate( pattern,-1 );// 0 signifie repéter chaque fois et -1 juste une fois
            // si le champs du nom d'user est vide
            Toast.makeText(this, "Veuillez entrer votre Email", Toast.LENGTH_SHORT).show();
            //arreter l'execution de la fonction
            return;
        }
        if (Mot_de_passe.length() > 8){
            vibrator.vibrate( pattern,-1 );// 0 signifie repéter chaque fois et -1 juste une fois
            // si le champs du nom d'user est vide
            Toast.makeText(this, "le mot de passe doit être inférieur ou égale à 8 lettres", Toast.LENGTH_LONG).show();
            //arreter l'execution de la fonction
            return;
        }

        //nous aurons d'abord une apparition d'une barre de progression
        progressdialog.setTitle( this.type );
        progressdialog.setMessage("Patientez, enregistrement de vos informations...");
        //SetCanceledonTouchOutside(false): ne pas enlever la barre de progression au touché à l'exterieure de celle ci
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();

        firebaseAuth.createUserWithEmailAndPassword(Email,Mot_de_passe)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            //Lorsque l'utilisateur s'isncrit, les données suivantes lui sont assignées...genre statut par defaut, nom d'utilisateur qu'il a inscrit, photo (par défaut) etc
                            FirebaseUser current_user = firebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();

                            //On veut créer une données Token lorsque l'utilisateur s'inscrit, ceci pour les notifications (Firebase function)
                            //device token est le jeton qui permettra d'envoyer un élément sur le téléphone d'un utilisateur
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Actor").child(uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("nom", "default");
                            userMap.put("status", "default");
                            userMap.put("profil_image", "default");
                            userMap.put("Type", type);
                            userMap.put("password", Mot_de_passe);
                            userMap.put("phone_number", "default");
                            userMap.put( "email", Email );
                            userMap.put( "device_token", deviceToken );

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        // l'utilisateur est bien enregistré et vous êtes connecté
                                        //nous allons commencer l'activité de profil ici
                                        //maintenant, nous allons afficher un toast uniquement
                                        Toast.makeText(SignUpActivity.this, "Enregistrement réussi!", Toast.LENGTH_SHORT).show();
                                        progressdialog.hide();
                                        Intent mainIntent = new Intent(SignUpActivity.this, PrincipalActivity.class);
                                        mainIntent.putExtra( "type", type );
                                        //quand on appuie sur retour ça quitte l'app si on est coonnecté
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });

                        }
                        else{
                            String error = task.getException().getMessage();
                            Toast.makeText(SignUpActivity.this, "Error : " + error, Toast.LENGTH_LONG).show();
                            progressdialog.hide();
                            vibrator.vibrate( pattern,-1 );// 0 signifie repéter chaque fois et -1 juste une fois
                        }

                    }
                });
    }
}

package corine.com.jurisplus.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import corine.com.jurisplus.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class AffaireFragment extends Fragment implements View.OnClickListener {

    private View mMainView;
    private ImageButton floating_edit;
    private RecyclerView mAffaireList;
    private LinearLayoutManager mLayoutManager;
    private Dialog addAffaire_dialog;
    private DatabaseReference mAffaireDatabase, mRootRef;
    private TextView have_case;
    //On crée une variable de type long qu'on appelle pattern (objet)
    private final long[] pattern = {200, 300}; //Pause pendant 200 millisecondes (0.2 sec) et vibre pendant 300 milliseconds


    public AffaireFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On gonfle le layout ce fragment et la retourne à la fin de la fonction principale
        mMainView = inflater.inflate( R.layout.activity_affaire_fragment, container, false );

        floating_edit = (ImageButton) mMainView.findViewById( R.id.floating_edit );
        have_case = mMainView.findViewById( R.id.have_case );

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.keepSynced( true );
        mAffaireDatabase = FirebaseDatabase.getInstance().getReference().child( "Affaire" );
        mAffaireDatabase.keepSynced( true );
        mAffaireList = (RecyclerView) mMainView.findViewById( R.id.affaire_list );
        addAffaire_dialog = new Dialog(getContext());

        mLayoutManager = new LinearLayoutManager(getActivity());
        //On adapte notre recyclerView au layout
        mAffaireList.setHasFixedSize( true );
        mAffaireList.setLayoutManager( mLayoutManager );

        floating_edit.setOnClickListener( this );


        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Affaire, AffaireViewHolder> affaireViewHolderRecyclerAdapter = new FirebaseRecyclerAdapter<Affaire, AffaireViewHolder>(
                Affaire.class,
                R.layout.recycler_affaire_layout,
                AffaireViewHolder.class,
                mAffaireDatabase
        ) {

            @Override
            protected void populateViewHolder(final AffaireViewHolder affaireViewHolder, Affaire affaire, int position) {

                final String list_user_id = getRef( position ).getKey();
                have_case.setVisibility( View.INVISIBLE );

                mAffaireDatabase.child( list_user_id ).addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String code_affaire = dataSnapshot.child( "code_affaire" ).getValue().toString();
                        final String code_droit = dataSnapshot.child( "code_droit" ).getValue().toString();
                        final String code_juridiction = dataSnapshot.child( "code_juridiction" ).getValue().toString();

                        int x = (int) (Math.random() * 10);

                        affaireViewHolder.setUserImage(x);
                        affaireViewHolder.setStringsValues(code_affaire, code_droit, code_juridiction);


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
            }
        };

        mAffaireList.setAdapter(affaireViewHolderRecyclerAdapter);

    }

    //une class Publique pour notre UserViewHolder
    public static class AffaireViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public AffaireViewHolder(View itemView) {
            super( itemView );
            mView = itemView;

        }

        //Affectation locale de la date d'amitié avec notre objet statut du layout
        public void setStringsValues (String code_affaire1, String code_droit1, String juridiction1){

            TextView code_affaire = (TextView) mView.findViewById( R.id.code_case );
            TextView code_droit = (TextView) mView.findViewById( R.id.code_droit );
            TextView juridiction = (TextView) mView.findViewById( R.id.juridiction );

            code_affaire.setText( "Affaire n° " + code_affaire1 );
            code_droit.setText( "Droit n° " + code_droit1 );
            juridiction.setText( "Juridiction " + juridiction1 );
        }


        public void setUserImage(int x){
              CircleImageView imageView = (CircleImageView) mView.findViewById(R.id.circle_image );

            if( x == 1)
                imageView.setImageResource( R.color.colorPrimary );
            else if (x == 2)
                imageView.setImageResource( R.color.Or );
            else if (x == 3)
                imageView.setImageResource( R.color.Black );
            else if (x == 4)
                imageView.setImageResource( R.color.blue );
            else if (x == 5)
                imageView.setImageResource( R.color.red );
            else if (x == 6)
                imageView.setImageResource( R.color.cyan);
            else if (x == 7)
                imageView.setImageResource( R.color.OrangeTM );
            else if (x == 0)
                imageView.setImageResource( R.color.colorAccent );
            else if (x == 8)
                imageView.setImageResource( R.color.RosePink );
            else if (x == 9)
                imageView.setImageResource( R.color.rose_like );
            else if (x == 10)
                imageView.setImageResource( R.color.purple );
        }



    }

    //todo --------------------------------------- LES CLICK ------------------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {

        if(floating_edit == v){

            AddAffaire();
        }

    }
    //todo --------------------------------------- LES CLICK -----------------------------------------------------------------------------------------------------------

    //todo ---------------------------------------- Dialog Ajout DEBUT --------------------------------------------------------------------------------
    private void AddAffaire() {

        ImageView closeBtn;
        TextView title_popup;
        TextInputLayout code_affaire, code_droit, categorie_affaire, instrumentsL, code_juridiction
                     , date_debut, dateFin;
        Button Add_btn;

        //On lit notre layout avec notre variable de type "Dialog"
        addAffaire_dialog.setContentView( R.layout.popup_add_affaire );

        //Initialisation de  nos variables
        title_popup = addAffaire_dialog.findViewById( R.id.title_dialog_affaire );
        closeBtn = addAffaire_dialog.findViewById( R.id.close_btn );
        code_affaire = addAffaire_dialog.findViewById( R.id.code_input );
        code_droit = addAffaire_dialog.findViewById( R.id.codeDroit_input );
        categorie_affaire = addAffaire_dialog.findViewById( R.id.categorie_input );
        instrumentsL = addAffaire_dialog.findViewById( R.id.legislation_input );
        code_juridiction = addAffaire_dialog.findViewById( R.id.juridiction_input );
        date_debut = addAffaire_dialog.findViewById( R.id.dateDebut_input );
        dateFin = addAffaire_dialog.findViewById( R.id.dateFin_input );
        Add_btn = addAffaire_dialog.findViewById( R.id.Add_btn );
        
        //Si l'utilisateur touche à l'extérieur de la boîte de dialogue, celle-ci disparait
        addAffaire_dialog.setCanceledOnTouchOutside( true );

        //Action lorsque l'on clique sur bouton close
        closeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addAffaire_dialog.dismiss();
                }
            } );

        Add_btn.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                final String StrcodeAffaire = code_affaire.getEditText().getText().toString().trim();
                final String Strcode_droit = code_droit.getEditText().getText().toString().trim();
                final String StrCategorie_affaire = categorie_affaire.getEditText().getText().toString().trim();
                final String StrInstrumentsL = instrumentsL.getEditText().getText().toString().trim();
                final String StrCode_jutidiction = code_juridiction.getEditText().getText().toString().trim();
                final String StrDateDebit = date_debut.getEditText().getText().toString().trim();
                final String StrDateFin = dateFin.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(StrcodeAffaire)){
                    //si le champ de l'Email est vide
                    Toast.makeText(getContext(), "Veuillez entrer le code de l'Affaire", Toast.LENGTH_SHORT).show();
                    title_popup.setText( "Error: Case code should not be empty" );
                    title_popup.setBackgroundResource( R.color.JauneMTN );
                    //arreter l'execution de la fonction
                    return;
                }
                if (TextUtils.isEmpty(Strcode_droit)){
                    //si le champ de l'Email est vide
                    Toast.makeText(getContext(), "Veuillez entrer le code du droit", Toast.LENGTH_SHORT).show();
                    title_popup.setText( "Error:  right code should not be empty" );
                    title_popup.setBackgroundResource( R.color.JauneMTN );
                    return;
                }
                if (TextUtils.isEmpty(StrCategorie_affaire)){
                    //si le champ de l'Email est vide
                    Toast.makeText(getContext(), "Veuillez entrer la catégorie de l'affaire", Toast.LENGTH_SHORT).show();
                    title_popup.setText( "Error: category case should not be empty" );
                    title_popup.setBackgroundResource( R.color.JauneMTN );
                    return;
                }
                if (TextUtils.isEmpty(StrCode_jutidiction)){
                    //si le champ de l'Email est vide
                    Toast.makeText(getContext(), "Veuillez entrer la Juridiction", Toast.LENGTH_SHORT).show();
                    title_popup.setText( "Error: jurisdiction case should not be empty" );
                    title_popup.setBackgroundResource( R.color.JauneMTN );
                    return;
                }
                if (TextUtils.isEmpty(StrInstrumentsL)){
                    //si le champ de l'Email est vide
                    Toast.makeText(getContext(), "Veuillez entrer l'instrument de législation", Toast.LENGTH_SHORT).show();
                    title_popup.setText( "Error: legislative instrument should not be empty" );
                    title_popup.setBackgroundResource( R.color.JauneMTN );
                    return;
                }
                if (TextUtils.isEmpty(StrDateDebit)){
                    //si le champ de l'Email est vide
                    Toast.makeText(getContext(), "Veuillez entrer la date de debut", Toast.LENGTH_SHORT).show();
                    title_popup.setText( "Error: start date should not be empty" );
                    title_popup.setBackgroundResource( R.color.JauneMTN );
                    return;
                }
                if (TextUtils.isEmpty(StrDateFin)){
                    //si le champ de l'Email est vide
                    Toast.makeText(getContext(), "Veuillez entrer la date de Fin", Toast.LENGTH_SHORT).show();
                    title_popup.setText( "Error: End date should not be empty" );
                    title_popup.setBackgroundResource( R.color.JauneMTN );
                    return;
                }

                //mAffaireDatabase = FirebaseDatabase.getInstance().getReference().child( "Affaire" );

                DatabaseReference affaire_push = mAffaireDatabase.push();
                String push_id = affaire_push.getKey();

                title_popup.setBackgroundResource( R.color.white_true );
                title_popup.setText( "Ajouter Affaire \n Add case " );
                HashMap<String, String> data_affaire = new HashMap<>(  );
                data_affaire.put( "code_affaire", code_affaire.getEditText().getText().toString() );
                data_affaire.put( "code_droit", code_droit.getEditText().getText().toString() );
                data_affaire.put( "categorie", categorie_affaire.getEditText().getText().toString() );
                data_affaire.put( "instrument", instrumentsL.getEditText().getText().toString() );
                data_affaire.put( "code_juridiction", code_juridiction.getEditText().getText().toString() );
                data_affaire.put( "date_debut", date_debut.getEditText().getText().toString() );
                data_affaire.put( "date_fin", dateFin.getEditText().getText().toString() );
                
                mAffaireDatabase.child( push_id ).setValue( data_affaire ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        
                        if(task.isSuccessful()){

                            Toast.makeText( getContext(), "Ajout réussi / successful addition!", Toast.LENGTH_LONG ).show();
                            addAffaire_dialog.dismiss();

                        }else{
                            String error = task.getException().toString();
                            Toast.makeText( getContext(), error, Toast.LENGTH_LONG ).show();
                        }
                        
                    }
                } );
            }
        } );

        //Affichage d enotre Popup
        addAffaire_dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        addAffaire_dialog.show();

    }


    //todo ---------------------------------------- Dialog Ajout FIN --------------------------------------------------------------------------------------------------------------

}

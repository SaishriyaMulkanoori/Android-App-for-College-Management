package com.example.smartcollege;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    ImageView imageView;
    TextView nameEt,departmentEt,roll_noEt,yearEt,emailEt;
    ImageButton ib_edit,imageButtonMenu;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Postmember postmember;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageView=getActivity().findViewById(R.id.iv_pf1);
        nameEt=getActivity().findViewById(R.id.tv_name_pf1);
        departmentEt=getActivity().findViewById(R.id.tv_dept_pf1);
        roll_noEt=getActivity().findViewById(R.id.tv_roll_no_pf1);
        yearEt=getActivity().findViewById(R.id.tv_year_pf1);
        emailEt=getActivity().findViewById(R.id.tv_email_pf1);

        ib_edit=getActivity().findViewById(R.id.ib_edit_pf1);
        imageButtonMenu=getActivity().findViewById(R.id.ib_menu_pf1);
        imageButtonMenu.setOnClickListener(this);
        ib_edit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
      switch(v.getId()){
          case R.id.ib_edit_pf1:
              Intent intent=new Intent(getActivity(),UpdateProfileActivity.class);
              startActivity(intent);
              break;
          case R.id.ib_menu_pf1:
               BottomSheetMenu bottomSheetMenu=new BottomSheetMenu();
               bottomSheetMenu.show(getFragmentManager(),"bottonsheet");

              break;
      }
    }
    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentid=user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        reference=firestore.collection("user").document(currentid);
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if(task.getResult().exists()){
                        String nameResult=task.getResult().getString("name");

                          String deptResult=task.getResult().getString("branch");
                          String rollResult=task.getResult().getString("section");
                          String yearResult=task.getResult().getString("year");
                          String emailResult=task.getResult().getString("email");
                          String url=task.getResult().getString("url");



                          Picasso.get().load(url).into(imageView);
                          nameEt.setText(nameResult);
                          departmentEt.setText(deptResult);
                          roll_noEt.setText(rollResult);
                          yearEt.setText(yearResult);
                          emailEt.setText(emailResult);
                      }else{
                          Intent intent=new Intent(getActivity(),ProfileActivity.class);
                          startActivity(intent);
                      }
                    }
                });

    }

}
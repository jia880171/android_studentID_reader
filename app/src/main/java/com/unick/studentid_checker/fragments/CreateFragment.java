package com.unick.studentid_checker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unick.studentid_checker.R;
import com.unick.studentid_checker.SignUpActivity;
import com.unick.studentid_checker.models.Event;
import com.unick.studentid_checker.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String UID;

    EditText editText_schoolname;
    String schoolname;

    EditText editText_color_1;
    String color_1;
    EditText editText_amont_1;
    int amount_1;

    EditText editText_color_2;
    String color_2;
    EditText editText_amont_2;
    int amount_2;

    EditText editText_color_3;
    String color_3;
    EditText editText_amont_3;
    int amount_3;
    Button button_enter;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");



    public CreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
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
        final View myInflatedView = inflater.inflate(R.layout.fragment_create, container,false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UID =user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editText_schoolname = myInflatedView.findViewById(R.id.editView_school_name);
        editText_color_1 = myInflatedView.findViewById(R.id.editView_color_1);
        editText_amont_1 = myInflatedView.findViewById(R.id.editView_amount_1);

        editText_color_2 = myInflatedView.findViewById(R.id.editView_color_2);
        editText_amont_2 = myInflatedView.findViewById(R.id.editView_amount_2);

        editText_color_3 = myInflatedView.findViewById(R.id.editView_color_3);
        editText_amont_3 = myInflatedView.findViewById(R.id.editView_amount_3);

        Log.d("createFragment","school name: "+ schoolname);
        Log.d("createFragment","color 1: "+ color_1);
        Log.d("createFragment","color 2: "+ color_2);
        Log.d("createFragment","color 3: "+ color_3);

        button_enter = myInflatedView.findViewById(R.id.button_enter);
        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });

        return myInflatedView;
    }

    public void createEvent(){
        try{
            schoolname = (editText_schoolname.getText().toString());
        }catch(Exception e){
            // Do something to handle the error;
            new AlertDialog.Builder(getActivity())
                    .setTitle("請輸入校名")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }

        try{
            color_1 = (editText_color_1.getText().toString());
        }catch(Exception e){
            // Do something to handle the error;
            new AlertDialog.Builder(getActivity())
                    .setTitle("請輸入顏色")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }

        try{
            amount_1 = Integer.parseInt(editText_amont_1.getText().toString());
        }catch(Exception e){
            // Do something to handle the error;
            new AlertDialog.Builder(getActivity())
                    .setTitle("數量必須為數字")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }

        try{
            color_2 = (editText_color_2.getText().toString());
        }catch(Exception e){
            // Do something to handle the error;
            new AlertDialog.Builder(getActivity())
                    .setTitle("請輸入顏色")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }

        try{
            amount_2 = Integer.parseInt(editText_amont_2.getText().toString());
        }catch(Exception e){
            // Do something to handle the error;
            new AlertDialog.Builder(getActivity())
                    .setTitle("數量必須為數字")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }

        try{
            color_3 = (editText_color_3.getText().toString());
        }catch(Exception e){
            // Do something to handle the error;
            new AlertDialog.Builder(getActivity())
                    .setTitle("請輸入顏色")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }

        try{
            amount_3 = Integer.parseInt(editText_amont_3.getText().toString());
        }catch(Exception e){
            // Do something to handle the error;
            new AlertDialog.Builder(getActivity())
                    .setTitle("數量必須為數字")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }


        //String key = mDatabase.child("Events/" + sdf.format(new Date()) + "/").push().getKey();
        //Log.d("createFragment","key:"+key);
        Event event = new Event(UID, schoolname, color_1, amount_1, amount_1, color_2, amount_2, amount_2, color_3, amount_3, amount_3);
        mDatabase.child("Events").child(sdf.format(new Date()).toString()).child(schoolname.toString()).setValue(event);

        Fragment ListFragment;
        FragmentManager manager;
        manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction =manager.beginTransaction();
        ListFragment = new ListFragment();
        transaction.replace(R.id.content_frame,ListFragment);
        transaction.commit();

    }

//    // TODO: Rename method, update argument and hook method into UI Event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}

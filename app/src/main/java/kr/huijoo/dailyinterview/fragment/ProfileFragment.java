package kr.huijoo.dailyinterview.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kr.huijoo.dailyinterview.R;
import kr.huijoo.dailyinterview.model.User;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    public ProfileFragment() {}

    Button modeBtn;
    EditText EditText3;
    EditText EditText4;
    TextView TextView1;
    TextView TextView2;
    TextView TextView3;
    TextView TextView4;
    ImageView userImage;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    SharedPreferences pref;
    String currUserName;
    User temp = new User("", "", "", "");
    ValueEventListener listener;

    @Override
    public void onResume() {
        super.onResume();
        listener = mDatabase.child("User").child(currUserName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp.setBirth(dataSnapshot.child("birth").getValue(String.class));
                temp.setName(dataSnapshot.child("name").getValue(String.class));
                temp.setCompany(dataSnapshot.child("company").getValue(String.class));
                temp.setPosition(dataSnapshot.child("position").getValue(String.class));
                TextView1.setText(temp.getName());
                TextView2.setText(temp.getBirth());
                TextView3.setText(temp.getCompany());
                TextView4.setText(temp.getPosition());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mDatabase.removeEventListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        modeBtn = view.findViewById(R.id.textmode);
        userImage = view.findViewById(R.id.profilecircleimage);
        EditText3 = view.findViewById(R.id.edittextprofilecompany);
        EditText4 = view.findViewById(R.id.edittextprofilejob);
        TextView1 = view.findViewById(R.id.textUsername);
        TextView2 = view.findViewById(R.id.textprofiledate);
        TextView3 = view.findViewById(R.id.textprofilecompany);
        TextView4 = view.findViewById(R.id.textprofilejob);
        modeBtn.setOnClickListener(this);

        pref = getActivity().getSharedPreferences("kr.huijoo.dailyinterview", MODE_PRIVATE);

        currUserName = pref.getString("userName", "");

        return view;
    }

    @Override
    public void onClick(View view) {
        if (String.valueOf(modeBtn.getText()).equals("SAVE")){
            String temp3 = EditText3.getText().toString();
            TextView3.setText(temp3);
            EditText3.setVisibility(View.INVISIBLE);
            TextView3.setVisibility(View.VISIBLE);
            String temp4 = EditText4.getText().toString();
            TextView4.setText(temp4);
            EditText4.setVisibility(View.INVISIBLE);
            TextView4.setVisibility(View.VISIBLE);
            modeBtn.setText("EDIT");
            temp.setCompany(temp3);
            temp.setPosition(temp4);
            mDatabase.child("User").child(currUserName).setValue(temp);
            Toast.makeText(getContext(),"저장되었습니다.",Toast.LENGTH_LONG).show();
        } else if(String.valueOf(modeBtn.getText()).equals("EDIT")) {
            String temp3 = TextView3.getText().toString();
            EditText3.setText(temp3);
            EditText3.setVisibility(View.VISIBLE);
            TextView3.setVisibility(View.INVISIBLE);
            String temp4 = TextView4.getText().toString();
            EditText4.setText(temp4);
            EditText4.setVisibility(View.VISIBLE);
            TextView4.setVisibility(View.INVISIBLE);
            modeBtn.setText("SAVE");
            Toast.makeText(getContext(),"희망 회사와 희망 직렬을 편집해보세요!",Toast.LENGTH_LONG).show();
        }
    }
}
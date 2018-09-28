package com.finalproject.app.findingtutors.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.app.findingtutors.R;
import com.finalproject.app.findingtutors.model.Student;
import com.finalproject.app.findingtutors.model.Teacher;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Student> objectArrayList;

    public StudentListAdapter(Activity activity, ArrayList<Student> objectArrayList){
        this.activity = activity;
        this.objectArrayList = objectArrayList;
    }

    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(StudentListAdapter.ViewHolder holder, int position) {
        holder.name.setText(objectArrayList.get(position).getName());
        holder.gender.setText(objectArrayList.get(position).getGender());
        holder.phone.setText(objectArrayList.get(position).getPhone());
        holder.institution.setText(objectArrayList.get(position).getInstitution());

        holder.reqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        try {
            holder.thumbnail.setBackgroundColor(Color.TRANSPARENT);
            holder.thumbnail.setImageBitmap(decodePhoto(objectArrayList.get(position).getThumbnail()));
        } catch (Exception ex){
            holder.thumbnail.setBackgroundColor(Color.parseColor("#DF4F5F"));
            holder.thumbnail.setImageResource(R.drawable.ic_profile_white);
        }
    }

    private Bitmap decodePhoto(String encode){
        byte[] imageAsBytes = Base64.decode(encode, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Button reqButton;
        private TextView name;
        private TextView gender;
        private TextView phone;
        private TextView institution;
        private CircleImageView thumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            reqButton = itemView.findViewById(R.id.requestButton);
            name = itemView.findViewById(R.id.teacher_name);
            gender = itemView.findViewById(R.id.teacher_gender);
            phone = itemView.findViewById(R.id.teacher_phone);
            institution = itemView.findViewById(R.id.teacher_institution);
            thumbnail = (CircleImageView) itemView.findViewById(R.id.profile_img);
        }
    }
}

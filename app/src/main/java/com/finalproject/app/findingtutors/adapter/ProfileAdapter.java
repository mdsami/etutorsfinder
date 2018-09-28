package com.finalproject.app.findingtutors.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finalproject.app.findingtutors.R;
import com.finalproject.app.findingtutors.model.Student;
import com.finalproject.app.findingtutors.model.Teacher;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;



public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Student student;
    private Teacher teacher;
    private boolean isTrue;
    private ClickListenerHandler handler;

    public ProfileAdapter (Student student, boolean type){
        this.student = student;
        this.isTrue = type;
    }

    public ProfileAdapter (Teacher teacher, boolean type ){
        this.teacher = teacher;
        this.isTrue = type;
    }

    public void setOnClickListener(ClickListenerHandler handler){
        this.handler = handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (isTrue){
            holder.nameText.setText(student.getName());
            holder.addText.setText(student.getAddress());
            holder.gPhone.setText(student.getGuardianphone());
            holder.gText.setText(student.getGuardianname());
            holder.genderText.setText(student.getGender());
            holder.instText.setText(student.getInstitution());
            holder.deptText.setText(student.getDeptname());
            holder.classText.setText(student.getClassname());
            holder.phoneText.setText(student.getPhone());
            try {
                holder.imgProfile.setImageBitmap(decodePhoto(student.getThumbnail()));
            } catch (Exception ex){
                holder.imgProfile.setImageResource(R.drawable.ic_avatar);
            }
        } else {
            holder.nameText.setText(teacher.getName());
            holder.addText.setText(teacher.getAddress());
            holder.genderText.setText(teacher.getGender());
            holder.instText.setText(teacher.getInstitution());
            holder.phoneText.setText(teacher.getPhone());
            try {
                holder.imgProfile.setImageBitmap(decodePhoto(teacher.getThumbnail()));
            } catch (Exception ex){
                holder.imgProfile.setImageResource(R.drawable.ic_avatar);
            }


            holder.cLayout.setVisibility(View.GONE);
            holder.gNLayout.setVisibility(View.GONE);
            holder.gPhoneLayout.setVisibility(View.GONE);
            holder.dLayout.setVisibility(View.GONE);
        }

        holder.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onClick(view, holder.getAdapterPosition());
            }
        });
    }

    private Bitmap decodePhoto(String encode){
        byte[] imageAsBytes = Base64.decode(encode, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText, phoneText, classText, deptText, instText, genderText, gText, gPhone, addText;
        private LinearLayout cLayout, gNLayout, gPhoneLayout, dLayout;
        private CircleImageView imgProfile;
        ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            phoneText = itemView.findViewById(R.id.phoneText);
            classText = itemView.findViewById(R.id.className);
            deptText = itemView.findViewById(R.id.deptText);
            instText = itemView.findViewById(R.id.institutionText);
            genderText = itemView.findViewById(R.id.genderText);
            gText = itemView.findViewById(R.id.guardianText);
            gPhone = itemView.findViewById(R.id.guardianPhone);
            addText = itemView.findViewById(R.id.addText);

            cLayout = itemView.findViewById(R.id.classLayout);
            gNLayout = itemView.findViewById(R.id.guardianLayout);
            gPhoneLayout = itemView.findViewById(R.id.gPhoneLayout);
            dLayout = itemView.findViewById(R.id.dLayout);

            imgProfile = itemView.findViewById(R.id.profile_img);
        }
    }

    public interface ClickListenerHandler {
        void onClick(View view, int position);
    }
}

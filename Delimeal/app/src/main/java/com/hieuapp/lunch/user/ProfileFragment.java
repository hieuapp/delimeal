package com.hieuapp.lunch.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.login.LoginManager;
import com.hieuapp.lunch.LunchIOActivity;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.Tabs;
import com.hieuapp.lunch.util.AccountUtils;
import com.hieuapp.lunch.util.FacebookUtils;
import com.hieuapp.lunch.util.FormatUtils;
import com.hieuapp.lunch.util.ImageUtils;
import com.hieuapp.lunch.util.LunchConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hieuapp on 27/02/2017.
 */

public class ProfileFragment extends Fragment {

    RecyclerView rvUserProfile;
    ImageView avatar;
    TextView username;
    ProfileAdapter profileAdapter;
    RelativeLayout cover;
    TextView emptyMsg;
    private static String log_out_label;
    private static String email_label;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.profile));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        avatar = (ImageView) root.findViewById(R.id.img_avatar);
        cover = (RelativeLayout) root.findViewById(R.id.cover);
        rvUserProfile = (RecyclerView)root.findViewById(R.id.info_recycler_view);
        emptyMsg = (TextView)root.findViewById(R.id.tv_empty_msg);
        username = (TextView) root.findViewById(R.id.tv_username);

        try{
            JSONObject user = AccountUtils.getUserProfile(getContext());
            if(user != null){
                String strAvatar = user.getString(AccountUtils.USER_AVATAR);
                String name = user.getString(AccountUtils.USER_NAME);
                username.setText(name);

                byte[] bytes = Base64.decode(strAvatar, Base64.DEFAULT);
                Bitmap src =BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                avatar.setImageDrawable(ImageUtils.roundedImage(root.getContext(), src));

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
                List<ProfileRow> listInfo = loadUserData(getContext());
                profileAdapter = new ProfileAdapter(listInfo);
                rvUserProfile.setLayoutManager(layoutManager);
                rvUserProfile.setItemAnimator(new DefaultItemAnimator());
                rvUserProfile.setAdapter(profileAdapter);

            }else {
                cover.setVisibility(View.INVISIBLE);
                rvUserProfile.setVisibility(View.INVISIBLE);
                emptyMsg.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            FacebookUtils.fetchUserProfile(getContext());
        }

        return root;
    }

    private List<ProfileRow> loadUserData(Context context){
        List<ProfileRow> listRow = new ArrayList<>();
        try {
            JSONObject userProfile = AccountUtils.getUserProfile(getContext());
            String email = userProfile.getString(AccountUtils.USER_EMAIL);
            email_label = context.getResources().getString(R.string.email_label);
            ProfileRow rEmail = new ProfileRow(email_label, email, R.drawable.ic_profile);
            listRow.add(rEmail);

            log_out_label = context.getResources().getString(R.string.logout_label);
            String logoutDes = context.getResources().getString(R.string.logout_des);
            ProfileRow logout = new ProfileRow(log_out_label, logoutDes, R.drawable.ic_log_out);
            listRow.add(logout);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listRow;
    }

    @Override
    public void onResume(){
        super.onResume();
        //set this fragment is active
        LunchIOActivity.CURRENT_TAB = Tabs.PROFILE;
    }

    public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{
        private List<ProfileRow> infoList;

        public ProfileAdapter(List<ProfileRow> infoList){
            this.infoList = infoList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_info_item_layout, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final ProfileRow config = infoList.get(position);
            holder.label.setText(config.getLabel());
            holder.value.setText(config.getValue());
            holder.icon.setImageResource(config.getIcon());
            holder.itemView.setTag(config.getLabel());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getTag() == log_out_label) {
                        new MaterialDialog.Builder(getContext())
                                .title(R.string.logout_label)
                                .positiveText(R.string.yes)
                                .negativeText(R.string.no)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        LoginManager.getInstance().logOut();
                                        getActivity().startActivity(new Intent(getContext(), LunchIOActivity.class));
                                        getActivity().finish();
                                    }
                                })
                                .show();
                        return;
                    }

                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(view.getTag() == email_label){
                        showEditIPDialog();
                        return true;
                    }
                    return false;
                }
            });
        }

        private void showEditIPDialog(){
            String oldIP = LunchConfig.getIpHost(getContext());
            new MaterialDialog.Builder(getContext())
                    .title("Ip host")
                    .inputType(InputType.TYPE_NUMBER_VARIATION_NORMAL)
                    .input("Enter a new IP",oldIP, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            String ip = String.valueOf(input).trim();
                            if(FormatUtils.validateIPv4(ip)){
                                LunchConfig.setIpHost(getContext(), ip);
                                Toast.makeText(getContext(), "Updated ip = " + ip, Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getContext(), "IP invalidate", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).show();
        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView label, value;
            public ImageView icon;
            public ViewHolder(View view) {
                super(view);
                label = (TextView)view.findViewById(R.id.tv_title);
                value = (TextView)view.findViewById(R.id.tv_detail);
                icon = (ImageView)view.findViewById(R.id.img_icon);
            }
        }

    }
}

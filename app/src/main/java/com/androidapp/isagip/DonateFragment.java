package com.androidapp.isagip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DonateFragment extends Fragment implements View.OnClickListener {

    ImageView ivFb, ivTwitter, ivInstagram, ivYoutube;
    TextView txtEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        ivFb = (ImageView) view.findViewById(R.id.image_fb);
        ivTwitter = (ImageView) view.findViewById(R.id.image_twitter);
        ivInstagram = (ImageView) view.findViewById(R.id.image_instagram);
        ivYoutube = (ImageView) view.findViewById(R.id.image_youtube);
        txtEmail = (TextView) view.findViewById(R.id.text_email);

        ivFb.setOnClickListener(this);
        ivTwitter.setOnClickListener(this);
        ivInstagram.setOnClickListener(this);
        ivYoutube.setOnClickListener(this);
        txtEmail.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_fb:
                openUrl("https://www.facebook.com/abscbnfoundationkapamilya/");
                break;
            case R.id.image_twitter:
                openUrl("https://twitter.com/ABSCBNLKFI");
                break;
            case R.id.image_instagram:
                openUrl("https://www.instagram.com/abscbnlkfi/");
                break;
            case R.id.image_youtube:
                openUrl("https://www.youtube.com/user/abscbnfoundation");
                break;
            case R.id.text_email:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"foundation@abs-cbnfoundation.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "From iSagip App");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Write your email here...");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
                break;
        }
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}

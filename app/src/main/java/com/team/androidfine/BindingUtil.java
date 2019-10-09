package com.team.androidfine;

import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import com.squareup.picasso.Picasso;

public class BindingUtil {

    @BindingAdapter("android:text")
    public static void setInt(TextView textView, int value) {
        textView.setText(String.valueOf(value));
    }

    @BindingAdapter("android:text")
    public static void setInt(EditText editText, int value) {
        if (value >= 0) {
            editText.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getInt(EditText editText) {
        String text = editText.getText().toString();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    @BindingAdapter("path")
    public static void setImageUri(ImageView imageView, String imageFilePath) {
        if (imageFilePath != null && !imageFilePath.isEmpty()) {
            Picasso.get().load(imageView.getContext().getString(R.string.server_path) + "/android-fine/" + imageFilePath)
                    .into(imageView);
            //imageView.setImageURI(Uri.parse(imageFilePath));
        }
    }
}

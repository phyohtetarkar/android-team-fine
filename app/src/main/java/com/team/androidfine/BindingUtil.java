package com.team.androidfine;

import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

public class BindingUtil {

    @BindingAdapter("android:text")
    public static void setInt(TextView textView, int value) {
        textView.setText(String.valueOf(value));
    }

    @BindingAdapter("android:text")
    public static void setInt(EditText editText, int value) {
        if (value > 0) {
            editText.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getInt(EditText editText) {
        String text = editText.getText().toString();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

}

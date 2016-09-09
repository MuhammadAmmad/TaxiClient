package com.example.roman.test.utilities;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class HelperClasses {
    public static class MyDialogFragment extends DialogFragment {
        public static AlertDialog.Builder newInstance(Activity activity, String title, String text) {

            return new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(text);
        }
    }

    public static class JSONField {
        private String method;
        private String data;

        public JSONField(String method, String data) {
            this.method = method;
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public String getMethod() {
            return method;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}

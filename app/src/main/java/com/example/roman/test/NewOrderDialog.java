package com.example.roman.test;

import android.support.v4.app.DialogFragment;

public class NewOrderDialog extends DialogFragment {
    public NewOrderDialog() {
    }

    /**
     * creates a new instance of NewOrderFragment
     */
    public static NewOrderDialog newInstance() {
        return new NewOrderDialog();
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        //getting proper access to LayoutInflater is the trick. getLayoutInflater is a                   //Function
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        View view = inflater.inflate(R.layout.my_dialog, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(view);
//        builder.setTitle(getActivity().getString(R.string.new_order)).setNeutralButton(
//                getActivity().getString(R.string.okay), null);
//        return builder.create();
//    }
//}
}

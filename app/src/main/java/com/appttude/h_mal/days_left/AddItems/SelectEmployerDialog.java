package com.appttude.h_mal.days_left.AddItems;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class SelectEmployerDialog extends Dialog {

    public SelectEmployerDialog(@NonNull Context context) {
        super(context);
    }

    public SelectEmployerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SelectEmployerDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


}

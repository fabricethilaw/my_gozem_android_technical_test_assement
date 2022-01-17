package com.fabricethilaw.gozem.businesscase;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.fabricethilaw.gozem.R;


public class DialogProgressBar {

    private Context context;
    private Dialog dialog;

    public DialogProgressBar(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.getWindow().setLayout(MATCH_PARENT, MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setCancelable(false);
    }

    public boolean isLoading() {
        if (dialog.getWindow() != null)
            return dialog.isShowing();

        return false;
    }

    public void show() {
        if (dialog.getWindow() == null)
            init();
        dialog.show();
    }

    public void hide() {
        if (dialog != null)
            dialog.cancel();
    }

}

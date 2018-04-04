package aditya.nayanda.shippingmanager.activity.secondary.fragment.dialog.helper;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.FloatRange;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by nayanda on 24/03/18.
 */

public class DialogHelper {

    public static void setDialogWidthPercentage(@FloatRange(from = 0.0, to = 1.0) Float width, Dialog dialog, Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;

        int dialogWindowWidth = (int) (displayWidth * width);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setLayout(dialogWindowWidth, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

}

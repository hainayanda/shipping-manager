package aditya.nayanda.shippingmanager.view.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.model.ListOfJobs;

/**
 * Created by nayanda on 22/03/18.
 */

public class ListOfJobViewHolder {

    private final Context context;
    private final ImageView icon;
    private final TextView shipmentNumber;
    private final TextView totalJobs;

    public ListOfJobViewHolder(Context context, View view) {
        this.context = context;
        icon = view.findViewById(R.id.item_icon);
        shipmentNumber = view.findViewById(R.id.content_shipment_number);
        totalJobs = view.findViewById(R.id.content_total_jobs);
    }

    public void apply(ListOfJobs listOfJobs) {
        String iconName;
        iconName = "other";
        try {
            int id = context.getResources().getIdentifier(iconName,
                    "raw", context.getPackageName());
            InputStream in = context.getResources().openRawResource(id);
            Drawable drawable = Drawable.createFromStream(in, iconName);
            icon.setImageDrawable(drawable);
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        String number = listOfJobs.getShipmentNumber();
        if (number.length() > 20) number = number.substring(0, 15) + "...";
        shipmentNumber.setText(number);
        String remarks = context.getString(R.string.shipment_remarks);
        remarks = remarks.replace("{totalJobs}", Integer.toString(listOfJobs.getTotalJobs()));
        totalJobs.setText(remarks);
    }

}

package aditya.nayanda.shippingmanager.view.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.model.Job;

/**
 * Created by nayanda on 22/03/18.
 */

public class JobViewHolder {

    private final Context context;
    private final ImageView icon;
    private final TextView itemName;
    private final TextView itemDetails;
    private final TextView itemAddress;

    public JobViewHolder(Context context, View view) {
        this.context = context;
        icon = view.findViewById(R.id.item_icon);
        itemName = view.findViewById(R.id.item_name);
        itemDetails = view.findViewById(R.id.item_detail);
        itemAddress = view.findViewById(R.id.item_address_summary);
    }

    public void apply(Job job) {
        String iconName;
        switch (job.getType()) {
            case GAS:
                iconName = "gas";
                break;
            case OIL:
                iconName = "oil";
                break;
            case LUBE:
                iconName = "lube";
                break;
            case PETROCHEMICAL:
                iconName = "petrochemical";
                break;
            default:
                iconName = "other";
                break;
        }
        try {
            int id = context.getResources().getIdentifier(iconName,
                    "raw", context.getPackageName());
            InputStream in = context.getResources().openRawResource(id);
            Drawable drawable = Drawable.createFromStream(in, iconName);
            icon.setImageDrawable(drawable);
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        String name = job.getItemName();
        if (name.length() > 20) name = name.substring(0, 15) + "...";
        itemName.setText(name);
        String details = job.getItemDetail();
        if (details.length() > 40) details = details.substring(0, 35) + "...";
        itemDetails.setText(details);
        String address = job.getAddress();
        if (address.length() > 90) address = address.substring(0, 85) + "...";
        itemAddress.setText(address);
    }

}

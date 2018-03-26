package aditya.nayanda.shippingmanager.view.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.model.ListOfJobs;

/**
 * Created by nayanda on 22/03/18.
 */

public class ListOfJobViewHolder {

    private final Context context;
    private final TextView shipmentNumber;
    private final TextView totalJobs;
    private final ImageView indicator;
    private final FrameLayout expandedBackground;
    private final Drawable drawableExpandedBackground;
    private final Drawable drawableTransparent;

    public ListOfJobViewHolder(Context context, View view) {
        this.context = context;
        shipmentNumber = view.findViewById(R.id.content_shipment_number);
        totalJobs = view.findViewById(R.id.content_total_jobs);
        indicator = view.findViewById(R.id.expand_indicator);
        expandedBackground = view.findViewById(R.id.expanded_background);
        drawableExpandedBackground = context.getDrawable(R.drawable.bg_expand);
        drawableTransparent = context.getDrawable(android.R.color.transparent);
    }

    public void apply(ListOfJobs listOfJobs, boolean isExpanded) {
        String number = listOfJobs.getShipmentNumber();
        if (number.length() > 20) number = number.substring(0, 15) + "...";
        shipmentNumber.setText(number);
        String remarks = context.getString(R.string.shipment_remarks);
        remarks = remarks.replace("{1}", Integer.toString(listOfJobs.getTotalJobs()));
        totalJobs.setText(remarks);
        indicator.setSelected(isExpanded);
        if (isExpanded) expandedBackground.setBackground(drawableExpandedBackground);
        else expandedBackground.setBackground(drawableTransparent);
    }

}

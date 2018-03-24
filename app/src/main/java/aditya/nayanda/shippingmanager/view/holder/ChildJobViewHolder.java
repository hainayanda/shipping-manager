package aditya.nayanda.shippingmanager.view.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.model.Job;

/**
 * Created by nayanda on 24/03/18.
 */

public class ChildJobViewHolder extends JobViewHolder {

    private final Drawable lastChildBackground;
    private final Drawable normalChildBackground;
    private final ConstraintLayout childLayout;

    public ChildJobViewHolder(Context context, View view) {
        super(context, view);
        childLayout = view.findViewById(R.id.child_list_of_jobs_background);
        lastChildBackground = context.getDrawable(R.drawable.bg_last_child_expand);
        normalChildBackground = context.getDrawable(R.drawable.bg_child_expand);
    }

    public void apply(Job job, boolean isLastChild) {
        apply(job);
        if (isLastChild) childLayout.setBackground(lastChildBackground);
        else childLayout.setBackground(normalChildBackground);
    }
}

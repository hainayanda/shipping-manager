package aditya.nayanda.shippingmanager.activities;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by nayanda on 22/03/18.
 */
public class MainActivityTest {
    @Test
    public void setTitleByIndex() throws Exception {
        MainActivity activity = new MainActivity();
        assertTrue(activity.setTitleByIndex(0));
        assertTrue(activity.setTitleByIndex(1));
        assertTrue(activity.setTitleByIndex(2));
        assertTrue(activity.setTitleByIndex(3));
        assertFalse(activity.setTitleByIndex(4));
    }
}
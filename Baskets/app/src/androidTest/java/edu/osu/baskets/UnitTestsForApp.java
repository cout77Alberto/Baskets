package edu.osu.baskets;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UnitTestsForApp {
    @Test
    public void useAppContextTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.osu.baskets", appContext.getPackageName());
    }

    @Test
    public void CreateFoodTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(0, Inventory.get(appContext).GetAllFoodItems().size());
        Inventory.get(appContext).AddItemToBasket(FoodUtils.Spawn("strawberries", 10));
        assertEquals(1, Inventory.get(appContext).GetAllFoodItems().size());
        assertEquals("edu.osu.baskets", appContext.getPackageName());
    }
}

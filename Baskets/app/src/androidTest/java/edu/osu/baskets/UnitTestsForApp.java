package edu.osu.baskets;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import edu.osu.baskets.foods.BaseFood;
import edu.osu.baskets.foods.IFood;
import edu.osu.baskets.recipes.CookingHistory;
import edu.osu.baskets.recipes.RecipeBook;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
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
        FoodUtils.PopulateConstructors(appContext);
        Inventory.get(appContext).AddItemToBasket(FoodUtils.Spawn("strawberries", 10));
        assertEquals(true, Inventory.get(appContext).HasItem("strawberries",10));
    }

    @Test
    public void CookFoodTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        FoodUtils.PopulateConstructors(appContext);
        AccountSingleton accountSingleton = AccountSingleton.get();
        assertEquals(0, accountSingleton.getCalories());
        Inventory.get(appContext).AddItemToBasket(FoodUtils.Spawn("strawberries", 10));
        Inventory.get(appContext).AddItemToBasket(FoodUtils.Spawn("water", 10));
        assertEquals(true, Inventory.get(appContext).HasItem("strawberries",10));
        RecipeBook.get(appContext).getRecipes().get(0).make(appContext);
        assertEquals(false, Inventory.get(appContext).HasItem("strawberries",10));
        assertEquals(1, CookingHistory.get(appContext).getRecipes().size());
        assertEquals(120, accountSingleton.getCalories());
    }

    @Test
    public void CheckScoreTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        FoodUtils.PopulateConstructors(appContext);

        IFood strawberries = FoodUtils.Spawn("strawberries", 10);
        Inventory.get(appContext).AddItemToBasket(strawberries);

        assertEquals(false, Inventory.get(appContext).GetFridgeItems().contains(strawberries));
        Inventory.get(appContext).MoveToFridge(0);
        assertEquals(true, Inventory.get(appContext).GetFridgeItems().contains(strawberries));

    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(SplashActivity.class);
    @Test
    public void ViewAccountTest() {
        Espresso.onView(withId(R.id.account)).perform(click());

    }
}
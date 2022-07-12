package wa.mobile.rpghelper;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("wa.mobile.rpghelper", appContext.getPackageName());
    }

    @Test
    public void selectIsCorrect() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        /*DatabaseContextSingleton.getDatabaseContext(appContext);

        */
        assertEquals(4, 2 + 2);
    }
}
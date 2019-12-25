package developer.android.com.enlightme

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@MediumTest
@RunWith(AndroidJUnit4::class)
class MainFragmentTest{
    @Rule @JvmField
    var mActivityRule =
        ActivityTestRule(MainActivity::class.java)
    @Test
    fun createDebateTransition(){
        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)
        // Create a graphical FragmentScenario for the TitleScreen
        var mainScenario = launchFragmentInContainer<MainFragment>(Bundle(), R.style.Theme_MaterialComponents)
        // Set the NavController property on the fragment
        mainScenario.onFragment { fragment ->  Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        // Verify that performing a click "créer" prompt the correct Navigation action
        onView(ViewMatchers.withId(R.id.nav_button_creer)).perform(ViewActions.click())
        verify(mockNavController).navigate(R.id.action_mainFragment_to_create1Fragment)
    }
    // @Test
    // fun createDebateTransition_1(){
    //     // Create a mock NavController
    //     val mockNavController = mock(NavController::class.java)
    //     // Create a graphical FragmentScenario for the TitleScreen
    //     var create1Scenario = launchFragmentInContainer<Create1Fragment>()
    //     // Set the NavController property on the fragment
    //     create1Scenario.onFragment { fragment ->  Navigation.setViewNavController(fragment.requireView(), mockNavController)
    //     }
    //     // Verify that performing a click "créer" prompt the correct Navigation action
    //     onView(ViewMatchers.withId(R.id.nav_button_creer)).perform(ViewActions.click())
    //     verify(mockNavController).navigate(R.id.action_mainFragment_to_create1Fragment)
    // }
}
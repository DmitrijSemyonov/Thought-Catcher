package com.squalgamesstudio.thoughtcatcher

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.squalgamesstudio.thoughtcatcher.presentation.ResourcesActivity
import com.squalgamesstudio.thoughtcatcher.presentation.ThoughtAdapter
import com.squalgamesstudio.thoughtcatcher.repository.ThoughtRepository
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class ResourcesActivityTest {

    private lateinit var scenario: ActivityScenario<ResourcesActivity>

    private lateinit var repository: ThoughtRepository
    @Before
    fun init(){
        repository = FakeThoughtRepository()
        ServiceLocator.thoughtRepository = repository
        scenario = ActivityScenario.launch(ResourcesActivity::class.java)
        //scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun testOnClickAppearingDialog(){
        onView(withId(R.id.floatingActionButton1)).check(matches(isDisplayed()))
        onView(withId(R.id.floatingActionButton1)).perform(click())

        onView(withId(R.id.buttonEnter)).check(matches(isDisplayed()))
        onView(withId(R.id.editText)).check(matches(isDisplayed()))
    }
    @Test
    fun testAddThought() {
        onView(withId(R.id.floatingActionButton1)).perform(click())
        onView(withId(R.id.editText)).perform(ViewActions.typeText("Test1"))
        onView(withId(R.id.buttonEnter)).perform(click())
        onView(withText("Test1")).check(matches(isDisplayed()))
    }
    @Test
    fun deleteThought() {
        val rv = getActivityInstance()?.findViewById<RecyclerView>(R.id.recyclerView1)
        val itemCount = rv?.adapter?.itemCount

        onView(withId(R.id.recyclerView1)).perform(RecyclerViewActions.actionOnItemAtPosition<ThoughtAdapter.ViewHolder>(2, MyViewAction.clickChildViewWithId(R.id.deleteButton)));

        assert(itemCount != null)
        if (rv != null && itemCount != null) {
            assert(rv.adapter?.itemCount == itemCount - 1)
        }
    }

    private fun getActivityInstance(): Activity? {
        val currentActivity = arrayOf<Activity?>(null)
        getInstrumentation().runOnMainSync(Runnable {
            val resumedActivity: Collection<Activity> =
                ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED)
            val it = resumedActivity.iterator()
            currentActivity[0] = it.next()
        })
        return currentActivity[0]
    }
}
object MyViewAction {
    fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }
}
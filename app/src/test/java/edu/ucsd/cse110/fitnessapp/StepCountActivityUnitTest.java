package edu.ucsd.cse110.fitnessapp;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.fitnessapp.fitness.FitnessService;
import edu.ucsd.cse110.fitnessapp.fitness.FitnessServiceFactory;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class StepCountActivityUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private long nextStepCount;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), StepCountActivity.class);
        intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
    }

    @Test
    public void testUpdateStepsButton() {
        nextStepCount = 1337;

        ActivityScenario<StepCountActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textSteps = activity.findViewById(R.id.textSteps);
            Button btnUpdateSteps = activity.findViewById(R.id.buttonUpdateSteps);
            assertThat(textSteps.getText().toString()).isEqualTo("steps will be shown here");
            btnUpdateSteps.performClick();
            assertThat(textSteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
        });
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private StepCountActivity stepCountActivity;

        public TestFitnessService(StepCountActivity stepCountActivity) {
            this.stepCountActivity = stepCountActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            stepCountActivity.setStepCount(nextStepCount);
        }
    }
}
package com.example.lemonade

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.lemonade.ui.theme.LemonadeTheme

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

class LemonadeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun makeLemonadeTest(){
     //Start the app
        composeTestRule.setContent {
            LemonadeTheme {
                MakeLemonade()
            }
        }

        maxSqueezes = 2

        // Start State - 1
        composeTestRule.onNodeWithContentDescription("Lemon tree").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap the lemon tree to select a lemon").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Lemon tree").performClick()

        // 2
        assertEquals(lemonSqueezes, 0)
        composeTestRule.onNodeWithContentDescription("Lemon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Keep tapping the lemon to squeeze it").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Lemon").performClick()
        assertEquals(lemonSqueezes, 1)
        // second click on 2
        composeTestRule.onNodeWithContentDescription("Lemon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Keep tapping the lemon to squeeze it").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Lemon").performClick()
        // reset clicks
        assertEquals(lemonSqueezes, 0)

        // 3
        composeTestRule.onNodeWithContentDescription("Glass of lemonade").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap the lemonade to drink it").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Glass of lemonade").performClick()

        // 4
        composeTestRule.onNodeWithContentDescription("Empty glass").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap the empty glass to start again").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Empty glass").performClick()

        // restart
        composeTestRule.onNodeWithContentDescription("Lemon tree").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap the lemon tree to select a lemon").assertIsDisplayed()
    }

}
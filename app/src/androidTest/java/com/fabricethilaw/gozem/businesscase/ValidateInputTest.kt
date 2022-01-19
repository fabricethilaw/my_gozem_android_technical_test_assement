package com.fabricethilaw.gozem.businesscase

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValidateInputTest {

    @Test
    fun validateName_Empty_text_is_not_valid() {
        val name = ""
        assertFalse("Empty text is not valid name", Validator.validateName(name))
    }

    @Test
    fun validateName_Blank_text_is_not_valid() {
        val name = "     "
        assertFalse("Blank text is not valid name", Validator.validateName(name))
    }

    @Test
    fun validateName_short_text_is_valid() {
        val name = "Alpha13"
        assertTrue("Short text is valid name", Validator.validateName(name))
    }

    @Test
    fun validateEmail_Blank_text_is_not_valid() {
        val email = ""
        assertFalse("Blank text is not valid email", Validator.validateEmail(email))
    }

    @Test
    fun validateEmail_Empty_text_is_not_valid() {
        val email = " "
        assertFalse("Blank text is not valid email", Validator.validateEmail(email))
    }

    @Test
    fun validateEmail_incorrect_email_is_not_valid() {
        val email = "example@123_"
        assertFalse("Incorrect email is not valid email", Validator.validateEmail(email))
    }

    @Test
    fun validateEmail_well_formatted_correct_email_is_valid() {
        val email = "example@yopmail.com"
        assertTrue(Validator.validateEmail(email))
    }

    @Test
    fun validatePassword_blank_text_is_not_valid() {
        val password = ""
        assertFalse(Validator.validatePassword(password))
    }

    @Test
    fun validatePassword_too_short_text_is_not_valid() {
        val password = "s0"
        assertFalse(Validator.validatePassword(password))
    }

    @Test
    fun validatePassword_long_short_text_is_valid() {
        val password = "s018LpT0BmD_"
        assertTrue(Validator.validatePassword(password))
    }
}
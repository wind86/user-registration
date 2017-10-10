package com.user.registration.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.user.registration.controller.user.UserControllerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   UserControllerTest.class
})
public class ControllerIntegrationTests {

}
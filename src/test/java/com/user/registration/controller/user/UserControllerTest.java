package com.user.registration.controller.user;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.user.registration.Application;
import com.user.registration.dto.user.UserDto;
import com.user.registration.dto.validation.FieldErrorDto;
import com.user.registration.dto.validation.ValidationErrorDto;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// re-create context after each test (good for running with small number of test)
// but for production testing something like dbunit should be used to restore db state after each test
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

	private static final String USER_URL = "/api/user/";

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	@Qualifier("validationMessageSource")
	private MessageSource messageSource;

	@Before
	public void setUp() {
		mockMvc(webAppContextSetup(context).build());
	}

	@Test
	public void testCreatingValidUser() throws JsonParseException, JsonMappingException, IOException {
		UserDto userDto = createDefaultUser();

		MockMvcResponse response = sendCreateUserRequest(userDto);
		response.then().statusCode(HttpServletResponse.SC_OK);

		UserDto storedUser = JSON_MAPPER.readValue(response.asString(), UserDto.class);

		assertNotNull(storedUser);
		assertNotNull(storedUser.getId());

		assertEquals(userDto.getName(), storedUser.getName());
		assertEquals(userDto.getEmail(), storedUser.getEmail());
		assertEquals(userDto.getPassword(), storedUser.getPassword());
	}

	@Test
	public void testSavingDuplicatedUser() throws JsonParseException, JsonMappingException, IOException {
		UserDto userDto = createDefaultUser();

		MockMvcResponse response = sendCreateUserRequest(userDto);
		response.then().statusCode(HttpServletResponse.SC_OK);

		MockMvcResponse response2 = sendCreateUserRequest(userDto);
		response2.then().statusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		@SuppressWarnings("unchecked")
		Map<String, String> responseMap = (Map<String, String>) JSON_MAPPER.readValue(response2.asString(), HashMap.class);
		assertEquals("User with following name or email already exists", responseMap.get("message"));
	}

	@Test
	public void testSavingBlankUser() throws Exception {
		List<FieldErrorDto> fieldErrors = testInvalidUserSaving(new UserDto());
		assertEquals(5, fieldErrors.size());
	}

	@Test
	public void testSavingUserWithInvalidUserNameLength() throws Exception {
		UserDto userDto = createDefaultUser();
		userDto.setName("test");

		testInvalidUserFieldSaving(userDto, "name", "user.name");
	}

	@Test
	public void testSavingUserWithInvalidUserNameCharacters() throws Exception {
		UserDto userDto = createDefaultUser();
		userDto.setName("te-st");

		testInvalidUserFieldSaving(userDto, "name", "user.name");
	}
	
	@Test
	public void testSavingUserWithInvalidEmail() throws Exception {
		UserDto userDto = createDefaultUser();
		userDto.setEmail("test");

		testInvalidUserFieldSaving(userDto, "email", "user.email");
	}

	@Test
	public void testSavingUserWithPasswordWithoutAnyNumber() throws Exception {
		testInvalidUserPasswordSaving("testTest");
	}

	@Test
	public void testSavingUserWithPasswordWithoutAnyUpperCaseCharacter() throws Exception {
		testInvalidUserPasswordSaving("test1abc");
	}

	@Test
	public void testSavingUserWithPasswordWithoutAnyLowerCaseCharacter() throws Exception {
		testInvalidUserPasswordSaving("TEST1ABC");
	}

	@Test
	public void testSavingUserWithPasswordLengthLessThanEigthCharacters() throws Exception {
		testInvalidUserPasswordSaving("test1T");
	}

	private void testInvalidUserPasswordSaving(String invalidPassword) throws Exception {
		UserDto userDto = createDefaultUser();
		userDto.setPassword(invalidPassword);

		testInvalidUserFieldSaving(userDto, "password", "user.password");
	}
	
	private void testInvalidUserFieldSaving(UserDto user, String invalidField, String expectedMessageCode) throws Exception {
		String expectedMessage = messageSource.getMessage(expectedMessageCode, null, LocaleContextHolder.getLocale());
		
		List<FieldErrorDto> fieldErrors = testInvalidUserSaving(user);
		assertEquals(1, fieldErrors.size());

		FieldErrorDto fieldError = fieldErrors.get(0);

		assertEquals(expectedMessage, fieldError.getMessage());
		assertEquals(invalidField, fieldError.getField());
	}

	private static List<FieldErrorDto> testInvalidUserSaving(UserDto user)
			throws JsonParseException, JsonMappingException, IOException {
		
		MockMvcResponse response = sendCreateUserRequest(user);
		response.then().statusCode(HttpServletResponse.SC_BAD_REQUEST);

		ValidationErrorDto validationErrors = JSON_MAPPER.readValue(response.asString(), ValidationErrorDto.class);
		return validationErrors.getFieldErrors();
	}

	private static MockMvcResponse sendCreateUserRequest(UserDto user) {
		return given().body(user)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.post(USER_URL);
	}

	private static UserDto createDefaultUser() {
		String name = RandomStringUtils.randomAlphabetic(10);
		String email = "some@email.com";
		String password = RandomStringUtils.randomAlphanumeric(7) + "A1z";

		return createUserDto(name, email, password);
	}

	private static UserDto createUserDto(String name, String email, String password) {
		UserDto user = new UserDto();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		return user;
	}
}

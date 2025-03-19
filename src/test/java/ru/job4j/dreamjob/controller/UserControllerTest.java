package ru.job4j.dreamjob.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void init() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void whenGetLoginPageWhenOk() {
        assertThat(userController.getLoginPage()).isEqualTo("users/login");
    }

    @Test
    void whenGetRegisterPageWhenOk() {
        assertThat(userController.getRegistrationPage()).isEqualTo("users/register");
    }

    @Test
    void whenGetLogoutWhenSessionInvalidatedAndRedirectToLoginPage() {
        HttpSession session = mock(HttpSession.class);
        doNothing().when(session).invalidate();
        var view = userController.logout(session);
        assertThat(view).isEqualTo("redirect:/users/login");
        verify(session).invalidate();
    }

    @Test
    void whenPostLoginWhenInsertUserToSessionAndRedirectToVacanciesPage() {
        User expectedUser = new User(1, "email", "name", "password");
        Model model = new ConcurrentModel();
        HttpServletRequest request = new MockHttpServletRequest();
        var emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        var pwdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        doReturn(Optional.of(expectedUser)).when(userService).findByEmailAndPassword(emailArgumentCaptor.capture(), pwdArgumentCaptor.capture());

        User requestUser = new User();
        requestUser.setEmail("email");
        requestUser.setPassword("password");
        var view = userController.loginUser(requestUser, model, request);

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(emailArgumentCaptor.getValue()).isEqualTo(requestUser.getEmail());
        assertThat(pwdArgumentCaptor.getValue()).isEqualTo(requestUser.getPassword());
        assertThat(Objects.requireNonNull(request.getSession()).getAttribute("user")).isEqualTo(expectedUser);
    }

    @Test
    void whenPostLoginAndUserNotFoundWhenErrorAndRedirectToLoginPage() {
        Model model = new ConcurrentModel();
        HttpServletRequest request = new MockHttpServletRequest();
        var emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        var pwdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        doReturn(Optional.empty()).when(userService).findByEmailAndPassword(emailArgumentCaptor.capture(), pwdArgumentCaptor.capture());

        User requestUser = new User();
        requestUser.setEmail("email");
        requestUser.setPassword("password");
        var view = userController.loginUser(requestUser, model, request);

        assertThat(view).isEqualTo("users/login");
        assertThat(emailArgumentCaptor.getValue()).isEqualTo(requestUser.getEmail());
        assertThat(pwdArgumentCaptor.getValue()).isEqualTo(requestUser.getPassword());
        assertThat(model.getAttribute("error")).isEqualTo("Почта или пароль введены неверно");
    }

    @Test
    void whenPostRegisterWhenRedirectToRoot() {
        Model model = new ConcurrentModel();
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        doReturn(Optional.of(mock(User.class))).when(userService).save(userArgumentCaptor.capture());

        User requestUser = new User(1, "email", "name", "password");

        var view = userController.register(requestUser, model);

        assertThat(view).isEqualTo("redirect:/");
        assertThat(userArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(requestUser);
    }

    @Test
    void whenPostRegisterAndOptionalEmptyWhenErrorRedirectToRoot() {
        Model model = new ConcurrentModel();
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        doReturn(Optional.empty()).when(userService).save(userArgumentCaptor.capture());

        User requestUser = new User(1, "email", "name", "password");

        var view = userController.register(requestUser, model);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("Пользователь с такой почтой уже существует");
        assertThat(userArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(requestUser);
    }

}
package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class UserControllerTest {

    private UserController userController;

    private UserService userService;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRegister() {
        var user1 = new User(1, "mail", "max", "1234");
        var user2 = new User(2, "google", "xam", "12345");

        var model = new ConcurrentModel();
        var view = userController.register(user1, model);

        assertThat(view).isEqualTo("redirect:/index");

    }
}
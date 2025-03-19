package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class IndexControllerTest {

    private IndexController controller;

    @BeforeEach
    void init() {
        controller = new IndexController();
    }

    @Test
    void whenGetIndexThenReturnIndexPage() {
        assertThat(controller.getIndex()).isEqualTo("index");
    }



}
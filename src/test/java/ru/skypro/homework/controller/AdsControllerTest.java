package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.ImageService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.ConstantForTests.*;

@WebMvcTest(controllers = AdsController.class)
@WithMockUser(username = "user@mail.ru", password = "password", roles = "USER")
class AdsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AdsRepository adsRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private Authentication authentication;
    @MockBean
    private AdsService adsService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private ImageService imageService;
    @MockBean
    private AdsMapper adsMapper;

    @InjectMocks
    private AdsController adsController;

    @Test
    void getALLAds() throws Exception {
        when(adsRepository.findAll()).thenReturn(List.of(ADS_1, ADS_2));
        when(adsMapper.listAdsToListAdsDto(anyList())).thenReturn(List.of(ADS_DTO_1, ADS_DTO_2));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void removeAds() throws Exception {
    }

    @Test
    void getAds() {
    }

    @Test
    void updateAds() {
    }

    @Test
    void getAdsMe() {
    }

    @Test
    void getAllCommentsOfAds() {
    }

    @Test
    void addAdsComment() {
    }

    @Test
    void updateAdsComment() {
    }

    @Test
    void deleteAdsComment() {
    }

    @Test
    void getAdsComment() {
    }

    @Test
    void updateAdsImage() {
    }
}
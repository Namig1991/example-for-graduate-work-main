package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.impl.ImageService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Контррллер для работы с объявлениями.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequestMapping("/ads")
public class AdsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdsController.class);
    private final AdsService adsService;
    private final ImageService imageService;

    /**
     * Получение всех объявлений.
     */
    @Operation(
            summary = "Получение всех объявлений", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping
    public ResponseEntity<ResponseWrapperAdsDto> getALLAds() {
        LOGGER.info("Was invoked method of AdsController for get All Ads.");
        return adsService.getAllAds();
    }

    /**
     * Добавление нового объявления.
     *
     * @param createAdsDto - входная форма.
     * @param file         - изображение к объявлению.
     */
    @Operation(
            summary = "Добавление нового объявления", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AdsDto> addAds(@Valid
                                         @Parameter(description = "Передаем заполненное объявление")
                                         @RequestPart("properties") CreateAdsDto createAdsDto,
                                         @Parameter(description = "Передаем изображение к объявлению")
                                         @RequestPart("image") MultipartFile file) throws IOException {
        LOGGER.info("Was invoked method of AdsController for save Ads.");
        return adsService.saveAds(createAdsDto, file);
    }

    @Operation(
            summary = "Удаление объявления по ID", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("@adsServiceImpl.getAdsById(#id).getBody().getEmail()" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<AdsDto> removeAds(
            @Parameter(description = "Передает ID для удаления")
            @PathVariable Integer id) {
        LOGGER.info("Was invoked method of AdsController for delete Ads by id.");
        return adsService.removeAdsById(id.longValue());
    }


    @Operation(
            summary = "Получение объявления по идентификатору", description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = FullAdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDto> getAds(
            @Parameter(description = "Передаем ID объявления")
            @PathVariable Integer id) {
        LOGGER.info("Was invoked method of AdsController for get Ads by id.");
        return adsService.getAdsById(id.longValue());
    }

    @Operation(
            summary = "Редактирование объявления", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("@adsServiceImpl.getAdsById(#id).getBody().getEmail()" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(
            @Parameter(description = "Передаем ID объявления")
            @PathVariable Integer id,
            @RequestBody AdsDto adsDto) {
        LOGGER.info("Was invoked method of AdsController for update Ads.");
        return adsService.updateAds(id.longValue(), adsDto);
    }

    @Operation(
            summary = "Получение своих объявлений", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication authentication) {
        LOGGER.info("Was invoked method of AdsController for get Ads of current user.");
        return adsService.getAdsMe(authentication);
    }

    @Operation(
            summary = "/ads/{ad_pk}/comment", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsCommentDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )

    @GetMapping("/{adsPk}/comment")
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAdsComments(
            @Parameter(description = "Передаем первичный ключ обявления")
            @PathVariable Integer adsPk) {
        return ResponseEntity.ok(new ResponseWrapperAdsCommentDto());
    }

    @Operation(summary = "/ads/{ad_pk}/comment", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsCommentDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @PostMapping("/{adsPk}/comment")
    public ResponseEntity<AdsCommentDto> addAdsComment(
            @Parameter(description = "Передаем заполненный комментарий")
            @RequestBody AdsCommentDto adsCommentDto,
            @Parameter(description = "Передаем первичный ключ обявления")
            @PathVariable Integer adsPk
    ) {
        return ResponseEntity.ok(new AdsCommentDto());
    }

    @Operation(summary = "/ads/{ad_pk}/comment/{id}", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PatchMapping("/{adsPk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsComment(
            @PathVariable Integer adsPk,
            @PathVariable Integer id,
            @RequestBody AdsCommentDto adsCommentDto
    ) {
        return ResponseEntity.ok(new AdsCommentDto());
    }

    @Operation(summary = "/ads/{ad_pk}/comment/{id}", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })

    @DeleteMapping("/{adsPk}/comment/{id}")
    public ResponseEntity<Void> deleteAdsComment(
            @Parameter(description = "Передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "Передаем ID комментария")
            @PathVariable Integer id) {
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "/ads/{ad_pk}/comment/{id}", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{adsPk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> getAdsComment(
            @Parameter(description = "Передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "Передаем ID комментария")
            @PathVariable Integer id) {
        return ResponseEntity.ok(new AdsCommentDto());
    }

    @Operation(
            summary = "updateAdsImage", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAdsImage(
            @Parameter(description = "Передаем ID объявления")
            @PathVariable Integer id,
            @RequestBody Authentication authentication,
            @Parameter(description = "Передаем новое изображение")
            @RequestPart(value = "image") @Valid MultipartFile image) {
        return ResponseEntity.ok().body("Изображение успешно обновлено.");
    }

    /**
     * "Метод получения изображения по идентификатору.
     */
    @Operation(summary = "/ads/getImage/{imageId}",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping(value = "/getImage/{imageId}")
    public void getImage(@PathVariable Long imageId, HttpServletResponse response) {
        Images image = imageService.getImageById(imageId);
        Path path = Path.of(image.getFilePath());
        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream()
        ) {
            response.setStatus(200);
            response.setContentType(image.getMediaType());
            response.setContentLength(Math.toIntExact(image.getFileSize()));
            is.transferTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

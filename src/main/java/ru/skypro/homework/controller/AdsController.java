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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.impl.AdsServiceImpl;
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
@RequestMapping("/ads")
public class AdsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdsController.class);
    private final AdsService adsService;
    private final ImageService imageService;

    @Operation(
            summary = "getALLAds", description = "", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )

    @GetMapping
    public ResponseEntity<ResponseWrapperAdsDto> getALLAds(){
        return ResponseEntity.ok(new ResponseWrapperAdsDto());
    }

    /**
     * Добавление нового объявления.
     * @param createAdsDto - входная форма.
     * @param file - изображение к объвлению.
     */
    @Operation(
            summary = "addAds", description = "", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(@Valid
            @Parameter(description = "Передаем заполненное объявление")
            @RequestPart (name = "properties") CreateAdsDto createAdsDto,
            @Parameter(description = "Передаем изображение к объявлению")
            @RequestPart("image")MultipartFile file) throws IOException {
        LOGGER.info("Was invoked method of AdsController for save Ads.");
        return adsService.saveAds(createAdsDto, file);
    }

    @Operation(
            summary = "removeAds/{id}", description = "", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<AdsDto> removeAds(
            @Parameter(description = "Пердает ID для удаления") @PathVariable Integer id) {
        return ResponseEntity.status(204).build();
    }

    @Operation(
            summary = "getAds/{id}", description = "", tags={ "Объявления" },
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
            @Parameter(description = "Передаем ID объявления") @PathVariable Integer id){
        return ResponseEntity.ok(new FullAdsDto());
    }

    @Operation(
            summary = "updateAds/{id}", description = "", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )

    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(
            @Parameter(description = "Передаем ID объявления")
            @PathVariable Integer id,
            @RequestBody AdsDto adsDto){
        return ResponseEntity.ok(new AdsDto());
    }

    @Operation(
            summary = "getAds/me", description = "", tags={ "Объявления" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(
            @Parameter(description = "true или false") @RequestParam(required = false) Boolean authenticated,
            @Parameter(description = "authorities[0].authority") @RequestParam(required = false) String authority,
            @Parameter(description = "credentials") @RequestParam(required = false) Object credentials,
            @Parameter(description = "details") @RequestParam(required = false) Object details,
            @Parameter(description = "principal") @RequestParam(required = false) Object principal
    ) {
        return ResponseEntity.ok(new ResponseWrapperAdsDto());
    }

    @Operation(
            summary = "/ads/{ad_pk}/comment", description = "", tags={ "Объявления" },
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

    @Operation(summary = "/ads/{ad_pk}/comment", description = "", tags={ "Объявления" },
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

    @Operation(summary = "/ads/{ad_pk}/comment/{id}", description = "", tags={ "Объявления" },
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

    @Operation(summary = "/ads/{ad_pk}/comment/{id}", description = "", tags={ "Объявления" },
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

    @Operation(summary = "/ads/{ad_pk}/comment/{id}", description = "", tags={ "Объявления" },
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
            summary = "updateAdsImage", description = "", tags={ "Объявления" },
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
            @RequestPart(value = "image") @Valid MultipartFile image){
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
        Images image = imageService.getImage(imageId);
        Path path = Path.of(image.getFilePath());
        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream()
                ){
            response.setStatus(200);
            response.setContentType(image.getMediaType());
            response.setContentLength(Math.toIntExact(image.getFileSize()));
            is.transferTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

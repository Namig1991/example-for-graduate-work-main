package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

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

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(@Valid
            @Parameter(description = "Передаем заполненное объявление")
            @RequestPart (name = "properties") CreateAdsDto createAdsDto,
            @Parameter(description = "Передаем изображение к объявлению")
            @RequestPart("image")MultipartFile file){
        return ResponseEntity.ok(new AdsDto());
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

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

}

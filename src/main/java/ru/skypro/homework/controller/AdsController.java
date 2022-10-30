package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    @Operation(
            summary = "getALLAds",
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
        log.info("Метод показа всех объявялений.");
        return ResponseEntity.ok(new ResponseWrapperAdsDto());
    }

    @Operation(
            summary = "addAds",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )

    @PostMapping
    public ResponseEntity<CreateAdsDto> addAds(
            @Parameter(description = "Передаем заполненное объявление") @RequestBody AdsDto adsDto ){
        log.info("Метод сохраняет новое объявление.");
        return ResponseEntity.ok(new CreateAdsDto());
    }

    @Operation(
            summary = "removeAds/{id}",
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
        log.info("Метод удаления объявления по переданному ID");
        return ResponseEntity.status(204).build();
    }

    @Operation(
            summary = "getAds/{id}",
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
        log.info("Метод возвращает объявление по ID");
        return ResponseEntity.ok(new FullAdsDto());
    }

    @Operation(
            summary = "updateAds/{id}",
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
            @Parameter(description = "Передаем ID щбъявления") @PathVariable Integer id, @RequestBody AdsDto adsDto){
        log.info("Метод изменения объявления.");
        return ResponseEntity.ok(new AdsDto());
    }

    @Operation(
            summary = "getAds/me",
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
        log.info("Метод получения объявлений определенного пользователя.");
        return ResponseEntity.ok(new ResponseWrapperAdsDto());
    }

    @Operation(
            summary = "/ads/{ad_pk}/comment",
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
        log.info("Метод получения всех комментариев по первичному ключу.");
        return ResponseEntity.ok(new ResponseWrapperAdsCommentDto());
    }

    @Operation(summary = "/ads/{ad_pk}/comment",
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
        log.info("Метод добавляет новый комментарий.");
        return ResponseEntity.ok(new AdsCommentDto());
    }

    @Operation(summary = "/ads/{ad_pk}/comment/{id}",
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
        log.info("Метод изменения комментария.");
        return ResponseEntity.ok(new AdsCommentDto());
    }

    @Operation(summary = "/ads/{ad_pk}/comment/{id}",
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
        log.info("Метод удаляет комментарий.");
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "/ads/{ad_pk}/comment/{id}",
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
        log.info("Метод получения комментария по ID");
        return ResponseEntity.ok(new AdsCommentDto());
    }


}

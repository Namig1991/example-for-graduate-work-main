package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.model.Comment;


import java.util.List;

@Mapper(componentModel = "string")
public interface CommentMapper {

    @Mapping(target = "pk", source = "id")
    AdsCommentDto toAdsCommentDto(Comment comment);

    @Mapping(target = "id",source = "pk")
    Comment adsCommentDtoToComment(AdsCommentDto adsCommentDto);

    List<AdsCommentDto> listCommentsToListAdsCommentDto(List<Comment> commentsList);

}

package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Users;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdsMapper {

    @Mapping(target = "pk", source = "id")
    AdsDto toAdsDto(Ads ads);

    @Mapping(target = "id", source = "pk")
    Ads toAds(AdsDto adsDto);

    @Mapping(target = "id", source = "pk")
    Ads createAdsDtoToAds(CreateAdsDto createAdsDto);

    @Mapping(target = "pk", source = "ads.id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.username")
    FullAdsDto toFullAdsDto(Ads ads, Users user);

    List<AdsDto> listAdsToListAdsDto(List<Ads> adsList);
}

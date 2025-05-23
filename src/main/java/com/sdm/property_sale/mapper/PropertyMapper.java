package com.sdm.property_sale.mapper;

import com.sdm.property_sale.dto.PropertyDto;
import com.sdm.property_sale.dto.PropertyImageDto;
import com.sdm.property_sale.entity.Property;
import com.sdm.property_sale.entity.PropertyImage;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PropertyMapper {

    public PropertyDto toDto(Property property) {
        if (property == null) {
            return null;
        }

        PropertyDto dto = new PropertyDto();
        dto.setId(property.getId());
        dto.setName(property.getName());
        dto.setDescription(property.getDescription());
        dto.setLandSize(property.getLandSize());
        dto.setPricePerPerch(property.getPricePerPerch());
        dto.setFullPrice(property.getFullPrice());
        dto.setLocation(property.getLocation());
        dto.setMainCity(property.getMainCity());
        dto.setDistanceToMainCity(property.getDistanceToMainCity());
        dto.setOwnerName(property.getOwnerName());
        dto.setOwnerMobile(property.getOwnerMobile());
        dto.setOwnerAddress(property.getOwnerAddress());
        dto.setOwnerPrice(property.getOwnerPrice());
        dto.setStatus(property.getStatus());
        dto.setUserId(property.getUserId());
        dto.setCreatedAt(property.getCreatedAt());
        dto.setUpdatedAt(property.getUpdatedAt());

        if (property.getImages() != null) {
            dto.setImages(property.getImages().stream()
                    .map(this::toImageDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Property toEntity(PropertyDto dto) {
        if (dto == null) {
            return null;
        }

        Property property = new Property();
        property.setId(dto.getId());
        property.setName(dto.getName());
        property.setDescription(dto.getDescription());
        property.setLandSize(dto.getLandSize());
        property.setPricePerPerch(dto.getPricePerPerch());
        property.setFullPrice(dto.getFullPrice());
        property.setLocation(dto.getLocation());
        property.setMainCity(dto.getMainCity());
        property.setDistanceToMainCity(dto.getDistanceToMainCity());
        property.setOwnerName(dto.getOwnerName());
        property.setOwnerMobile(dto.getOwnerMobile());
        property.setOwnerAddress(dto.getOwnerAddress());
        property.setOwnerPrice(dto.getOwnerPrice());
        property.setStatus(dto.getStatus());
        property.setUserId(dto.getUserId());

        return property;
    }

    public PropertyImageDto toImageDto(PropertyImage image) {
        if (image == null) {
            return null;
        }

        PropertyImageDto dto = new PropertyImageDto();
        dto.setId(image.getId());
        dto.setPath(image.getPath());
        dto.setType(image.getType());
        dto.setPropertyId(image.getProperty().getId());

        return dto;
    }

    public PropertyImage toImageEntity(PropertyImageDto dto) {
        if (dto == null) {
            return null;
        }

        PropertyImage image = new PropertyImage();
        image.setId(dto.getId());
        image.setPath(dto.getPath());
        image.setType(dto.getType());

        return image;
    }

    public List<PropertyDto> toDtoList(List<Property> properties) {
        return properties.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
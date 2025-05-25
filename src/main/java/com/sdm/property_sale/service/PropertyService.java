package com.sdm.property_sale.service;

import com.sdm.property_sale.dto.PropertyDto;
import com.sdm.property_sale.dto.PropertyImageDto;
import com.sdm.property_sale.entity.Property;
import com.sdm.property_sale.entity.PropertyImage;
import com.sdm.property_sale.enums.PropertyStatus;
import com.sdm.property_sale.mapper.PropertyMapper;
import com.sdm.property_sale.repository.PropertyRepository;
import com.sdm.property_sale.repository.PropertyImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final PropertyMapper propertyMapper;

    public PropertyService(PropertyRepository propertyRepository, PropertyImageRepository propertyImageRepository, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.propertyImageRepository = propertyImageRepository;
        this.propertyMapper = propertyMapper;
    }

    public PropertyDto saveProperty(PropertyDto propertyDto) {
        Property property = propertyMapper.toEntity(propertyDto);
        Property savedProperty = propertyRepository.save(property);

        // Save images if provided
        if (propertyDto.getImages() != null && !propertyDto.getImages().isEmpty()) {
            for (PropertyImageDto imageDto : propertyDto.getImages()) {
                PropertyImage image = propertyMapper.toImageEntity(imageDto);
                image.setProperty(savedProperty);
                propertyImageRepository.save(image);
            }
        }

        return propertyMapper.toDto(propertyRepository.findById(savedProperty.getId()).orElse(savedProperty));
    }

    public PropertyDto updateProperty(Long propertyId, PropertyDto propertyDto) {
        Optional<Property> existingProperty = propertyRepository.findById(propertyId);

        if (existingProperty.isEmpty()) {
            throw new RuntimeException("Property not found or unauthorized access");
        }

        Property property = getProperty(propertyDto, existingProperty);

        Property savedProperty = propertyRepository.save(property);

        return propertyMapper.toDto(propertyRepository.findById(savedProperty.getId()).orElse(savedProperty));
    }

    private static Property getProperty(PropertyDto propertyDto, Optional<Property> existingProperty) {
        Property property = existingProperty.get();

        // Update property details
        property.setName(propertyDto.getName());
        property.setDescription(propertyDto.getDescription());
        property.setLandSize(propertyDto.getLandSize());
        property.setPricePerPerch(propertyDto.getPricePerPerch());
        property.setFullPrice(propertyDto.getFullPrice());
        property.setLocation(propertyDto.getLocation());
        property.setMainCity(propertyDto.getMainCity());
        property.setDistanceToMainCity(propertyDto.getDistanceToMainCity());
        property.setOwnerName(propertyDto.getOwnerName());
        property.setOwnerMobile(propertyDto.getOwnerMobile());
        property.setOwnerAddress(propertyDto.getOwnerAddress());
        property.setOwnerPrice(propertyDto.getOwnerPrice());

        if (propertyDto.getStatus() != null) {
            property.setStatus(propertyDto.getStatus());
        }
        return property;
    }

    public PropertyDto updatePropertyStatus(Long propertyId, PropertyStatus status, UUID userId) {
        Optional<Property> existingProperty = propertyRepository.findByIdAndUserId(propertyId, userId);

        if (existingProperty.isEmpty()) {
            throw new RuntimeException("Property not found or unauthorized access");
        }

        Property property = existingProperty.get();
        property.setStatus(status);
        Property savedProperty = propertyRepository.save(property);

        return propertyMapper.toDto(savedProperty);
    }

    public List<PropertyDto> getPropertiesByUserId(UUID userId) {
        List<Property> properties = propertyRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return propertyMapper.toDtoList(properties);
    }

    public PropertyDto getPropertyById(Long propertyId) {
        Optional<Property> property = propertyRepository.findById(propertyId);
        return property.map(propertyMapper::toDto).orElse(null);
    }

    public PropertyDto getPropertyByIdAndUserId(Long propertyId, UUID userId) {
        Optional<Property> property = propertyRepository.findByIdAndUserId(propertyId, userId);
        return property.map(propertyMapper::toDto).orElse(null);
    }

    public void deleteProperty(Long propertyId, UUID userId) {
        Optional<Property> property = propertyRepository.findByIdAndUserId(propertyId, userId);
        if (property.isPresent()) {
            propertyRepository.delete(property.get());
        } else {
            throw new RuntimeException("Property not found or unauthorized access");
        }
    }
}
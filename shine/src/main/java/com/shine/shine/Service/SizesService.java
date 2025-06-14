package com.shine.shine.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.Sizes;
import com.shine.shine.Repository.SizesRepository;

@Service
public class SizesService {

    private final SizesRepository sizesRepository;

    public SizesService(SizesRepository sizesRepository) {
        this.sizesRepository = sizesRepository;
    }

    public List<Sizes> getAllSizes() {
        return sizesRepository.findAll();
    }

    public Optional<Sizes> getSizeById(Integer id) {
        return sizesRepository.findById(id);
    }

    @Transactional
    public Sizes createSize(Sizes size) {
        if (sizesRepository.existsBySizeName(size.getSizeName())) {
            throw new IllegalArgumentException("Size with name '" + size.getSizeName() + "' already exists.");
        }
        return sizesRepository.save(size);
    }

    @Transactional
    public Sizes updateSize(Integer id, Sizes sizeDetails) {
        Optional<Sizes> optionalSize = sizesRepository.findById(id);
        if (optionalSize.isPresent()) {
            Sizes existingSize = optionalSize.get();
            if (sizesRepository.existsBySizeName(sizeDetails.getSizeName()) &&
                !existingSize.getSizeName().equalsIgnoreCase(sizeDetails.getSizeName())) { // Check for uniqueness excluding itself
                throw new IllegalArgumentException("Size with name '" + sizeDetails.getSizeName() + "' already exists.");
            }
            existingSize.setSizeName(sizeDetails.getSizeName());
            return sizesRepository.save(existingSize);
        } else {
            return null; 
        }
    }

    @Transactional
    public boolean deleteSize(Integer id) {
        if (sizesRepository.existsById(id)) {
            sizesRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Sizes> getSizeByName(String sizeName) {
        return sizesRepository.findBySizeName(sizeName);
    }
}
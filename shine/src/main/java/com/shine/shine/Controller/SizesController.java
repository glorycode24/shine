package com.shine.shine.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Entity.Sizes;
import com.shine.shine.Service.SizesService;

@RestController
@RequestMapping("/api/sizes")
public class SizesController {

    private final SizesService sizeService;

    public SizesController(SizesService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping
    public ResponseEntity<List<Sizes>> getAllSizes() {
        List<Sizes> sizes = sizeService.getAllSizes();
        return new ResponseEntity<>(sizes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sizes> getSizeById(@PathVariable("id") Integer id) {
        Optional<Sizes> size = sizeService.getSizeById(id);
        return size.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Sizes> createSize(@RequestBody Sizes size) {
        try {
            Sizes createdSize = sizeService.createSize(size);
            return new ResponseEntity<>(createdSize, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sizes> updateSize(@PathVariable("id") Integer id, @RequestBody Sizes sizeDetails) {
        try {
            Sizes updatedSize = sizeService.updateSize(id, sizeDetails);
            if (updatedSize != null) {
                return new ResponseEntity<>(updatedSize, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSize(@PathVariable("id") Integer id) {
        boolean deleted = sizeService.deleteSize(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-name")
    public ResponseEntity<Sizes> getSizeByName(@RequestParam("name") String sizeName) {
        Optional<Sizes> size = sizeService.getSizeByName(sizeName);
        return size.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
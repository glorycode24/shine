package com.shine.shine.Controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Entity.Colors;
import com.shine.shine.Service.ColorsService;

@RestController
@RequestMapping("/api/colors")
public class ColorsController {

        private final ColorsService colorsService;
    
        public ColorsController(ColorsService colorsService) {
            this.colorsService = colorsService;
        }
    
        @GetMapping
        public List<Colors> getAllColors() {
            return colorsService.getAllColors();
        }
    
        @GetMapping("/{id}")
        public ResponseEntity<Colors> getColorById(@PathVariable int id) {
            return colorsService.getColorById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
    
        @PostMapping
        public Colors createColor(@RequestBody Colors colors) {
            return colorsService.createColor(colors);
        }
    
        @PutMapping("/{id}")
        public ResponseEntity<Colors> updateColor(@PathVariable int id, @RequestBody Colors colors) {
            try {
                return ResponseEntity.ok(colorsService.updateColors(id, colors));
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        }
    
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteColor(@PathVariable int id) {
            colorsService.deleteColor(id);
            return ResponseEntity.noContent().build();
        }
}

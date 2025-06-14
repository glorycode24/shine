package com.shine.shine.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shine.shine.Entity.Colors;
import com.shine.shine.Repository.ColorsRepository;
@Service
public class ColorsService {

private final ColorsRepository colorsRepository;

public ColorsService(ColorsRepository colorsRepository){
    this.colorsRepository=colorsRepository;
}

public List<Colors> getAllColors() {
    return colorsRepository.findAll();
}

public Optional<Colors> getColorById(int id) {
    return colorsRepository.findById(id);
}

public Colors createColor(Colors color) {
    return colorsRepository.save(color);
}

public void deleteColor(int id) {
    colorsRepository.deleteById(id);
}

public Colors updateColors(int id, Colors colorDetails) {
    return colorsRepository.findById(id).map(color -> {
        color.setColorName(colorDetails.getColorName());
        return colorsRepository.save(color);
    }).orElseThrow(() -> new RuntimeException("Color not found"));

}
}

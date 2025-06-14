// Example of your actual Colors.java structure that would cause this error
package com.shine.shine.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "colors")
public class Colors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ColorId")
    private Integer colorId;

    @Column(name = "ColorName", nullable = false, unique = true)
    private String colorName;

    public Colors() {}

    public Colors(String colorName) {
        this.colorName = colorName;
    }

    public Integer getColorId() { 
        return colorId;
    }

    public void setColorID(Integer colorID, Integer colorId) { 
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    @Override
    public String toString() {
        return "Colors{" +
                "colorID=" + colorId +
                ", colorName='" + colorName + '\'' +
                '}';
    }
}
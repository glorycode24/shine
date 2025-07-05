package com.shine.shine.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Service.StockService;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * Get available stock for a product variation
     */
    @GetMapping("/{variationId}")
    public ResponseEntity<Map<String, Object>> getStock(@PathVariable Integer variationId) {
        try {
            Integer availableStock = stockService.getAvailableStock(variationId);
            Map<String, Object> response = new HashMap<>();
            response.put("variationId", variationId);
            response.put("availableStock", availableStock);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get stock information: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check if there's sufficient stock for a quantity
     */
    @GetMapping("/{variationId}/check")
    public ResponseEntity<Map<String, Object>> checkStockAvailability(
            @PathVariable Integer variationId,
            @RequestParam Integer quantity) {
        try {
            boolean hasStock = stockService.hasSufficientStock(variationId, quantity);
            Map<String, Object> response = new HashMap<>();
            response.put("variationId", variationId);
            response.put("requestedQuantity", quantity);
            response.put("hasSufficientStock", hasStock);
            response.put("availableStock", stockService.getAvailableStock(variationId));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to check stock availability: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update stock quantity (Admin only)
     */
    @PutMapping("/{variationId}")
    public ResponseEntity<Map<String, Object>> updateStock(
            @PathVariable Integer variationId,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer newQuantity = request.get("quantity");
            if (newQuantity == null || newQuantity < 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid quantity. Must be a non-negative integer.");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            boolean updated = stockService.updateStock(variationId, newQuantity);
            if (updated) {
                Map<String, Object> response = new HashMap<>();
                response.put("variationId", variationId);
                response.put("newQuantity", newQuantity);
                response.put("message", "Stock updated successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Product variation not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update stock: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Reserve stock (decrease stock)
     */
    @PostMapping("/{variationId}/reserve")
    public ResponseEntity<Map<String, Object>> reserveStock(
            @PathVariable Integer variationId,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            if (quantity == null || quantity <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid quantity. Must be a positive integer.");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            boolean reserved = stockService.reserveStock(variationId, quantity);
            if (reserved) {
                Map<String, Object> response = new HashMap<>();
                response.put("variationId", variationId);
                response.put("reservedQuantity", quantity);
                response.put("remainingStock", stockService.getAvailableStock(variationId));
                response.put("message", "Stock reserved successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Insufficient stock or product variation not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to reserve stock: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Release stock (increase stock)
     */
    @PostMapping("/{variationId}/release")
    public ResponseEntity<Map<String, Object>> releaseStock(
            @PathVariable Integer variationId,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            if (quantity == null || quantity <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid quantity. Must be a positive integer.");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            boolean released = stockService.releaseStock(variationId, quantity);
            if (released) {
                Map<String, Object> response = new HashMap<>();
                response.put("variationId", variationId);
                response.put("releasedQuantity", quantity);
                response.put("newStock", stockService.getAvailableStock(variationId));
                response.put("message", "Stock released successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Product variation not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to release stock: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 
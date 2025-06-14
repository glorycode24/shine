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
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Entity.Addresses;
import com.shine.shine.Service.AddressesService;

@RestController
@RequestMapping("/api/addresses")
public class AddressesController {

    private final AddressesService addressService;

    public AddressesController(AddressesService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<Addresses>> getAllAddresses() {
        List<Addresses> addresses = addressService.getAllAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Addresses> getAddressById(@PathVariable("id") Integer id) {
        Optional<Addresses> address = addressService.getAddressById(id);
        return address.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Addresses> createAddress(@RequestBody Addresses address) {
        try {
            Addresses createdAddress = addressService.createAddress(address);
            return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Addresses> updateAddress(@PathVariable("id") Integer id, @RequestBody Addresses addressDetails) {
        try {
            Addresses updatedAddress = addressService.updateAddress(id, addressDetails);
            if (updatedAddress != null) {
                return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAddress(@PathVariable("id") Integer id) {
        boolean deleted = addressService.deleteAddress(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Addresses>> getAddressesByUserId(@PathVariable("userId") Integer userId) {
        List<Addresses> addresses = addressService.getAddressesByUserId(userId);
        if (!addresses.isEmpty()) {
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}/default")
    public ResponseEntity<Addresses> getDefaultAddressByUserId(@PathVariable("userId") Integer userId) {
        Optional<Addresses> defaultAddress = addressService.getDefaultAddressByUserId(userId);
        return defaultAddress.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                             .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
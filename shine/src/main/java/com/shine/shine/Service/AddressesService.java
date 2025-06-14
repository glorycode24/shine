package com.shine.shine.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.Addresses;
import com.shine.shine.Entity.Users;
import com.shine.shine.Repository.AddressesRepository;
import com.shine.shine.Repository.UsersRepository;

@Service
public class AddressesService {

    private final AddressesRepository addressRepository;
    private final UsersRepository userRepository; // Needed to validate User existence

    public AddressesService(AddressesRepository addressRepository, UsersRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<Addresses> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Optional<Addresses> getAddressById(Integer id) {
        return addressRepository.findById(id);
    }

    @Transactional
    public Addresses createAddress(Addresses address) {
        // Ensure the associated User exists
        if (address.getUser() == null || address.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User ID must be provided for the address.");
        }
        Users existingUser = userRepository.findById(address.getUser().getUserId())
                                          .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + address.getUser().getUserId()));
        address.setUser(existingUser);

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            List<Addresses> userAddresses = addressRepository.findByUser_UserId(existingUser.getUserId());
            userAddresses.forEach(addr -> {
                if (Boolean.TRUE.equals(addr.getIsDefault()) && !addr.getAddressId().equals(address.getAddressId())) {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                }
            });
        }
        return addressRepository.save(address);
    }

    @Transactional
    public Addresses updateAddress(Integer id, Addresses addressDetails) {
        Optional<Addresses> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Addresses existingAddress = optionalAddress.get();

            if (addressDetails.getUser() != null && addressDetails.getUser().getUserId() != null) {
                Users existingUser = userRepository.findById(addressDetails.getUser().getUserId())
                                                  .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + addressDetails.getUser().getUserId()));
                existingAddress.setUser(existingUser);
            } else if (addressDetails.getUser() == null) {
               
            }


            existingAddress.setStreetAddress(addressDetails.getStreetAddress());
            existingAddress.setCity(addressDetails.getCity());
            existingAddress.setStateProvince(addressDetails.getStateProvince());
            existingAddress.setPostalCode(addressDetails.getPostalCode());
            existingAddress.setCountry(addressDetails.getCountry());

            if (Boolean.TRUE.equals(addressDetails.getIsDefault()) && !Boolean.TRUE.equals(existingAddress.getIsDefault())) {
             
                List<Addresses> userAddresses = addressRepository.findByUser_UserId(existingAddress.getUser().getUserId());
                userAddresses.forEach(addr -> {
                    if (Boolean.TRUE.equals(addr.getIsDefault()) && !addr.getAddressId().equals(existingAddress.getAddressId())) {
                        addr.setIsDefault(false);
                        addressRepository.save(addr); // Save to persist the change
                    }
                });
                existingAddress.setIsDefault(true);
            } else if (Boolean.FALSE.equals(addressDetails.getIsDefault()) && Boolean.TRUE.equals(existingAddress.getIsDefault())) {
                existingAddress.setIsDefault(false);
            }


            return addressRepository.save(existingAddress);
        } else {
            return null;
        }
    }

    @Transactional
    public boolean deleteAddress(Integer id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Addresses> getAddressesByUserId(Integer userId) {
        return addressRepository.findByUser_UserId(userId);
    }

    public Optional<Addresses> getDefaultAddressByUserId(Integer userId) {
        return addressRepository.findByUser_UserIdAndIsDefaultTrue(userId);
    }
}
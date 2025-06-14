package com.shine.shine.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shine.shine.Entity.Addresses;

@Repository
public interface AddressesRepository extends JpaRepository<Addresses, Integer> {
  
    List<Addresses> findByUser_UserId(Integer userId);

    Optional<Addresses> findByUser_UserIdAndIsDefaultTrue(Integer userId);
}
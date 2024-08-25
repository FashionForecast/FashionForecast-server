package com.example.fashionforecastbackend.guest.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fashionforecastbackend.guest.domain.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

	Optional<Guest> findByUuid(String uuid);
	boolean existsByUuid(String uuid);
}

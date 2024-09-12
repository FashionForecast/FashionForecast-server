package com.example.fashionforecastbackend.global.login.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.fashionforecastbackend.global.login.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}

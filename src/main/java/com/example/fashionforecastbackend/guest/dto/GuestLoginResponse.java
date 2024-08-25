package com.example.fashionforecastbackend.guest.dto;

public record GuestLoginResponse(
	String uuid,
	boolean isNewGuest
) {
	public static GuestLoginResponse of(String uuid, boolean isNewGuest) {
		return new GuestLoginResponse(uuid, isNewGuest);
	}
}

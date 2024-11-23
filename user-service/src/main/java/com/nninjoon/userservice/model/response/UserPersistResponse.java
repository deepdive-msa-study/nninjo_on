package com.nninjoon.userservice.dto.response;

import com.nninjoon.userservice.domain.User;

import lombok.Builder;

@Builder
public record UserPersistResponse(
	Long id
) {
	public static UserPersistResponse of(User user) {
		return new UserPersistResponse(user.getId());
	}
}

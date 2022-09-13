package com.cNerds.dailyMoment.core.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SocialAuthToken {
	private String soAccessAuthToken;
    private String soRefreshAuthToken;
    private String socialEmail;
}

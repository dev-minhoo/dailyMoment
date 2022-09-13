package com.cNerds.dailyMoment.core.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthToken {
    private String grantType;
    private String accessAuthToken;
    private String refreshAuthToken;
    private String key;

}

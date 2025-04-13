package com.phuc.tutoring_center.core.domain.dto.response;

import com.phuc.tutoring_center.core.domain.entity.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private RefreshToken refreshToken;
    private String tokenType;
}

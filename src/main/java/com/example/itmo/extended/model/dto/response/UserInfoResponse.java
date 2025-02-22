package com.example.itmo.extended.model.dto.response;

import com.example.itmo.extended.model.dto.request.UserInfoRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse extends UserInfoRequest {

    @Schema(description = "id")
    private Long id;
}

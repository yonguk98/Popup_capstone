package com.capstone.popup.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class StoreCreateRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private String  startDate;

    @NotNull
    private String  endDate;

    @NotBlank
    private String location;
}

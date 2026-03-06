package com.example.mssscredentials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthMethodDTO {
    private AuthMethodType type;
    private String provider;
    private Boolean enabled;
    private Boolean canAdd;
    private Boolean canRemove;
}

package org.example.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminUpdateUserRolesDto {
  private List<String> roles;
}

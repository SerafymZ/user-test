package com.usertest.dto.basedto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BaseResponseDto {
    private Status status = Status.OK;
    private List<ErrorDto> errors;
}

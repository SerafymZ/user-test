package com.usertest.dto.basedto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResponseDto<T> extends BaseResponseDto{
    private T data;

    public ResponseDto(T data) {
        this.data = data;
    }
}

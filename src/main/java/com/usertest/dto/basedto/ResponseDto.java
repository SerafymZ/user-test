package com.usertest.dto.basedto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResponseDto<T> extends BaseResponseDto{
    private T data;

    public ResponseDto(T data) {
        this.data = data;
    }

    public static <T> ResponseDto<T> okResponseDto(T data) {
        var responseDto = new ResponseDto<>(data);
        responseDto.setStatus(Status.OK);
        return responseDto;
    }

    public static <T> ResponseDto<T> failedResponseDto(T data) {
        var responseDto = new ResponseDto<>(data);
        responseDto.setStatus(Status.Failed);

        return responseDto;
    }

    public static <T> ResponseDto<T> failedResponseDto(List<ErrorDto> errors) {

        var responseDto = new ResponseDto();
        responseDto.setStatus(Status.Failed);
        responseDto.setErrors(errors);

        return responseDto;
    }
}

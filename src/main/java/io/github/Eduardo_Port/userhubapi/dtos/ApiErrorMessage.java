package io.github.Eduardo_Port.userhubapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ApiErrorMessage(
        @Schema(description = "Data/Hora do erro", example = "2026-01-26T20:30:00")
        @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
        LocalDateTime timestamp,
        @Schema(description = "Código de status HTTP", example = "404")
        Integer code,
        @Schema(description = "Nome do status HTTP", example = "Not_Found")
        String status,
        @Schema(description = "Resumo do erro", example = "Usuário não encontrado")
        List<String> errors

) {

}

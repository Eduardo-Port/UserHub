package io.github.Eduardo_Port.userhubapi.annotation;

import io.github.Eduardo_Port.userhubapi.dtos.ApiErrorMessage;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "500", description = "Erro no servidor",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorMessage.class), examples = {
                        @ExampleObject(value = "No body returned for response")
                })),
        @ApiResponse(responseCode = "400", description = "UUID inválido",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorMessage.class), examples = {
                        @ExampleObject(value = """
                    {
                        "timestamp": "2026-01-27T10:00:00",
                        "code": 400,
                        "status": "BAD_REQUEST",
                        "errors": [
                            "UUID String too large"
                        ]
                    }
                """)
                })),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorMessage.class),
                        examples = @ExampleObject(value = """
                    {
                        "timestamp": "2026-01-27T10:00:00",
                        "code": 404,
                        "status": "NOT_FOUND",
                        "errors": [
                            "User not found"
                        ]
                    }
                """))
        ),

})
public @interface ApiStandardErrors {
}

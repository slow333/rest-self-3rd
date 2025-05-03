package com.magic.controller;

import com.magic.system.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Start {

   @Operation(summary = "QR 스캔", description = "사용자가 배송된 물품의 QR코드를 스캔합니다.")
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "스캔 성공",
                   content = @Content(schema = @Schema(implementation = Result.class))),
           @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                   content = @Content(schema = @Schema(implementation = Result.class))),
           @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
                   content = @Content(schema = @Schema(implementation = Result.class))),
           @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                   content = @Content(schema = @Schema(implementation = Result.class)))
   })
   @GetMapping("/start")
   public String start() {
      return "Magic application started!";
   }
}

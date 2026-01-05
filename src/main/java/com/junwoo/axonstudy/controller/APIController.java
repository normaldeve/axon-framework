package com.junwoo.axonstudy.controller;

import com.junwoo.axonstudy.command.CreateElephantCommand;
import com.junwoo.axonstudy.dto.ElephantDTO;
import com.junwoo.axonstudy.entity.Elephant;
import com.junwoo.axonstudy.service.ElephantService;
import com.junwoo.axonstudy.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Tag(name = "Order service API", description = "Order service API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class APIController {

    private final ElephantService elephantService;

    @PostMapping("/create")
    @Operation(summary = "코끼리 생성 API")
    public ResultVO<CreateElephantCommand> create(
            @RequestBody ElephantDTO elephant
    ) {
        log.info("[@POST/create] Executing create: {}", elephant.toString());

        return elephantService.create(elephant);
    }

    @PostMapping("/enter/{id}")
    @Operation(summary = "냉장고에 넣기 API")
    @Parameters({
            @Parameter(name = "id", in = ParameterIn.PATH, description = "코끼리 ID", required = true)
    })
    public ResultVO<String> enter(
            @PathVariable(name = "id") String id
    ) {
        log.info("[@Post/enter] Id: {}", id);

        return elephantService.enter(id);
    }

    @PostMapping("/exit/{id}")
    @Operation(summary = "냉장고에 꺼내기 API")
    @Parameters({
            @Parameter(name = "id", in = ParameterIn.PATH, description = "코끼리 ID", required = true)
    })
    public ResultVO<String> exit(
            @PathVariable(name = "id") String id
    ) {
        log.info("[@Post/exit] Id: {}", id);

        return elephantService.exit(id);
    }

    @GetMapping("/elephant/{id}")
    @Operation(summary = "코끼리 정보 API")
    @Parameters({
            @Parameter(name = "id", in = ParameterIn.PATH, description = "코끼리 ID", required = true)
    })
    public ResultVO<Elephant> getElephant(
            @PathVariable(name = "id") String id
    ) {
        log.info("[@GetMapping elephant/{id}] Id: {} ", id);

        return elephantService.getElephant(id);
    }

    @GetMapping("/elephants")
    @Operation(summary = "코끼리 리스트 API")
    public ResultVO<List<Elephant>> getLists() {
        log.info("[@GetMapping /elephants]");

        return elephantService.getLists();
    }
}

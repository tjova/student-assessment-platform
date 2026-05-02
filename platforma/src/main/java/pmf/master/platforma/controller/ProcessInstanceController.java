package pmf.master.platforma.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pmf.master.platforma.main.entity.ProcessInstanceHistory;
import pmf.master.platforma.main.model.ProcessInstanceHistoryResponseDto;
import pmf.master.platforma.model.StartProcessInstanceRequestDto;
import pmf.master.platforma.model.StartProcessInstanceResponseDto;
import pmf.master.platforma.main.service.ProcessInstanceService;

@RestController
@RequestMapping("/process-instance")
public class ProcessInstanceController {

    private final ProcessInstanceService processInstanceService;

    public ProcessInstanceController(ProcessInstanceService processInstanceService) {
        this.processInstanceService = processInstanceService;
    }

    @Operation(
            summary = "Start process instance by definition id",
            description = "Starts a new process instance for the definition provided."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Operation executed successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StartProcessInstanceResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @PostMapping("/start")
    public ResponseEntity<StartProcessInstanceResponseDto> startProcessInstance(@Parameter(description = "Request body for starting process instance")
                                                                                    @RequestBody StartProcessInstanceRequestDto requestDto) {
        return ResponseEntity.ok(processInstanceService.startProcessInstance(requestDto));
    }

    @Operation(
            summary = "Get process instance by process instance history id",
            description = "Executes the get operation on a Process Instance History entity by id."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Operation executed successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StartProcessInstanceResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @GetMapping("/{processInstanceHistoryId}")
    public ResponseEntity<ProcessInstanceHistoryResponseDto> getProcessInstanceHistory(@Parameter(description = "Process Instance History Id to be fetched")
                                                                            @PathVariable String processInstanceHistoryId) {
        return ResponseEntity.ok(processInstanceService.getProcessInstanceHistory(processInstanceHistoryId));
    }
}

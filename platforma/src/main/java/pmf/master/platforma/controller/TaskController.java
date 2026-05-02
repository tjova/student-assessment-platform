package pmf.master.platforma.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pmf.master.platforma.camunda.model.CamundaClaimTaskRequestDto;
import pmf.master.platforma.camunda.model.CamundaSubmitFormRequestDto;
import pmf.master.platforma.main.entity.TaskHistory;
import pmf.master.platforma.main.model.TaskHistoryResponseDto;
import pmf.master.platforma.main.service.TaskAsyncService;
import pmf.master.platforma.main.service.TaskService;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final TaskAsyncService taskAsyncService;

    public TaskController(TaskService taskService, TaskAsyncService taskAsyncService) {
        this.taskService = taskService;
        this.taskAsyncService = taskAsyncService;
    }

    @Operation(
            summary = "Complete task with file",
            description = "Completes a task with a file attached."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @PostMapping(value = "/{taskId}/complete-with-file",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> completeTaskWithFile(@Parameter(description = "ID of task to be completed") @PathVariable String taskId,
                                                     @Parameter(description = "Request body for complete task") @RequestPart("variables") CamundaSubmitFormRequestDto requestDto,
                                                     @Parameter(description = "File to be submitted for complete task") @RequestPart("uploadedFile") MultipartFile file) throws IOException {
        taskService.completeTaskWithFile(taskId, file, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Claims task by task id",
            description = "Executes the claim operation for a user by user id on a task by task id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @PostMapping("/{taskId}/claim")
    public ResponseEntity<Void> claimTask(@Parameter(description = "ID of task to be claimed") @PathVariable String taskId,
                                          @Parameter(description = "Request body for claim task") @RequestBody CamundaClaimTaskRequestDto camundaClaimTaskRequestDto) {
        taskService.claimTask(taskId, camundaClaimTaskRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Unclaims task by task id",
            description = "Executes the unclaim operation on a task by task id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @PostMapping("/{taskId}/unclaim")
    public ResponseEntity<Void> unclaimTask(@Parameter(description = "ID of task to be unclaimed") @PathVariable String taskId) {
        taskService.unclaimTask(taskId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Completes task by task id",
            description = "Executes the complete operation on a task by task id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @PostMapping("/{taskId}/complete")
    public ResponseEntity<Void> completeTask(@Parameter(description = "ID of task to be completed") @PathVariable String taskId,
                                             @Parameter(description = "Request body for complete task") @RequestBody CamundaSubmitFormRequestDto requestDto) {
        taskService.completeTask(taskId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Save task history list",
            description = "Executes the save operation on a list of Task History entities."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @PostMapping("/save/list")
    public ResponseEntity<Void> saveAllTaskHistory(@Parameter(description = "List of Task Histories to be saved")
                                                   @RequestBody List<TaskHistory> taskHistoryList) {
        taskAsyncService.saveAllAsync(taskHistoryList);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Save task history",
            description = "Executes the save operation on a Task History entity."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @PostMapping("/save")
    public ResponseEntity<TaskHistory> saveTaskHistory(@Parameter(description = "Task History to be saved")
                                                       @RequestBody TaskHistory taskHistory) {
        return ResponseEntity.ok(taskAsyncService.save(taskHistory));
    }


    @Operation(
            summary = "Get Task History by id",
            description = "Executes the get operation on a Task History entity by id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskHistoryResponseDto> getTaskHistory(@Parameter(description = "Task History Id to be fetched")
                                                                 @PathVariable String taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }
}

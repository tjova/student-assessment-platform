package pmf.master.platforma.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pmf.master.platforma.camunda.model.CamundaHttpConnectorRequest;
import pmf.master.platforma.main.service.ProcessInstanceService;
import pmf.master.platforma.main.service.TaskAsyncService;

@RestController
@RequestMapping("/camunda")
public class CamundaConnectorController {
    //todo write controller methods for when camunda is calling the application

    private final TaskAsyncService taskAsyncService;
    private final ProcessInstanceService processInstanceService;
    private final Logger logger = LoggerFactory.getLogger(CamundaConnectorController.class);

    public CamundaConnectorController(TaskAsyncService taskAsyncService,
                                      ProcessInstanceService processInstanceService) {
        this.taskAsyncService = taskAsyncService;
        this.processInstanceService = processInstanceService;
    }

    @Operation(
            summary = "Save task history from Camunda",
            description = "Executes the save operation on a Task History entity from Camunda."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @PostMapping(value = "/task-save", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, "application/json;charset=UTF-8" })
    public ResponseEntity<Void> saveTaskFromCamunda(@Parameter(description = "Camunda request for task history save")
                                                    @RequestBody CamundaHttpConnectorRequest request) {
        logger.info("Called task save");
        taskAsyncService.saveTaskFromCamunda(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Complete Process Instance from Camunda",
            description = "Executes the complete operation on a Process Instance History from Camunda."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception"),
            @ApiResponse(responseCode = "404", description = "Not Found Exception")
    })
    @PostMapping("/process-complete")
    public ResponseEntity<Void> completeProcessInstanceHistoryFromCamunda(@Parameter(description = "Camunda request for task history save")
                                                    @RequestBody CamundaHttpConnectorRequest request) {
        processInstanceService.completeProcessInstance(request);
        return ResponseEntity.ok().build();
    }
}

package net.deeptodo.app.api.plan.presentation;

import net.deeptodo.app.api.plan.application.PlanService;
import net.deeptodo.app.api.plan.dto.response.GetPlansResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    public ResponseEntity<GetPlansResponse> getPlans() {

        GetPlansResponse response = planService.getPlans();

        return ResponseEntity.ok(response);
    }
}

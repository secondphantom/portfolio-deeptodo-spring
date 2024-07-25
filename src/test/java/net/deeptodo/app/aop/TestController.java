package net.deeptodo.app.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    public static final String getPath = "/test/get";

    @GetMapping(getPath)
    public String testEndpoint() {
        return "This is a test endpoint";
    }
}

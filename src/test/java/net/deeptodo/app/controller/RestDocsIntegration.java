package net.deeptodo.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.deeptodo.app.aop.auth.AuthInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public abstract class RestDocsIntegration {
    @Autowired
    protected ObjectMapper objectMapper;

    protected RestDocumentationResultHandler restDocs;

    protected MockMvc mockMvc;

    @MockBean
    private AuthInterceptor authInterceptor;

    @BeforeEach
    void beforeEach(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) throws Exception {
        given(authInterceptor.preHandle(any(), any(), any())).willReturn(true);
        this.restDocs = MockMvcRestDocumentation.document(
                "{class-name}/{method-name}",
                Preprocessors.preprocessRequest(
                        Preprocessors.modifyHeaders()
                                .remove("Content-Length")
                                .remove("Host"),
                        Preprocessors.prettyPrint()
                ),
                Preprocessors.preprocessResponse(
                        Preprocessors.modifyHeaders()
                                .remove("Content-Length")
                                .remove("Transfer-Encoding")
                                .remove("Date")
                                .remove("Keep-Alive")
                                .remove("Connection"),
                        Preprocessors.prettyPrint()
                ));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(
                        MockMvcRestDocumentation.documentationConfiguration(provider)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint())
                )
                .alwaysDo(restDocs)
                .build();
    }
}

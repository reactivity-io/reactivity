package io.reactivity.core.broadcaster.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * <p>
 * This controller serving a test UI.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
@RestController
public class MockController {

    /**
     * <p>
     * Generates a test index.html page with a new cookie to track the user session.
     * </p>
     *
     * @return the HTTP response
     * @throws IOException if HTML page can be loaded
     */
    @GetMapping("/")
    public ResponseEntity<String> index() throws IOException {
        final String sessionId = UUID.randomUUID().toString();
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Set-Cookie", "SESSION=" + sessionId);
        responseHeaders.set("Content-Type", "text/html");

        try (InputStreamReader isr = new InputStreamReader(new ClassPathResource("mocks/index.html").getInputStream())) {
            return new ResponseEntity<>(FileCopyUtils.copyToString(isr), responseHeaders, HttpStatus.OK);
        }
    }
}

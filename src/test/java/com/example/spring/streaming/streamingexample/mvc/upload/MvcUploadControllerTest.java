package com.example.spring.streaming.streamingexample.upload;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MvcUploadControllerTest
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    // ============================== [Unit Tests] ==============================

    // -------------------- [Test Helper Classes] --------------------

    // -------------------- [Test Helper Methods] --------------------

    // -------------------- [Test Initialization] --------------------

    // -------------------- [Tests] --------------------

    @Autowired
    private TestRestTemplate template;

    @Test
    void uploadTest()
    {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("/video1.mp4"));
//        parameters.add("file", new ClassPathResource("/2-gb-file"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = template.exchange("/stream/upload/mvc/file", HttpMethod.POST, entity, String.class, "");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}

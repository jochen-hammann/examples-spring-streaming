package com.example.spring.streaming.streamingexample.upload;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Taken from: https://www.baeldung.com/spring-apache-file-upload.
 */
@RestController
@RequestMapping("stream/apachefileupload/mvc")
@Slf4j
public class MvcApacheFileUploadController
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    // ============================== [Construction / Destruction] ==============================

    // -------------------- [Public Construction / Destruction] --------------------

    // ============================== [Spring Beans] ==============================

    // -------------------- [Public Spring Beans] --------------------

    // ============================== [Getter/Setter] ==============================

    // -------------------- [Private Getter/Setter] --------------------

    // -------------------- [Public Getter/Setter] --------------------

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

    @PostMapping(value = "/file")
    public ResponseEntity<Void> upload(HttpServletRequest request) throws Exception
    {
        if (!ServletFileUpload.isMultipartContent(request))
            throw new Exception("Multipart request expected");

        ServletFileUpload upload = new ServletFileUpload();
        FileItemIterator iterStream = upload.getItemIterator(request);

        while (iterStream.hasNext())
        {
            FileItemStream item = iterStream.next();
            String fileName = item.getName();
            String type = item.getContentType();
            String destination = String.format("./target/%s-%s", UUID.randomUUID(), fileName);

            try (InputStream uploadedStream = item.openStream(); OutputStream out = new FileOutputStream(destination))
            {
                IOUtils.copy(uploadedStream, out);

                log.info("Saved {} with type {} to {}", fileName, type, destination);
            }
        }

        return ResponseEntity.noContent().build();
    }
}

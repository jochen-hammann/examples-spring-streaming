package com.example.spring.streaming.streamingexample.upload;

import com.example.spring.streaming.streamingexample.dto.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Taken from: https://niels.nu/blog/2022/spring-file-upload-download.
 */
@RestController
@RequestMapping("stream/upload/mvc")
@Slf4j
public class MvcUploadController
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

    public void upload(FileItemIterator iter) throws IOException {
        while (iter.hasNext()) {
            FileItemStream item = iter.next();
            if(item.isFormField()) {
                continue;
            }
            upload(item);
        }
    }

    private void upload(FileItemStream item) throws IOException {
        String fileName = item.getName();
        String type = item.getContentType();
        InputStream ins = item.openStream();
        var destination = new File("./target", String.format("%s-%s", UUID.randomUUID(), fileName));
        var outs = new FileOutputStream(destination);

        IOUtils.copy(ins, outs);
        IOUtils.closeQuietly(ins);
        IOUtils.closeQuietly(outs);

        log.info("Saved {} with type {} to {}", fileName, type, destination);
    }

    // -------------------- [Public Methods] --------------------

    @PostMapping(value = "/file")
    public ResponseEntity<Void> upload(HttpServletRequest request) throws Exception
    {
        if (!ServletFileUpload.isMultipartContent(request))
            throw new Exception("Multipart request expected");

        this.upload(new ServletFileUpload().getItemIterator(request));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

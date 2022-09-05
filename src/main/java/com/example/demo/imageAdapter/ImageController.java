package com.example.demo.imageAdapter;



import com.example.demo.imageAdapter.imageModels.PersonImage;
import com.example.demo.imageAdapter.service.ImageService;
import com.example.demo.models.Person;
import com.example.demo.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

import static com.example.demo.servises.PersonService.getCurrentUserID;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/personAvatar")
    private ResponseEntity<?> getImageById() {
        int person_id = getCurrentUserID();
        PersonImage image = imageService.getImageByPersonId(person_id);

        if ( image != null) {         //если аватарка у пользователя есть, то мы ее покаем
            return ResponseEntity.ok()
                    .header("fileName", image.getOriginalFileName())
                    .contentType(MediaType.valueOf(image.getContentType()))
                    .contentLength(image.getSize())
                    .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
        }

        PersonImage defaultIm = jdbcTemplate.query("select * from avatar_images where is_default = true",
                        new Object[]{},
                        new BeanPropertyRowMapper<>(PersonImage.class))
                .stream().findAny().get();


        return ResponseEntity.ok()   //А если все таки нету, то покажем дефолтную
                .header("fileName", defaultIm.getOriginalFileName())
                .contentType(MediaType.valueOf(defaultIm.getContentType()))
                .contentLength(defaultIm.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(defaultIm.getBytes())));
    }



}

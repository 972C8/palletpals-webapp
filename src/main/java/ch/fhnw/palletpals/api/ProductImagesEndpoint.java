/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.ImageService;
import ch.fhnw.palletpals.data.domain.image.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api")
public class ProductImagesEndpoint {
    @Autowired
    private ImageService imageService;

    /**
     * Save the uploaded image
     */
    @PostMapping("/product-images")
    public ResponseEntity<ProductImage> postProductImage(@RequestParam(value = "image") MultipartFile image) {
        try {
            ProductImage item = imageService.saveProductImage(image);

            return ResponseEntity.accepted().body(item);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }
}
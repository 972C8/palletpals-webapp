/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.ImageService;
import ch.fhnw.palletpals.data.domain.image.ProductImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api")
public class ProductImagesEndpoint {
    @Autowired
    private ImageService imageService;

    Logger logger = LoggerFactory.getLogger(ProductImagesEndpoint.class);

    /**
     * Code by: Tibor Haller
     *
     * Save the uploaded image
     */
    @PostMapping("/product-images")
    public ResponseEntity<ProductImage> postProductImage(@RequestParam(value = "image") MultipartFile image) {
        try {
            ProductImage item = imageService.saveProductImage(image);
            logger.info("Product image saved with id: " + item.getId());
            return ResponseEntity.accepted().body(item);
        } catch (Exception e) {
            logger.error("Error while saving product image: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    /**
     * Code by: Tibor Haller
     *
     * GET the uploaded image by imageId
     */
    @GetMapping("/product-images/{imageId}")
    public ResponseEntity<Resource> getProductImage(@PathVariable(value = "imageId") String imageId) {
        try {
            // Load file as Resource
            ProductImage imageFile = imageService.loadProductImage(Long.parseLong(imageId));

            //Create resource from imageFile
            Resource resource = imageService.loadResourceFromProductImage(imageFile);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(imageFile.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getFileName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            logger.error("Error while getting product image with id " + imageId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Code by: Tibor Haller
     *
     * @param imageId
     * @return
     */
    @DeleteMapping(path = "/product-images/{imageId}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable(value = "imageId") String imageId) {
        try {
            imageService.deleteImageById(Long.parseLong(imageId));
            logger.info("Product image delted with id: " + imageId);
        } catch (Exception e) {
            logger.error("Error while deleting product image with id " + imageId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

}
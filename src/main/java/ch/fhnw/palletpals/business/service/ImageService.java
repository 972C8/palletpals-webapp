package ch.fhnw.palletpals.business.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

import ch.fhnw.palletpals.data.domain.image.ProductImage;
import ch.fhnw.palletpals.data.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service to save and retrieve uploaded images ProductItemImage from file system.
 * <p>
 * https://www.bezkoder.com/spring-boot-file-upload/
 */
@Service
public class ImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    private final Path root = Paths.get("uploads");

    /**
     * Create root directory if not exists yet
     */
    public void rootExists() {
        try {
            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    /**
     * Creates a randomized name for the image name
     *
     * @param name
     * @return
     */
    public String createRandomName(String name) {
        //generate the random string
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String[] split = name.split("[.]");
        String suffix = split[split.length - 1];

        //Return the generated string and append the image type as suffix.
        //e.g. 1283239.png
        return generatedString + "." + suffix;
    }

    /**
     * Save the uploaded image as a ProductImage in the file system
     *
     * @param file
     * @return
     */
    public ProductImage saveProductImage(MultipartFile file) {
        try {
            //Check if root already exists
            rootExists();

            //Ensure a unique file name
            String imageName;
            Path url;

            int maxTries = 15;
            int i = 1;
            do {
                imageName = createRandomName(Objects.requireNonNull(file.getOriginalFilename()));
                url = this.root.resolve(imageName);

                //Ensures that no infinite loop occurs
                if (i > maxTries) {
                    throw new Exception("Could not ensure unique name for uploaded image.");
                }
                i++;
            } while (Files.exists(url));

            //TODO: add safety net after 15 tries!

            //Upload image to root folder
            Files.copy(file.getInputStream(), url);

            //store the image information in the database and reference the product
            ProductImage image = new ProductImage(imageName, url.toString(), file.getContentType());

            return productImageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    /**
     * Load ProductItemImage from productItemId
     *
     * @param imageId
     * @return
     */
    public ProductImage loadProductImage(Long imageId) {
        try {
            return productImageRepository.findProductImageById(imageId);

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    /**
     * Load the resource using the ProductImage. Resource is used to provide image through API.
     *
     * @param item
     * @return
     */
    public Resource loadResourceFromProductImage(ProductImage item) {
        try {
            //Find the image of the ProductItem
            Path file = root.resolve(item.getFileName());

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    /**
     * Delete image by provided id, both from directory and database
     *
     * @param imageId
     */
    public void deleteImageById(Long imageId) {
        try {
            //Load file as Resource
            ProductImage imageFile = loadProductImage(imageId);

            //Delete ProductImage by imageId. As explained in ProductImage.java, the reference is automatically deleted from Product when the ProductImage is deleted.
            productImageRepository.deleteById(imageId);

            //Find and delete the image from the directory
            Path file = root.resolve(imageFile.getFileName());
            Files.delete(file);

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
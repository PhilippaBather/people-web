package com.batherphilippa.peopledbweb.data;

import com.batherphilippa.peopledbweb.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Repository
public class FileStorageRepository {
    @Value("${STORAGE_FOLDER}")
    private String storageLocation;

    /*
     * Saves input stream as a file onto the file system.
     */
    public void save(String originalFilename, InputStream inputStream) {
        try {
            // normalize cleans up file path
            Path filePath = Path.of(storageLocation).resolve(originalFilename).normalize();
            // save input stream to path
            Files.copy(inputStream, filePath);
        } catch (IOException e) {
            throw new StorageException(format("Storage location not found: %s", e));
        }
    }

    public Resource findByName(String filename) {
        try {
            Path filePath = Path.of(storageLocation).resolve(filename).normalize();
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new StorageException(format("Storage location not found: %s", e));
        }
    }
    public void deleteAllByName(Collection<String> filenames) {
        try {
            for (String filename : filenames.stream()
                    .filter(filename -> filename != null).collect(Collectors.toSet())) {
                Path filePath = Path.of(storageLocation).resolve(filename).normalize();
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            throw new StorageException(format("Error deleting file: %s"), e);
        }
    }
}

package fr.pala.accounting.utils.file;

import fr.pala.accounting.utils.file.exception.FileIsEmptyException;
import fr.pala.accounting.utils.file.exception.FileNotCreatedException;
import fr.pala.accounting.utils.file.exception.FolderNotCreatedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class downloadUtils {
    public static Path downloadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileIsEmptyException();
        }

        Path filePath;
        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();

            String UPLOAD_FOLDER = "./Download/";
            File directory = new File(UPLOAD_FOLDER);
            if(!directory.exists()) {
                if(!directory.mkdir()) {
                    throw new FolderNotCreatedException();
                }
            }

            filePath = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            Files.write(filePath, bytes);

            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileNotCreatedException();
        }
    }
}

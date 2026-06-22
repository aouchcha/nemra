package backend.nemra.modules.media;

import backend.nemra.shared.exception.MyBadRequest;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
public class MediaService {
    private final S3Client s3Client;
    private final String bucketName;

    private final Tika tika;

    public MediaService(
            S3Client s3Client,
            @Value("${r2.bucket}") String bucketName,
            Tika tika
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.tika = tika;
    }

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );
    public String uploadFile(MultipartFile file, String folder) throws IOException, RuntimeException {

        final byte[] data = file.getBytes();
        if (data.length > 2 * 1024 * 1024) {
            throw new MyBadRequest("File size exceeds 2MB limit");
        }
        String mimeType = tika.detect(data);
        System.out.println("MIME TYPE: " + mimeType);
        if (!ALLOWED_TYPES.contains(mimeType)) {
            throw new MyBadRequest("Invalid file type: " + mimeType);
        }
        String extension = getExtension(file.getOriginalFilename());
        UUID fileId = UUID.randomUUID();
        String fileName = folder + "/" + fileId + extension;
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(data));
        return "/" + fileName;
    }

    public void deleteFile(String fileName) throws RuntimeException {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteRequest);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }
}

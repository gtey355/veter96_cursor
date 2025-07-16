package com.veter96.webapp.service;

import com.veter96.webapp.model.Attachment;
import com.veter96.webapp.repository.AttachmentRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachmentService {
    
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAllOrderByUploadDateDesc();
    }
    
    public Optional<Attachment> getAttachment(Long id) {
        return attachmentRepository.findById(id);
    }
    
    public List<Attachment> getAttachmentsByCategory(String category) {
        return attachmentRepository.findByCategory(category);
    }
    
    public List<Attachment> searchAttachments(String keyword) {
        return attachmentRepository.findByKeyword(keyword);
    }
    
    public List<String> getAllCategories() {
        return attachmentRepository.findDistinctCategories();
    }
    
    public Attachment saveAttachment(MultipartFile file, String description, String category) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        if (fileName.contains("..")) {
            throw new IOException("Недопустимое имя файла: " + fileName);
        }
        
        // Create uploads directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String fileExtension = FilenameUtils.getExtension(fileName);
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
        
        Path targetLocation = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        Attachment attachment = new Attachment(
            uniqueFileName,
            fileName,
            targetLocation.toString(),
            file.getContentType(),
            file.getSize()
        );
        
        attachment.setDescription(description);
        attachment.setCategory(category);
        
        return attachmentRepository.save(attachment);
    }
    
    public Resource loadFileAsResource(Long attachmentId) throws IOException {
        Optional<Attachment> attachmentOpt = attachmentRepository.findById(attachmentId);
        
        if (attachmentOpt.isEmpty()) {
            throw new IOException("Файл не найден с ID: " + attachmentId);
        }
        
        Attachment attachment = attachmentOpt.get();
        Path filePath = Paths.get(attachment.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            // Increment download count
            attachment.incrementDownloadCount();
            attachmentRepository.save(attachment);
            return resource;
        } else {
            throw new IOException("Файл не найден или не читается: " + attachment.getOriginalName());
        }
    }
    
    public void deleteAttachment(Long id) throws IOException {
        Optional<Attachment> attachmentOpt = attachmentRepository.findById(id);
        
        if (attachmentOpt.isPresent()) {
            Attachment attachment = attachmentOpt.get();
            
            // Delete file from filesystem
            Path filePath = Paths.get(attachment.getFilePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            
            // Delete from database
            attachmentRepository.delete(attachment);
        }
    }
    
    public Attachment updateAttachment(Long id, String description, String category) {
        Optional<Attachment> attachmentOpt = attachmentRepository.findById(id);
        
        if (attachmentOpt.isPresent()) {
            Attachment attachment = attachmentOpt.get();
            attachment.setDescription(description);
            attachment.setCategory(category);
            return attachmentRepository.save(attachment);
        }
        
        return null;
    }
    
    public List<Attachment> getPopularAttachments() {
        return attachmentRepository.findAllOrderByDownloadCountDesc();
    }
    
    public long getTotalAttachments() {
        return attachmentRepository.count();
    }
    
    public long getTotalFileSize() {
        return attachmentRepository.findAll().stream()
            .mapToLong(attachment -> attachment.getFileSize() != null ? attachment.getFileSize() : 0)
            .sum();
    }
}
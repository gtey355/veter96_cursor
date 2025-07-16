package com.veter96.webapp.repository;

import com.veter96.webapp.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
    List<Attachment> findByCategory(String category);
    
    List<Attachment> findByOriginalNameContainingIgnoreCase(String name);
    
    List<Attachment> findByDescriptionContainingIgnoreCase(String description);
    
    @Query("SELECT a FROM Attachment a WHERE " +
           "a.originalName LIKE %:keyword% OR " +
           "a.description LIKE %:keyword% OR " +
           "a.category LIKE %:keyword%")
    List<Attachment> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT a FROM Attachment a ORDER BY a.uploadDate DESC")
    List<Attachment> findAllOrderByUploadDateDesc();
    
    @Query("SELECT a FROM Attachment a ORDER BY a.downloadCount DESC")
    List<Attachment> findAllOrderByDownloadCountDesc();
    
    @Query("SELECT DISTINCT a.category FROM Attachment a WHERE a.category IS NOT NULL ORDER BY a.category")
    List<String> findDistinctCategories();
}
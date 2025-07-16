package com.veter96.webapp.controller;

import com.veter96.webapp.model.Attachment;
import com.veter96.webapp.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/attachments")
public class AttachmentController {
    
    @Autowired
    private AttachmentService attachmentService;
    
    @GetMapping
    public String listAttachments(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "search", required = false) String search,
            Model model) {
        
        List<Attachment> attachments;
        
        if (search != null && !search.trim().isEmpty()) {
            attachments = attachmentService.searchAttachments(search.trim());
            model.addAttribute("searchKeyword", search);
        } else if (category != null && !category.trim().isEmpty()) {
            attachments = attachmentService.getAttachmentsByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            attachments = attachmentService.getAllAttachments();
        }
        
        model.addAttribute("attachments", attachments);
        model.addAttribute("categories", attachmentService.getAllCategories());
        model.addAttribute("totalAttachments", attachmentService.getTotalAttachments());
        model.addAttribute("totalFileSize", formatFileSize(attachmentService.getTotalFileSize()));
        model.addAttribute("pageTitle", "Файлы и документы - Ветеринар Васильев А.В.");
        model.addAttribute("currentPage", "attachments");
        
        return "attachments/list";
    }
    
    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("categories", attachmentService.getAllCategories());
        model.addAttribute("pageTitle", "Загрузка файла - Ветеринар Васильев А.В.");
        model.addAttribute("currentPage", "upload");
        return "attachments/upload";
    }
    
    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "category", required = false) String category,
            RedirectAttributes redirectAttributes) {
        
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Пожалуйста, выберите файл для загрузки");
            return "redirect:/attachments/upload";
        }
        
        try {
            Attachment attachment = attachmentService.saveAttachment(file, description, category);
            redirectAttributes.addFlashAttribute("success", 
                "Файл '" + attachment.getOriginalName() + "' успешно загружен");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", 
                "Ошибка при загрузке файла: " + e.getMessage());
        }
        
        return "redirect:/attachments";
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            Resource resource = attachmentService.loadFileAsResource(id);
            Optional<Attachment> attachmentOpt = attachmentService.getAttachment(id);
            
            if (attachmentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Attachment attachment = attachmentOpt.get();
            
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + attachment.getOriginalName() + "\"")
                .body(resource);
                
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/view/{id}")
    public String viewAttachment(@PathVariable Long id, Model model) {
        Optional<Attachment> attachmentOpt = attachmentService.getAttachment(id);
        
        if (attachmentOpt.isEmpty()) {
            return "redirect:/attachments";
        }
        
        model.addAttribute("attachment", attachmentOpt.get());
        model.addAttribute("pageTitle", "Просмотр файла - " + attachmentOpt.get().getOriginalName());
        model.addAttribute("currentPage", "attachments");
        
        return "attachments/view";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Attachment> attachmentOpt = attachmentService.getAttachment(id);
        
        if (attachmentOpt.isEmpty()) {
            return "redirect:/attachments";
        }
        
        model.addAttribute("attachment", attachmentOpt.get());
        model.addAttribute("categories", attachmentService.getAllCategories());
        model.addAttribute("pageTitle", "Редактирование файла - " + attachmentOpt.get().getOriginalName());
        model.addAttribute("currentPage", "attachments");
        
        return "attachments/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String updateAttachment(
            @PathVariable Long id,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "category", required = false) String category,
            RedirectAttributes redirectAttributes) {
        
        Attachment updatedAttachment = attachmentService.updateAttachment(id, description, category);
        
        if (updatedAttachment != null) {
            redirectAttributes.addFlashAttribute("success", "Информация о файле обновлена");
        } else {
            redirectAttributes.addFlashAttribute("error", "Файл не найден");
        }
        
        return "redirect:/attachments";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteAttachment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            attachmentService.deleteAttachment(id);
            redirectAttributes.addFlashAttribute("success", "Файл успешно удален");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении файла: " + e.getMessage());
        }
        
        return "redirect:/attachments";
    }
    
    @GetMapping("/popular")
    public String popularAttachments(Model model) {
        model.addAttribute("attachments", attachmentService.getPopularAttachments());
        model.addAttribute("pageTitle", "Популярные файлы - Ветеринар Васильев А.В.");
        model.addAttribute("currentPage", "popular");
        
        return "attachments/popular";
    }
    
    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024));
        return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
    }
}
package com.veter96.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Veter96 - Главная");
        model.addAttribute("currentPage", "index");
        return "index";
    }

    @GetMapping("/section/{sectionId}")
    public String section(@PathVariable String sectionId, Model model) {
        String pageTitle = getSectionTitle(sectionId);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("currentPage", sectionId);
        model.addAttribute("sectionId", sectionId);
        return "section";
    }

    @GetMapping("/section/{sectionId}/{subId}")
    public String subsection(@PathVariable String sectionId, @PathVariable String subId, Model model) {
        String pageTitle = getSubsectionTitle(sectionId, subId);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("currentPage", sectionId + "_" + subId);
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("subId", subId);
        return "subsection";
    }

    private String getSectionTitle(String sectionId) {
        return switch (sectionId) {
            case "1" -> "1. Органы управления и приборы контроля";
            case "2" -> "2. Техническое обслуживание";
            case "3" -> "3. Двигатели";
            case "4" -> "4. Отопление, вентиляция";
            case "5" -> "5. Топливная система";
            case "6" -> "6. Системы пуска, зажигания";
            case "7" -> "7. Трансмиссия";
            case "8" -> "8. Тормозная система";
            case "9" -> "9. Ходовая часть";
            case "10" -> "10. Кузов";
            case "11" -> "11. Электрооборудование";
            case "12" -> "12. Основные неисправности";
            default -> "Неизвестная секция";
        };
    }

    private String getSubsectionTitle(String sectionId, String subId) {
        // This can be expanded with specific subsection titles
        return getSectionTitle(sectionId) + " - " + subId;
    }
}
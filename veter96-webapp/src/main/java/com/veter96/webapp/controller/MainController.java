package com.veter96.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Ветеринар - невролог/кардиолог в Екатеринбурге");
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
            case "qa" -> "Вопрос-ответ";
            case "services" -> "Перечень и стоимость услуг";
            case "reviews" -> "Отзывы";
            case "diseases" -> "170 заболеваний кожи собак и кошек с 1200 фото";
            case "about" -> "Обо мне";
            case "articles" -> "Ветеринарные статьи - переводы из научных журналов";
            case "neurology" -> "Неврология собак и кошек";
            case "cardiology" -> "Кардиология собак и кошек";
            case "dermatology" -> "Дерматология собак и кошек";
            case "contacts" -> "Контакты и время работы";
            default -> "Неизвестная секция";
        };
    }

    private String getSubsectionTitle(String sectionId, String subId) {
        // This can be expanded with specific subsection titles
        return getSectionTitle(sectionId) + " - " + subId;
    }
}
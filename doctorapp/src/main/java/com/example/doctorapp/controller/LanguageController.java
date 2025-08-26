package com.example.doctorapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class LanguageController {

    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam String lang, 
                                HttpServletRequest request, 
                                HttpServletResponse response) {
        
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            Locale locale = new Locale(lang);
            localeResolver.setLocale(request, response, locale);
        }
        
        // Redirect back to the referring page or home
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/test-i18n")
    public String testI18n() {
        return "test-i18n";
    }
}

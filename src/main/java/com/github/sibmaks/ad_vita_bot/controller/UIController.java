package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.constant.CommonConst;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import com.github.sibmaks.ad_vita_bot.service.TelegramBotStorage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;

/**
 * HTTP controller for getting moving around the app pages
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UIController {
    private final LocalisationService localisationService;
    private final TelegramBotStorage telegramBotStorage;

    /**
     * Index page, redirect on rooms page if client is authorized.
     * Otherwise, authorization page will be returned.
     *
     * @param request http servlet request
     * @return redirect link or index page
     */
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        var sessionId = getSessionId(request);
        if(sessionId != null) {
            return "redirect:/menu";
        }
        return "index";
    }

    @GetMapping("/menu")
    public String getMenu(HttpServletRequest request) {
        var sessionId = getSessionId(request);
        if(sessionId == null) {
            return "redirect:/index";
        }
        return "menu";
    }

    @GetMapping("/localizations")
    public String getLocalizations(HttpServletRequest request, Model model) {
        var sessionId = getSessionId(request);
        if(sessionId == null) {
            return "redirect:/index";
        }
        var localizations = localisationService.getLocalizations();
        model.addAttribute("localizations", localizations);
        return "localizations";
    }

    @GetMapping("/themes")
    public String getThemes(HttpServletRequest request, Model model) {
        var sessionId = getSessionId(request);
        if(sessionId == null) {
            return "redirect:/index";
        }
        var themes = telegramBotStorage.getThemes();
        model.addAttribute("themes", themes);
        return "themes";
    }

    @GetMapping("/theme/edit/{themeId}")
    public String editTheme(HttpServletRequest request, Model model, @PathVariable(name = "themeId") String themeId) {
        var sessionId = getSessionId(request);
        if(sessionId == null) {
            return "redirect:/index";
        }
        var theme = telegramBotStorage.findThemeById(Long.parseLong(themeId));
        model.addAttribute("theme", theme);
        return "theme";
    }

    @GetMapping("/theme/drawings/{themeId}")
    public String editDrawings(HttpServletRequest request, Model model, @PathVariable(name = "themeId") String themeId) {
        var sessionId = getSessionId(request);
        if(sessionId == null) {
            return "redirect:/index";
        }
        var theme = telegramBotStorage.findThemeById(Long.parseLong(themeId));
        model.addAttribute("drawings", theme.getDrawings());
        model.addAttribute("themeId", themeId);
        return "drawings";
    }

    @GetMapping("/bot_props")
    public String getRooms(HttpServletRequest request, Model model) {
        var sessionId = getSessionId(request);
        if(sessionId == null) {
            return "redirect:/index";
        }
        var deactivationDate = telegramBotStorage.getDeactivationDate();
        model.addAttribute("deactivation_date", deactivationDate.format(DateTimeFormatter.ISO_DATE));
        var invoiceProviderToken = telegramBotStorage.getInvoiceProviderToken();
        model.addAttribute("invoice_provider_token", invoiceProviderToken);
        return "bot_properties";
    }

    /**
     * Get session identifier from http request.
     * Looking for session id in headers and cookies
     *
     * @param request http servlet request
     * @return session identifier
     */
    private static String getSessionId(HttpServletRequest request) {
        var header = request.getHeader(CommonConst.HEADER_SESSION_ID);
        if(header == null && request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if(CommonConst.HEADER_SESSION_ID.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return header;
    }
}

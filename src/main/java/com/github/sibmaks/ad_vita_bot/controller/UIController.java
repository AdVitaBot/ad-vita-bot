package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.conf.AdminProperties;
import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import com.github.sibmaks.ad_vita_bot.entity.Drawing;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import com.github.sibmaks.ad_vita_bot.service.SessionService;
import com.github.sibmaks.ad_vita_bot.service.TelegramBotStorage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * HTTP controller for getting moving around the app pages
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Controller
public class UIController {
    private final LocalisationService localisationService;
    private final TelegramBotStorage telegramBotStorage;
    private final SessionService sessionService;
    private final AdminProperties.DrawingProps drawingProps;

    public UIController(LocalisationService localisationService,
                        TelegramBotStorage telegramBotStorage,
                        SessionService sessionService,
                        TelegramBotProperties telegramBotProperties) {
        this.localisationService = localisationService;
        this.telegramBotStorage = telegramBotStorage;
        this.sessionService = sessionService;
        var adminProps = telegramBotProperties.getAdmin();
        this.drawingProps = adminProps.getDrawing();
    }

    /**
     * Index page, redirect on rooms page if client is authorized.
     * Otherwise, authorization page will be returned.
     *
     * @param request http servlet request
     * @return redirect link or index page
     */
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        if (isNotAuthorized(request)) {
            return "index";
        }
        return "redirect:/menu";
    }

    @GetMapping("/menu")
    public String getMenu(HttpServletRequest request) {
        if(isNotAuthorized(request)) {
            return "redirect:/";
        }
        return "menu";
    }

    @GetMapping("/localizations")
    public String getLocalizations(HttpServletRequest request, Model model) {
        if(isNotAuthorized(request)) {
            return "redirect:/";
        }
        var localizations = localisationService.getLocalizations();
        model.addAttribute("localizations", localizations);
        return "localizations";
    }

    @GetMapping("/themes")
    public String getThemes(HttpServletRequest request, Model model) {
        if(isNotAuthorized(request)) {
            return "redirect:/";
        }
        var themes = telegramBotStorage.getThemes();
        model.addAttribute("themes", themes);
        return "themes";
    }

    @GetMapping("/theme/edit/{themeId}")
    public String editTheme(HttpServletRequest request, Model model, @PathVariable(name = "themeId") String themeId) {
        if(isNotAuthorized(request)) {
            return "redirect:/";
        }
        var theme = telegramBotStorage.findThemeById(Long.parseLong(themeId));
        model.addAttribute("theme", theme);
        return "theme";
    }

    @GetMapping("/theme/drawings/{themeId}")
    public String editDrawings(HttpServletRequest request, Model model, @PathVariable(name = "themeId") String themeId) {
        if(isNotAuthorized(request)) {
            return "redirect:/";
        }
        var theme = telegramBotStorage.findThemeById(Long.parseLong(themeId));
        var activeDrawings = theme.getDrawings().stream()
                .filter(Drawing::isActive)
                .collect(Collectors.toList());
        model.addAttribute("drawings", activeDrawings);
        model.addAttribute("themeId", themeId);
        return "drawings";
    }

    @GetMapping("/theme/drawings/{themeId}/add")
    public String addDrawings(HttpServletRequest request, Model model, @PathVariable(name = "themeId") String themeId) {
        if(isNotAuthorized(request)) {
            return "redirect:/";
        }
        model.addAttribute("themeId", themeId);
        model.addAttribute("maxDrawingSize", drawingProps.getMaxFileSize() / 1024);
        return "drawing";
    }

    @GetMapping("/bot_props")
    public String getRooms(HttpServletRequest request, Model model) {
        if(isNotAuthorized(request)) {
            return "redirect:/";
        }
        var deactivationDate = telegramBotStorage.getDeactivationDate();
        model.addAttribute("deactivation_date", deactivationDate.format(DateTimeFormatter.ISO_DATE));
        var invoiceProviderToken = telegramBotStorage.getInvoiceProviderToken();
        model.addAttribute("invoice_provider_token", invoiceProviderToken);
        return "bot_properties";
    }

    @GetMapping("/account")
    public String account(HttpServletRequest request) {
        if(isNotAuthorized(request)) {
            return "redirect:/";
        }
        return "account";
    }

    private boolean isNotAuthorized(HttpServletRequest request) {
        var session = sessionService.getSession(request);
        if(session == null) {
            return true;
        }
        return !session.isAuthorized();
    }
}

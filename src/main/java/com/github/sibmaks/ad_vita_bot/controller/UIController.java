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
            return "redirect:/localizations";
        }
        return "index";
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

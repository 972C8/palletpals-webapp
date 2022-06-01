package ch.fhnw.palletpals.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SpaErrorController implements ErrorController {

    @RequestMapping("/error")
    public Object error(HttpServletRequest request, HttpServletResponse response) {
        // place your additional code here (such as error logging...)
        if (request.getMethod().equalsIgnoreCase(HttpMethod.GET.name()) && !request.getRequestURI().contains("/api")) {
            return "forward:/index.html"; // forward to static SPA html resource.
        } else {
            return ResponseEntity.notFound().build(); // or your REST 404 blabla...
        }
    }
}
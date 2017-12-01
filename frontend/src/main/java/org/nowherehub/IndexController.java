package org.nowherehub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Value("${application.apiUrl}")
    private String apiUrl;

    @GetMapping("/{path:(?!.*\\.js|.*\\.css|.*\\.jpg|api|.*\\.ico|.*\\.json).*$}")
    public String index(Model model) {

        LOGGER.debug("Calling index with apiUrl={}", apiUrl);
        model.addAttribute("apiUrl", apiUrl);
        return "index";
    }

    @GetMapping("/**/{path:(?!.*\\.js|.*\\.css|.*\\.jpg|api|.*\\.woff|.*\\.woff2|.*.gif|.*.ttf|.*\\.ico|.*\\.json).*$}")
    public String indexLevel(Model model) {
        LOGGER.debug("Calling index with apiUrl={}", apiUrl);

        model.addAttribute("apiUrl", apiUrl);
        return "index";
    }

}

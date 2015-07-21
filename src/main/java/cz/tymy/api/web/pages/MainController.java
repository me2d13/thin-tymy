package cz.tymy.api.web.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by win7 on 10.7.2015.
 */
@Controller
public class MainController {
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(ModelMap model) {
        model.addAttribute("includePage", "/WEB-INF/jsp/main.jsp");
        return "skeleton";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String empty(ModelMap model) {
        return "forward:/main";
    }


}

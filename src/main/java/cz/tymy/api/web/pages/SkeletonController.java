package cz.tymy.api.web.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by win7 on 10.7.2015.
 */
@Controller
public class SkeletonController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String admin(ModelMap model) {
        String page = "main"; //TODO: check query path, set home on empty
        model.addAttribute("includePage", page);
        return "skeleton";
    }

}

package cz.tymy.thin.web.pages;

import cz.tymy.model.Discussion;
import cz.tymy.model.RestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by win7 on 10.7.2015.
 */
@Controller
public class MainController extends AbstractController {

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(ModelMap model, HttpSession session, HttpServletRequest request) {
        checkLogin(request, session, model);
        addCommonVars(model, request);
        addJavascript(model, "/static/js/main.js");
        addJavascript(model, "/cal/bic_calendar.js");
        addCss(model, "/cal/bic_calendar.css");
        RestResponse<List<Discussion>> discussions = restTemplate.getForObject(apiUrl("discussions/accessible", request, session), RestResponse.class);
        model.addAttribute("discussions", discussions);
        model.addAttribute("includePage", "/WEB-INF/jsp/main.jsp");
        return "skeleton";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String empty(ModelMap model) {
        return "forward:/main";
    }


}

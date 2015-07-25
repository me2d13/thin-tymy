package cz.tymy.api.web.pages;

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
 * Created by win7 on 25.7.2015.
 */
@Controller
public class DebugController extends AbstractController {

    @RequestMapping(value = "/debug13", method = RequestMethod.GET)
    public String main(ModelMap model, HttpSession session, HttpServletRequest request) {
        addCommonVars(model);
        return "debug";
    }

}

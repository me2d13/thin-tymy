package cz.tymy.thin.web.pages;

import cz.tymy.thin.dto.LoginForm;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by win7 on 7.9.2015.
 */
@Controller
public class AccessController extends AbstractController {
    private static Logger LOG = Logger.getLogger(AccessController.class);

    @ModelAttribute("lf")
    public LoginForm loadEmptyLoginForm() {
        return new LoginForm();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String main(ModelMap model, HttpSession session, HttpServletRequest request) {
        addCommonVars(model, request);
        return "login";
    }

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public String doLogin(@ModelAttribute("lf") LoginForm loginForm, BindingResult result, ModelMap model, HttpSession session, HttpServletRequest request) {
        LOG.debug(String.format("Login request for user [%s] with hashed password [%s]", loginForm.getLogin(), loginForm.getPassword()));
        return "debug";
    }

}

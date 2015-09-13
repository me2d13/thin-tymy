package cz.tymy.thin.web.pages;

import com.google.common.collect.ImmutableList;
import cz.tymy.model.RestResponse;
import cz.tymy.model.User;
import cz.tymy.thin.dto.LoginForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by win7 on 7.9.2015.
 */
@Controller
public class AccessController extends AbstractController {
    private static final String INVALID_CREDENTIALS = "Neplatné jméno nebo heslo.";
    private static final String LOGGED_OUT = "Odhlášeno.";
    private static Logger LOG = Logger.getLogger(AccessController.class);

    @ModelAttribute("lf")
    public LoginForm loadEmptyLoginForm() {
        return new LoginForm();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model, HttpSession session, HttpServletRequest request) {
        addCommonVars(model, request);
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(ModelMap model, HttpSession session, HttpServletRequest request) {
        session.invalidate();
        model.put("infos", ImmutableList.of(LOGGED_OUT));
        return "login";
    }

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public String doLogin(@ModelAttribute("lf") LoginForm loginForm, BindingResult result, ModelMap model, HttpSession session, HttpServletRequest request) {
        //LOG.debug(String.format("Login request for user [%s] with hashed password [%s]", loginForm.getLogin(), loginForm.getPassword()));
        String sessionKey = (String) session.getAttribute(ATTR_SESSION_KEY);
        if (StringUtils.isNotBlank(sessionKey)) {
            return "redirect:/main";
        }
        if (StringUtils.isBlank(loginForm.getLogin())) {
            model.put("errors", ImmutableList.of(INVALID_CREDENTIALS));
            return "/login";
        }
        String url = apiUrl("login", request);
        // call rest
        if (StringUtils.isNotBlank(url)) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("login", loginForm.getLogin());
            params.add("password", loginForm.getPassword());
            RestResponse<Map<String, String>> userResponse = restTemplate.postForObject(url, params, RestResponse.class);
            if (StringUtils.isNotBlank(userResponse.getSessionKey())) {
                session.setAttribute(ATTR_SESSION_KEY, userResponse.getSessionKey());
                session.setAttribute(ATTR_USER_LOGIN, userResponse.getData().get("login"));
                return "redirect:/main";
            } else {
                LOG.warn(String.format("Can not login user based on user name %s. Status %s, message %s.", loginForm.getLogin(),
                        userResponse.getStatus().toString(), concatMessages(userResponse.getMessages())));
            }
        } else {
            LOG.warn("Cannot build login request URL.");
        }
        model.put("errors", ImmutableList.of(INVALID_CREDENTIALS));
        return "/login";
    }

}

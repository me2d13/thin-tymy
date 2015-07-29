package cz.tymy.api.web.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tymy.api.dto.PostForm;
import cz.tymy.model.RestResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by win7 on 27.7.2015.
 */
@Controller
public class DiscussionController extends AbstractController {

    private static Logger LOG = Logger.getLogger(DiscussionController.class);

    @RequestMapping(value = "/ds/{dsId}", method = RequestMethod.GET)
    public String show(@ModelAttribute("post")PostForm postForm, BindingResult bindingResult, ModelMap model, @PathVariable Integer dsId, HttpSession session, HttpServletRequest request) {
        if (!checkLogin(request, session, model)) {
            return String.format("redirect:%s", getURL(request, false));
        }
        addCommonVars(model, request);
        addTxt("discussion", model, request, session);
        //addJavascript(model, "main.js");
        JsonNode discussion = restTemplate.getForObject(apiUrl(String.format("discussion/%d/bb/1", dsId),
                request, session), JsonNode.class);
        model.addAttribute(ATTR_PAGE_TITLE, discussion.path("data").path("discussion").path("caption").asText());
        ObjectMapper mapper = new ObjectMapper();
        try {
            //could be done but I would need String-->DateTime converter
            //model.addAttribute("ds", mapper.treeToValue(discussion.path("data"), DiscussionPosts.class));
            model.addAttribute("ds", mapper.treeToValue(discussion, RestResponse.class));
        } catch (JsonProcessingException e) {
            LOG.error("Cannot convert REST response to objects", e);
        }
        model.addAttribute("includePage", "/WEB-INF/jsp/ds.jsp");
        return "skeleton";
    }

    @RequestMapping(value = "/ds/{dsId}", params = "save", method = RequestMethod.POST)
    public String post(@ModelAttribute("post")PostForm postForm, @PathVariable Integer dsId, HttpSession session,
                       HttpServletRequest request, BindingResult bindingResult) {
        return "redirect:/ds/"+dsId.toString();
    }

    @RequestMapping(value = "/ds/{dsId}", params = "preview", method = RequestMethod.POST)
    public String preview(@ModelAttribute("post")PostForm postForm, @PathVariable Integer dsId, HttpSession session,
                       HttpServletRequest request, BindingResult bindingResult) {
        return "redirect:/ds/"+dsId.toString();
    }

}

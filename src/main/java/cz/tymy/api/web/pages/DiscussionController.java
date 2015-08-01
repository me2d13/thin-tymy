package cz.tymy.api.web.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tymy.api.dto.PostForm;
import cz.tymy.model.Post;
import cz.tymy.model.RestResponse;
import cz.tymy.model.RestResponseStatus;
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

    private static final String ATTR_PREVIEW = "preview";
    private static Logger LOG = Logger.getLogger(DiscussionController.class);

    @RequestMapping(value = "/ds/{dsId}", method = RequestMethod.GET)
    public String show(@ModelAttribute("post")PostForm postForm, ModelMap model, @PathVariable Integer dsId, HttpSession session, HttpServletRequest request) {
        if (!checkLogin(request, session, model)) {
            return String.format("redirect:%s", getURL(request, false));
        }
        addCommonVars(model, request);
        addTxt("discussion", model, request, session);
        //addJavascript(model, "main.js");
        loadDiscussionToModel(model, dsId, session, request);
        model.addAttribute("includePage", "/WEB-INF/jsp/ds.jsp");
        return "skeleton";
    }

    private void loadDiscussionToModel(ModelMap model, @PathVariable Integer dsId, HttpSession session, HttpServletRequest request) {
        JsonNode discussion = restTemplate.getForObject(apiUrl(String.format("discussion/%d/html/1", dsId),
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
    }

    @RequestMapping(value = "/ds/{dsId}", params = "save", method = RequestMethod.POST)
    public String post(@ModelAttribute("post")PostForm postForm, BindingResult bindingResult, @PathVariable Integer dsId, HttpSession session,
                       HttpServletRequest request, ModelMap model) {
        Post post = new Post();
        post.setPost(postForm.getPost());
        RestResponse result = restTemplate.postForObject(apiUrl(String.format("discussion/%d/post", dsId), request, session), post, RestResponse.class);
        if (result == null || result.getStatus() == RestResponseStatus.ERROR) {
            addCommonVars(model, request);
            addTxt("discussion", model, request, session);
            addError(model, result.getStatusMessage());
            loadDiscussionToModel(model, dsId, session, request);
            model.addAttribute("includePage", "/WEB-INF/jsp/ds.jsp");
            return "skeleton";
        }
        return "redirect:/ds/"+dsId.toString();
    }

    @RequestMapping(value = "/ds/{dsId}", params = "preview", method = RequestMethod.POST)
    public String preview(@ModelAttribute("post")PostForm postForm, @PathVariable Integer dsId, HttpSession session,
                       HttpServletRequest request, ModelMap model) {
        addCommonVars(model, request);
        addTxt("discussion", model, request, session);
        Post post = new Post();
        post.setPost(postForm.getPost());
        RestResponse result = restTemplate.postForObject(apiUrl("discussion/preview", request, session), post, RestResponse.class);
        if (result != null && result.getStatus() == RestResponseStatus.OK) {
            model.addAttribute(ATTR_PREVIEW, result.getData().toString());
        } else if (result != null) {
            addError(model, result.getStatusMessage());
        } else {
            LOG.error("Null response from preview API call");
        }
        loadDiscussionToModel(model, dsId, session, request);
        model.addAttribute("includePage", "/WEB-INF/jsp/ds.jsp");
        return "skeleton";
    }

}

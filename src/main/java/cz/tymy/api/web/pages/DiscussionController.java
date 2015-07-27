package cz.tymy.api.web.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tymy.model.Discussion;
import cz.tymy.model.DiscussionPosts;
import cz.tymy.model.RestResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by win7 on 27.7.2015.
 */
@Controller
public class DiscussionController extends AbstractController {

    private static Logger LOG = Logger.getLogger(DiscussionController.class);

    @RequestMapping(value = "/ds/{dsId}", method = RequestMethod.GET)
    public String main(ModelMap model, @PathVariable Integer dsId, HttpSession session, HttpServletRequest request) {
        if (!checkLogin(request, session, model)) {
            return String.format("redirect:%s", getURL(request, false));
        }
        addCommonVars(model, request);
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

}

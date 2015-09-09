package cz.tymy.thin.web.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tymy.model.RestResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by win7 on 19.8.2015.
 */
@Controller
public class EventController extends AbstractController {
    private static Logger LOG = Logger.getLogger(EventController.class);

    @RequestMapping(value = "/event/{evId}", method = RequestMethod.GET)
    public String show(ModelMap model, @PathVariable Integer evId, HttpSession session, HttpServletRequest request) {
        checkLogin(request, session, model);
        addCommonVars(model, request);
        addTxt("event_detail", model, request, session);
        addJavascript(model, "/static/js/event.js");
        addJavascript(model, "/static/js/Sortable.js");
        //loadEventToModel(model, evId, session, request);
        // let's call the API in js
        model.addAttribute("eventId", evId);
        model.addAttribute("includePage", "/WEB-INF/jsp/event.jsp");
        return "skeleton";
    }

    private void loadEventToModel(ModelMap model, @PathVariable Integer evId, HttpSession session, HttpServletRequest request) {
        JsonNode event = restTemplate.getForObject(apiUrl(String.format("event/%d", evId),
                request, session), JsonNode.class);
        model.addAttribute(ATTR_PAGE_TITLE, event.path("data").path("caption").asText());
        ObjectMapper mapper = new ObjectMapper();
        try {
            //could be done but I would need String-->DateTime converter
            //model.addAttribute("ds", mapper.treeToValue(discussion.path("data"), DiscussionPosts.class));
            model.addAttribute("event", mapper.treeToValue(event, RestResponse.class));
        } catch (JsonProcessingException e) {
            LOG.error("Cannot convert REST response to objects", e);
        }
    }


}

package cz.tymy.api.web.pages;

import com.fasterxml.jackson.databind.JsonNode;
import cz.tymy.model.Discussion;
import cz.tymy.model.RestResponse;
import cz.tymy.model.RestResponseStatus;
import cz.tymy.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by win7 on 23.7.2015.
 */
public class AbstractController {

    private static final String ATTR_TEXTS = "txt";
    private static final String ATTR_ERRORS = "errors";
    @Value("${fixedTeamName:}")
    private String fixedTeamName;

    @Value("${teamNamePattern:}")
    private String teamNamePattern;

    @Value("${apiUrl:}")
    private String fixedApiUrl;

    private static final String ATTR_TEAM_SYS_NAME = "teamSysName";
    private static final String ATTR_SESSION_KEY = "TSID";
    private static final String PHP_SESSION_ID = "PHPSESSID";
    private static final String ATTR_JS_FILES = "jsFiles";
    private static final String ATTR_CSS_FILES = "cssFiles";
    protected static final String ATTR_PAGE_TITLE = "pageTitle";

    private static Logger LOG = Logger.getLogger(AbstractController.class);

    protected RestTemplate restTemplate = new RestTemplate();

    protected void addCommonVars(ModelMap model, HttpServletRequest sr) {
        String teamSysName = getTeamSysName(sr);
        model.addAttribute(ATTR_TEAM_SYS_NAME, teamSysName);
        model.addAttribute(ATTR_PAGE_TITLE, String.format("%s.tymy.cz", teamSysName));
    }

    protected String getTeamSysName(HttpServletRequest sr) {
        if (org.springframework.util.StringUtils.hasText(fixedTeamName)) {
            return fixedTeamName;
        }
        if (org.springframework.util.StringUtils.hasText(teamNamePattern)) {
            Pattern p = Pattern.compile(teamNamePattern);
            Matcher m = p.matcher(sr.getRequestURL().toString());
            if (m.matches() && m.groupCount() == 1) {
                return m.group(1);
            } else {
                LOG.warn("No regexp match for " + sr.getRequestURL().toString() + " with pattern " + teamNamePattern);
            }
        }
        return null;
    }

    protected boolean checkLogin(HttpServletRequest request, HttpSession session, ModelMap model) {
        String sessionKey = (String) session.getAttribute(ATTR_SESSION_KEY);
        if (StringUtils.isNotBlank(sessionKey)) {
            return true;
        }
        String phpSessionId = request.getParameter(PHP_SESSION_ID);
        if (StringUtils.isBlank(phpSessionId)) {
            // most likely, it usually comes in cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie c : cookies) {
                    if (PHP_SESSION_ID.equalsIgnoreCase(c.getName())) {
                        phpSessionId = c.getValue();
                        break;
                    }
                }
            }
        }
        if (StringUtils.isBlank(phpSessionId)) {
            LOG.warn(PHP_SESSION_ID + " found neither in parameter nor in cookie.");
            return false;
        }
        String url = apiUrl(String.format("loginPhp/?%s=%s", PHP_SESSION_ID, phpSessionId), request);
        // call rest
        if (StringUtils.isNotBlank(url)) {
            RestResponse<User> userResponse = restTemplate.getForObject(url, RestResponse.class);
            if (StringUtils.isNotBlank(userResponse.getSessionKey())) {
                session.setAttribute(ATTR_SESSION_KEY, userResponse.getSessionKey());
                return true;
            } else {
                LOG.warn(String.format("Can not login user based on php session %s. Status %s, message %s.", phpSessionId,
                        userResponse.getStatus().toString(), concatMessages(userResponse.getMessages())));
            }
        } else {
            LOG.warn("Cannot build login request URL.");
        }
        return false;
    }

    private String concatMessages(Map<String, String> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            return "";
        } else {
            return messages.entrySet().stream().map(Object::toString).collect(Collectors.joining(", "));
        }
    }

    protected void addJavascript(ModelMap model, String file) {
        List<String> jsFiles = (List<String>) model.getOrDefault(ATTR_JS_FILES, new ArrayList<String>());
        jsFiles.add(file);
        model.addAttribute(ATTR_JS_FILES, jsFiles);
    }

    protected void addCss(ModelMap model, String file) {
        List<String> cssFiles = (List<String>) model.getOrDefault(ATTR_CSS_FILES, new ArrayList<String>());
        cssFiles.add(file);
        model.addAttribute(ATTR_CSS_FILES, cssFiles);
    }

    protected void addError(ModelMap model, String message) {
        List<String> errors = (List<String>) model.getOrDefault(ATTR_ERRORS, new ArrayList<String>());
        errors.add(message);
        model.addAttribute(ATTR_ERRORS, errors);
    }

    protected String apiUrl(String apiPage, HttpServletRequest request) {
        return apiUrl(apiPage, request, null);
    }

    protected String apiUrl(String apiPage, HttpServletRequest request, HttpSession session) {
        String result = null;
        if (session != null) {
            String tsid = (String) session.getAttribute(ATTR_SESSION_KEY);
            if (StringUtils.isNotBlank(tsid)) {
                if (apiPage.contains("?")) {
                    apiPage += String.format("&%s=%s", ATTR_SESSION_KEY, tsid);
                } else {
                    if (apiPage.endsWith("/")) {
                        apiPage += String.format("?%s=%s", ATTR_SESSION_KEY, tsid);
                    } else {
                        apiPage += String.format("/?%s=%s", ATTR_SESSION_KEY, tsid);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(fixedApiUrl)) {
            result = String.format("%s/%s", fixedApiUrl, apiPage);
        } else {
            // get from request
            result = String.format("%s/api/%s", getURL(request, false), apiPage);
        }
        LOG.debug(String.format("Querying API [%s]", result));
        return result;
    }

    protected void addTxt(String domain, ModelMap model, HttpServletRequest request, HttpSession session) {
        RestResponse<Object> txt = restTemplate.getForObject(apiUrl(String.format("caption/cs/%s/", domain),
                request, session), RestResponse.class);
        if (txt != null && txt.getStatus() == RestResponseStatus.OK) {
            model.addAttribute(ATTR_TEXTS, txt.getMessages());
        } else {
            LOG.error("Cannot fetch texts.");
        }
    }


    public static String getURL(HttpServletRequest req, boolean withPath) {

        String scheme = req.getScheme();             // http
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();        // 80
        String contextPath = req.getContextPath();   // /mywebapp
        String servletPath = req.getServletPath();   // /servlet/MyServlet
        String pathInfo = req.getPathInfo();         // /a/b;c=123
        String queryString = req.getQueryString();          // d=789

        // Reconstruct original requesting URL
        StringBuffer url = new StringBuffer();
        url.append(scheme).append("://").append(serverName);

        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }

        if (withPath) {
            url.append(contextPath).append(servletPath);

            if (pathInfo != null) {
                url.append(pathInfo);
            }
            if (queryString != null) {
                url.append("?").append(queryString);
            }
        }
        return url.toString();
    }

}

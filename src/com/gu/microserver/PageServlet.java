package com.gu.microserver;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class PageServlet extends ConfiguredServlet {
    private String template;

    public PageServlet(String template) {

        this.template = template;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //load the template
        URL htmlPath = getClass().getClassLoader().getResource(template);
        response.setContentType("text/html; charset=UTF-8");

        String html = IOUtils.toString(htmlPath.openStream());
        String urlString = getProperty("componentUrl");

        //add any parameters to the url
        Map<String, String> params = extractParametersFromConfig();

        String existingQueryString = request.getQueryString();
        if (existingQueryString == null)
            existingQueryString = "";

        urlString += "?" + existingQueryString;
        for (String name : params.keySet()) {
            urlString += name + "=" + URLEncoder.encode(params.get(name), "UTF-8") + "&";
        }

        //yes I really want a sysout here, not logging, it is feedback to the user
        System.out.println("Fetching HTML from: " + urlString);

        //load the html of the component
        URL htmlUrl = new URL(urlString);
        String component = IOUtils.toString(htmlUrl.openStream());

        //replace placeholder with the component
        html = html.replace("[PLACEHOLDER]", component);

        response.getWriter().print(html);
    }

    private Map<String, String> extractParametersFromConfig() throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        for (Map.Entry entry : getEntries()) {
            if (!entry.getKey().equals("componentUrl") && !entry.getKey().equals("cssUrl") && !entry.getKey().equals("javascriptUrl")) {
                if (entry.getValue() != null && ((String) entry.getValue()).trim().length() > 0) {
                    params.put((String) entry.getKey(), ((String) entry.getValue()).trim());
                }
            }
        }
        return params;
    }
}
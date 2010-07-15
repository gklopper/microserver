package com.gu.microserver;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ResourceServlet extends ConfiguredServlet {
    private String contentType;
    private String property;

    public ResourceServlet(String contentType, String property) {
        this.contentType = contentType;
        this.property = property;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        

        response.setContentType(contentType);

        String resourceUrl = getProperty(property);

        if (resourceUrl == null || resourceUrl.trim().isEmpty()) {
            return;
        }

        URL resource = new URL(resourceUrl);

        System.out.println("Serving resource of type " + contentType + " : " + resourceUrl);

        IOUtils.copy(resource.openStream(), response.getWriter());
    }
}

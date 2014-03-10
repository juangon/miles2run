package org.milestogo.framework;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


public class View implements Viewable {
    /**
     * If left unspecified, the default name of the model in the request attributes
     */
    public static final String DEFAULT_MODEL_NAME = "model";

    /** */
    private String path;
    private Object model;
    private String modelName;
    private boolean redirect;


    /**
     * Creates a view without a model.
     *
     * @param path will be dispatched to using the servlet container; it should
     *             have a leading /.
     */
    public View(String path) {
        this(path, null, null);
    }

    /** */
    public View(String path, Object model) {
        this(path, model, DEFAULT_MODEL_NAME);
    }

    /** */
    public View(String path, Object model, String modelName) {
        this.path = path;
        this.model = model;
        this.modelName = modelName;
    }

    /** */
    public View(String path, Object model, String modelName, boolean redirect) {
        this.path = path;
        this.model = model;
        this.modelName = modelName;
        this.redirect = redirect;
    }

    public String getPath() {
        return this.path;
    }

    public Object getModel() {
        return this.model;
    }

    public String getModelName() {
        return this.modelName;
    }

    /**
     * Sets up the model in the request attributes, creates a dispatcher, and forwards the request.
     */
    public String render(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, WebApplicationException {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setTemplateMode("LEGACYHTML5");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable(this.modelName, this.model);
        return templateEngine.process(this.path, context);
    }

    public boolean isRedirect() {
        return this.redirect;
    }

}

package org.milestogo.framework;

import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

@Provider
public class ViewWriter implements MessageBodyWriter<Object> {

    private ViewResolver viewResolver = new ViewResolver();

    @Override
    public long getSize(Object obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return viewResolver.isResolvable(type, genericType, annotations);
    }

    @Override
    public void writeTo(Object obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {
        Viewable viewingPleasure = viewResolver.getView(obj, type, genericType, annotations);

        if (viewingPleasure == null)
            throw new InternalServerErrorException("No View annotation found for object of type " + type.getName());

        HttpServletRequest request = ResteasyProviderFactory.getContextData(HttpServletRequest.class);
        HttpServletResponse response = ResteasyProviderFactory.getContextData(HttpServletResponse.class);
        response.addCookie(new Cookie("JSESSIONID",request.getSession().getId()));
        try {
            if(viewingPleasure.isRedirect()){
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + viewingPleasure.getPath());
            }   else{
                String processedTemplate = viewingPleasure.render(request, response);
                String charset = mediaType.getParameters().get("charset");
                if (charset == null) entityStream.write(processedTemplate.getBytes());
                else entityStream.write(processedTemplate.getBytes(charset));
            }
        } catch (ServletException ex) {
            throw new WebApplicationException(ex);
        }
    }

    public ViewResolver getViewResolver() {
        return viewResolver;
    }

    public void setViewResolver(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

}

package org.milestogo.framework;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import org.jboss.resteasy.plugins.server.servlet.HttpServletResponseWrapper;
import org.jboss.resteasy.spi.HttpResponse;


@WebFilter(displayName = "Jug",urlPatterns = "/*",initParams = {@WebInitParam(name = "javax.ws.rs.Application",value = "org.milestogo.resources.config.RestConfig")})
public class JugFilterDispatcher extends FilterDispatcher {



   @Override
   public HttpResponse createResteasyHttpResponse(HttpServletResponse response) {
       return new HttpServletResponseWrapper(response, getDispatcher().getProviderFactory()) {

           protected OutputStream getSuperOuptutStream() throws IOException {
               return super.getOutputStream();
           }

           public OutputStream getOutputStream() throws IOException {
               return new OutputStream() {
                   @Override
                   public void write(int b) throws IOException {
                       getSuperOuptutStream().write(b);
                   }

                   @Override
                   public void write(byte[] b) throws IOException {
                       getSuperOuptutStream().write(b);
                   }

                   @Override
                   public void write(byte[] b, int off, int len)
                           throws IOException {
                       getSuperOuptutStream().write(b, off, len);
                   }

                   @Override
                   public void flush() throws IOException {
                       getSuperOuptutStream().flush();
                   }

                   @Override
                   public void close() throws IOException {
                       getSuperOuptutStream().close();
                   }
               };
           }
           
           
       };
   }

}

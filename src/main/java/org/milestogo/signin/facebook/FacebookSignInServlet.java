package org.milestogo.signin.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signin/facebook")
public class FacebookSigninServlet extends HttpServlet {

    @Inject
    private FacebookFactory facebookFactory;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userExistsInSession(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        try {
            Facebook facebook = facebookFactory.getInstance();
            request.getSession().setAttribute("facebook", facebook);
            StringBuffer callbackUrl = request.getRequestURL();
            int index = callbackUrl.lastIndexOf("/signin/");
            callbackUrl.replace(index, callbackUrl.length(), "").append("/callback/facebook");
            response.sendRedirect(facebook.getOAuthAuthorizationURL(callbackUrl.toString()));
        } catch (Exception e) {
            throw new RuntimeException("Unable to get Facebook authentication url", e);
        }

    }

    private boolean userExistsInSession(HttpSession session) {
        if (session == null) {
            return false;
        }
        return session.getAttribute("username") != null ? true : false;

    }
}

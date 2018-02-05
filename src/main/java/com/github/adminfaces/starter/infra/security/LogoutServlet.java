package com.github.adminfaces.starter.infra.security;

import com.github.adminfaces.template.config.AdminConfig;
import com.github.adminfaces.template.util.Constants;
import org.apache.shiro.SecurityUtils;
import org.omnifaces.util.Faces;

import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "logoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Inject
    AdminConfig adminConfig;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtils.getSubject().logout();
        ExternalContext ec = Faces.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + getLoginPage());
    }

    private String getLoginPage() {
        String loginPage = adminConfig.getLoginPage();
        if (loginPage == null || "".equals(loginPage)) {
            loginPage = Constants.DEFAULT_INDEX_PAGE;
        }
        if (!loginPage.startsWith("/")) {
            loginPage = "/" + loginPage;
        }
        return loginPage;
    }
}
package com.github.adminfaces.starter.infra.security;

import com.github.adminfaces.template.session.AdminSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;

/**
 * Created by rmpestano on 12/20/14.
 * <p>
 * This is just a login example.
 * <p>
 * AdminSession uses isLoggedIn to determine if user must be redirect to login page or not.
 * By default AdminSession isLoggedIn always resolves to true so it will not try to redirect user.
 * <p>
 * If you already have your authorization mechanism which controls when user must be redirect to initial page or logon
 * you can skip this class.
 */
@Named
@SessionScoped
@Specializes
public class LogonMB extends AdminSession implements Serializable {

    private String email;
    private String password;
    private boolean remember;

    private Logger log = Logger.getLogger(LogonMB.class.getName());


    public void login() throws IOException {
        UsernamePasswordToken token = new UsernamePasswordToken(email,
                password);

        // "Remember Me" built-in:
        token.setRememberMe(remember);

        Subject currentUser = SecurityUtils.getSubject();

        log.log(Level.INFO, "Submitting login with username of {0} and password of {1}", new Object[]{email, password});
        try {
            currentUser.login(token);
            addDetailMessage("Logged in successfully as <b>" + email + "</b>");
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect("index.xhtml");
       /* you can handle specific exceptions
        } catch (UnknownAccountException uae) {
            //username wasn't in the system, show them an error message?
            throw uae;
        } catch (IncorrectCredentialsException ice) {
            //password didn't match, try again?
            throw ice;
        } catch (LockedAccountException lae) {
            //account for that username is locked - can't login.  Show them a message?
            throw lae;*/
        } catch (Exception e) {
            // Could catch a subclass of AuthenticationException if you like
            log.log(Level.SEVERE, e.getMessage(), e);
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Messages.addFatal(null,"Login Failed:" + e.getMessage());
        }
    }

    @Override
    public boolean isLoggedIn() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public String getCurrentUser() {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }

}

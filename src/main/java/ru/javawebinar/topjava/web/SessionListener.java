package ru.javawebinar.topjava.web;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        session.setAttribute("dateFrom", "");
        session.setAttribute("dateTo", "");
        session.setAttribute("timeFrom", "");
        session.setAttribute("timeTo", "");
    }
}

package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    UserService userService = UserServiceFactory.getUserService();

    // If user is not logged in, show a login form (could also redirect to a login page)
    if (!userService.isUserLoggedIn()) {
      String loginUrl = userService.createLoginURL("/");
      out.println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
      return;
    }

    // If user has not set a nickname, redirect to nickname page
    String nickname = getUserNickname(userService.getCurrentUser().getUserId());
    if (nickname == null) {
      response.sendRedirect("/nickname");
      return;
    }

    // User is logged in and has a nickname, so the request can proceed
    String logoutUrl = userService.createLogoutURL("/");
    out.println("<h1>Home</h1>");
    out.println("<p>Hello " + nickname + "!</p>");
    out.println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
    out.println("<p>Change your nickname <a href=\"/nickname\">here</a>.</p>");
  }

  /** Returns the nickname of the user with id, or null if the user has not set a nickname. */
  private String getUserNickname(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }
}
// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  //Creating an arraylist with my own created comment object
  private ArrayList<Comment> comments;


  //Initializing variables with manual test data
  @Override
  public void init() {
    comments = new ArrayList<>();
    comments.add(new Comment("Joe", "test1"));
    comments.add(new Comment("Joe", "test2"));
    comments.add(new Comment("Joe", "test3"));
    comments.add(new Comment("Joe", "test4"));
    comments.add(new Comment("Joe", "test5"));
    comments.add(new Comment("Joe", "test6"));
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String author = getParameter(request, "name-input", "");
    String message = getParameter(request, "message-input", "");
    //Taking system time for keepijg track of timestamps of comments
    long timestamp = System.currentTimeMillis();

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect(".");
      return;
    }

    String email = userService.getCurrentUser().getEmail();
    //Creating Datastore Entity
    Entity taskEntity = new Entity("Comment");
    taskEntity.setProperty("email", email);
    taskEntity.setProperty("author", author);
    taskEntity.setProperty("message", message);
    taskEntity.setProperty("timestamp", timestamp);
    //Storing the new comment
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    //Redirecting user back to current page
    response.sendRedirect(".");
    // Respond with the result on /data page
    response.setContentType("text/html;");
    response.getWriter().println(author + ": your message, " + message + ", has been added!");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //Loading data from datastore
    Query query = new Query("Comment").addSort("timestamp", SortDirection.ASCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Comment> output = new ArrayList<>(comments);
    for (Entity entity : results.asIterable()) {
      String author = (String) entity.getProperty("author");
      String message = (String) entity.getProperty("message");

      Comment comment = new Comment(author, message);
      output.add(comment);
    }

    //Using Gson to convert Comment object to json
    Gson gson = new Gson();
    String json = gson.toJson(output);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  public class Comment {
    // Private variables which hold comment data
    private String author;
    private String message;
    
    // This constructor has two parameters: author and message.
    public Comment(String a, String m) {
      author = a;
      message = m;
    }

    //Getter function to obtain author data
    public String getAuthor() {
        return author;
    }

    //Getter function to obtain message data
    public String getMessage() {
        return message;
    }

  }
}

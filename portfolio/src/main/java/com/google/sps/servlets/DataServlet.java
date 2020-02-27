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
import com.google.gson.Gson;
import java.io.IOException;
import java.util.*;
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
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //Using Gson to convert Comment object to json
    Gson gson = new Gson();
    String json = gson.toJson(comments);
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

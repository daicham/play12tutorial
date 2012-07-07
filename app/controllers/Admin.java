package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

@With(Secure.class)
public class Admin extends Controller {
  @Before
  static void setConnectedUser() {
    if (Security.isConnected()) {
      User user = User.find("byEmail", Security.connected()).first();
      renderArgs.put("user", user.fullname);
    }
  }

  public static void index() {
    String user = Security.connected();
    List<Post> posts = Post.find("author.email", user).fetch();
    render(posts);
  }

  public static void form() {
    render();
  }

  public static void save(String title, String content, String tags) {
    // Create Post
    User user = User.find("byEmail", Security.connected()).first();
    Post post = new Post(user, title, content);
    // Set tags list
    for (String tag : tags.split("\\s+")) {
      if (tag.trim().length() > 0) {
        post.tags.add(Tag.findOrCreateByName(tag));
      }
    }
    // Validate
    validation.valid(post);
    if (validation.hasErrors()) {
      render("@form", post);
    }
    // Save
    post.save();
    index();
  }
}

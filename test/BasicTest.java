import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

  @Before
  public void setup() {
    Fixtures.deleteDatabase();
  }

  @Test
  public void createandRetrieveUser() {
    // Create a new user and save it
    new User("bob@gmail.com", "secret", "Bob").save();

    // Retrieve the user with e-mail address bob@gmail.com
    User bob = User.find("byEmail", "bob@gmail.com").first();

    // Test
    assertNotNull(bob);
    assertEquals("Bob", bob.fullname);
  }

  @Test
  public void tryConnectAsUser() {
    // Create a new user and save it
    new User("bob@gmail.com", "secret", "Bob").save();

    // Test
    assertNotNull(User.connect("bob@gmail.com", "secret"));
    assertNull(User.connect("bob@gmail.com", "badpassword"));
    assertNull(User.connect("tom@gmail.com", "secret"));
  }

  @Test
  public void createPost() {
    // Create a new user and save it
    User bob = new User("bob@gmail.com", "secret", "Bob").save();

    // Create a new Post
    new Post(bob, "My first post", "Hello world").save();

    // Test that the post has bean created
    assertEquals(1, Post.count());

    // Retrieve all posts created by Bob
    List<Post> bobPosts = Post.find("byAuthor", bob).fetch();

    // Tests
    assertEquals(1, bobPosts.size());
    Post firstPost = bobPosts.get(0);
    assertNotNull(firstPost);
    assertEquals(bob, firstPost.author);
    assertEquals("My first post", firstPost.title);
    assertEquals("Hello world", firstPost.content);
    assertNotNull(firstPost.postedAt);
  }

  @Test
  public void postComments() {
    // Create a new user and save it
    User bob = new User("bob@gmail.com", "secret", "Bob").save();

    // Create a new Post
    Post bobPost = new Post(bob, "My first Post", "Hello world").save();

    // Post a first comment
    new Comment(bobPost, "Jeff", "Nice post").save();
    new Comment(bobPost, "Tom", "I knew that!").save();

    // Retrieve all comments
    List<Comment> bobPostComments = Comment.find("byPost", bobPost).fetch();

    // Tests
    assertEquals(2, bobPostComments.size());

    Comment firstComment = bobPostComments.get(0);
    assertNotNull(firstComment);
    assertEquals("Jeff", firstComment.author);
    assertEquals("Nice Post", firstComment.content);
    assertNotNull(firstComment.postedAt);

    Comment secondComment = bobPostComments.get(1);
    assertNotNull(secondComment);
    assertEquals("Jeff", secondComment.author);
    assertEquals("Nice Post", secondComment.content);
    assertNotNull(secondComment.postedAt);
  }

  @Test
  public void useTheCommentsRelation() {
    // Create a new user and save it
    User bob = new User("bob@gmail.com", "secret", "Bob").save();

    // Create a new Post
    Post bobPost = new Post(bob, "My first Post", "Hello world").save();

    // Post a first comment
    new Comment(bobPost, "Jeff", "Nice post").save();
  }
}

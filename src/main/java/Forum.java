import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Forum implements ForumCapable {

    private static final Logger LOGGER = Logger.getLogger( Forum.class.getName() );

    private MySQLHandler mySQLHandler;
    private User userLoggedIn = null;
    private Category currentCategory = null;

    public Forum(String dbUrl, String userDb, String passDb) throws SQLException {
        mySQLHandler = new MySQLHandler(dbUrl, userDb, passDb);
    }

    public boolean login(String username, String password)
    {
        boolean loginSuccesful = false;
        User user = mySQLHandler.getUserByUsername(username);

        if (user != null && user.isThisPasswordMine(password)) {
            LOGGER.log(Level.INFO,username + " logged in succesfully");
            loginSuccesful = true;
            userLoggedIn = user;
        } else {
            LOGGER.log(Level.WARNING,"invalid login ! ! !");
        }
        return loginSuccesful;
    }

    public void logout()
    {
        userLoggedIn = null;
    }

    public void exitCategory()
    {
        currentCategory = null;
    }

    public void register(String username, String password, String mail) {
        mySQLHandler.insertUser(username, password, mail);
    }

    public void insertCategory(String subject) {
        LOGGER.log(Level.INFO,"Insert subject for the new category = ");
        mySQLHandler.insertNewCategory(subject, userLoggedIn.getId());
    }

    public List<Category> getAllCategories() {
        return mySQLHandler.getAllCategories();
    }

    public boolean enterCategory(Integer categoryId) {

        boolean enterSuccesfully = false;
        Category category = mySQLHandler.getCategoryById(categoryId);

        if (category == null) {
            LOGGER.log(Level.SEVERE,"Invalid category ! ! !");
        }
        else
        {
            currentCategory = category;
            enterSuccesfully = true;
        }
        return enterSuccesfully;
    }

    public String whoIsLoggedIn()
    {
        return userLoggedIn != null ? userLoggedIn.getUsername() : "";
    }

    public String whichCategory()
    {
        return currentCategory != null ? currentCategory.getSubject() : "";
    }

    public void cleanupEntireDB()
    {
        mySQLHandler.cleanup();
    }
}

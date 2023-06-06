package Persistence;

import Model.User;
import Model.UserType;
import Persistence.Exception.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class UserDBRepo implements IUserRepo{

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public UserDBRepo(Properties properties) {
        logger.info("Initializing UserRepo with properties {}",properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public User findUserByUsername(String username) throws RepositoryException {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("select * from users where username=?")){
            ps.setString(1,username);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    logger.traceExit();
                    return parseUser(rs);
                }
            }
        }catch (SQLException ex){
            logger.trace(ex);
        }
        logger.traceExit();
        throw new RepositoryException("Nonexistent User");
    }

    private User parseUser(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        UserType userType =  UserType.valueOf(rs.getString("usertype"));
        User user = new User(username,password);
        user.setId(id);
        user.setUserType(userType);
        return user;
    }
}

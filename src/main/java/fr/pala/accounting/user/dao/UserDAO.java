package fr.pala.accounting.user.dao;

import fr.pala.accounting.exception.UserAlreadyExistsException;
import fr.pala.accounting.user.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserDAO {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public UserDAO(){
    }

    public UserModel addUser(UserModel user) throws UserAlreadyExistsException {

        if (getUserByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException(
                    "There is an user with that email address : " + user.getEmail());
        }

        return mongoTemplate.save(user);
    }

    public List<UserModel> getAllUsers() {
        return mongoTemplate.findAll(UserModel.class);
    }

    public UserModel getUserById(String user_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(user_id));
        return mongoTemplate.findOne(query, UserModel.class);
    }

    public UserModel getUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, UserModel.class);
    }

    public void updateUser(UserModel user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(user.getUser_id()));
        Update update = new Update();
        update.set("name", user.getName());
        update.set("email", user.getEmail());
        update.set("last_connection", user.getLast_connection());
        mongoTemplate.findAndModify(query, update, UserModel.class);
    }
}
package org.example;

import java.util.List;
import java.util.Map;

public interface UserManagerInterface {
    User loginUser(String fullName, String password);
    void listUsers();
    List<User> getUsers();

}

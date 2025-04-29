package org.example;

import java.util.List;

public interface UserManagerInterface {
    User loginUser(String fullName, String password);
    void listUsers();
    List<User> getUsers();

}

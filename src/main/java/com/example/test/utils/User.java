package com.example.test.utils;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
public  class User {
        private long id;
        private String username;
        private String password;
        private String email;
        private Role role;

        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", role='" + role  + '\'' +
                    ", email=" + email +
                    '}';
        }

    public static List<User> getSampleUsers() {
        return List.of(
                new User(1, "bakbak","admin","admin@email.com",Role.ADMIN),
                new User(2,"lilil","user", "user@email.com",Role.USER)
        );
    }


}
/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "home";
  }

  @GetMapping(
    path = "/home"
  )
  public String getUserForm(Map<String, Object> model){
    User user = new User();  
    model.put("user", user);
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial, username varchar(20), password varchar(20))");
      ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
      ArrayList<User> output = new ArrayList<User>();
      while (rs.next()) {
        User tuser = new User();
        String tname = rs.getString("username");
        String tpassword = rs.getString("password");
        int id = rs.getInt("id");
        tuser.setUsername(tname);
        tuser.setPassword(tpassword);
        tuser.setId(id);
        output.add(tuser);
      }
      
      model.put("records", output);
      return "redirect:/";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping(
    path = "/signup"
  )
  public String getSignUpForm(Map<String, Object> model){
    User user = new User();  // creates new rectangle object with empty fname and lname
    model.put("user", user);
    return "signup";
  }

  @PostMapping(
    path = "/signup",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleBrowserSignupSubmit(Map<String, Object> model, User user) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial, username varchar(20), password varchar(20))");
      ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
      while(rs.next()){
        String tname = rs.getString("username");
        if(user.getUsername().equals(tname)){
        System.out.println("they the same");
        //TODO: HOW TO MAKE THIS POP UP IN HTML? 
        return "signup";
        }
      }
      String sql = "INSERT INTO accounts (username,password) VALUES ('" + user.getUsername() + "','" + user.getPassword() + "')";
      stmt.executeUpdate(sql);
      return "mainpage";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping(
    path = "/login"
  )
  public String getLoginForm(Map<String, Object> model) {
    User user = new User();  // creates new rectangle object with empty fname and lname
    model.put("user", user);
    return "login";
  }

  @PostMapping(
    path = "/login",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  // stuff below this is temporary and wrong
  public String handleBrowserLoginSubmit(Map<String, Object> model, User user) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
      ArrayList<User> output = new ArrayList<User>();
      while (rs.next()) {
        User tuser = new User();
        String tname = rs.getString("username");
        String tpassword = rs.getString("password");
        int id = rs.getInt("id");
        if(user.getUsername().equals(tname)){
            if(user.getPassword().equals(tpassword)) {
            //maybe put a user.IsLoggedIn() as a boolean?
            //HOW TF WE GET THIS ON THE HTML?
            System.out.println("LOGGED IN AS " + user.getUsername());
            return "mainpage";
            }else{
                System.out.println("PASSWORD WRONG");
                return "login";
            }
        }
        System.out.println("username not exist :(");
      }
      return "login";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }
    

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }



}

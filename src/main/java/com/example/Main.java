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

import javax.swing.JOptionPane;

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
@SessionAttributes("loggeduser") //CREATING THE VARIABLE FOR THE SESSION
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;


   @ModelAttribute("loggeduser") //loggeduser IS THE User OBJECT THAT IS PER SESSION SO THIS CAN CARRY THE LGGED IN USERDATA MAYBE ????
   public User setUpUserForm() {
      return new User();
   }



  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }


  @RequestMapping("/")
  String index() {
    return "mainpage";
  }

  @GetMapping(
    path = "/mainpage"
  )
  public String getUserForm(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model){
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial, username varchar(20), password varchar(20), age integer, sex varchar(20), region varchar(20), bio varchar(150), pfp varchar(30), groups varchar(20))");
      ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
      ArrayList<User> output = new ArrayList<User>();
      System.out.println(loggeduser.getId());
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
    User user = new User();
    model.put("user", user);
    return "signup";
  }

  @PostMapping(
    path = "/signup",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )//ADD CONFIRM PASSWORD AND MAYBE EMAIL? SEQURITY QUESTIONS? ENCRYPT PASSWORD? REGION? SEX? AGE? ETC>...>>>
  public String handleBrowserSignupSubmit(Map<String, Object> model, User user) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial, username varchar(20), password varchar(20), age integer, sex varchar(20), region varchar(20), bio varchar(150), pfp varchar(30), groups varchar(20))");
      ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
      while(rs.next()){
        String tname = rs.getString("username");
        if(user.getUsername().equals(tname)){
        System.out.println("they the same");
        return "signuperror";
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
    path = "/signuperror"
  )
  public String getSignUpErrorForm(Map<String, Object> model){
    User user = new User();
    model.put("user", user);
    return "signuperror";
  }

  @PostMapping(
    path = "/signuperror",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleBrowserSignupErrorSubmit(Map<String, Object> model, User user) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial, username varchar(20), password varchar(20), age integer, sex varchar(20), region varchar(20), bio varchar(150), pfp varchar(30), groups varchar(20))");
      ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
      while(rs.next()){
        String tname = rs.getString("username");
        if(user.getUsername().equals(tname)){
        System.out.println("they the same");
        //TODO: HOW TO MAKE THIS POP UP IN HTML? 
        return "signuperror";
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
    User user = new User();
    model.put("user", user);
    return "login";
  }

   @PostMapping("/login")
   public String doLogin(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model, User user) { //THE (@ModelAttribute("loggeduser") ... PARAM CARRIES SESSION DATA INTO THE PAGE SO WE CAN FIND THE USER STUFF IT WORKS!!!
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
      ArrayList<User> output = new ArrayList<User>();
      while (rs.next()) {
        User tuser = new User();
        String tname = rs.getString("username");
        String tpassword = rs.getString("password");
        int id = rs.getInt("id");
        int age = rs.getInt("age");
        String sex = rs.getString("sex");
        String region = rs.getString("region");
        String bio = rs.getString("bio");
        String pfp = rs.getString("pfp");
        String groups = rs.getString("groups");
        if(user.getUsername().equals(tname)){
            if(user.getPassword().equals(tpassword)) {
            System.out.println("LOGGED IN AS1 " + user.getUsername());
            loggeduser.setId(id);
            loggeduser.setUsername(tname);
            loggeduser.setPassword(tpassword);
            loggeduser.setId(id);
            loggeduser.setAge(age);
            loggeduser.setSex(sex);
            loggeduser.setRegion(region);
            loggeduser.setBio(bio);
            loggeduser.setPfp(pfp);
            loggeduser.setGroups(groups);
            return "mainpage";
            }else{
                System.out.println("PASSWORD WRONG");
                return "loginerror";
            }
        }
        System.out.println("username not exist :(");
      }
      return "loginerror";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
}

  @GetMapping(
    path = "/logout"
  )
   public String doLogout(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model, User user) {
        loggeduser.setId(0);
        loggeduser.setUsername("");
        loggeduser.setPassword("");
        loggeduser.setAge(0);
        loggeduser.setSex("");
        loggeduser.setRegion("");
        loggeduser.setBio("");
        loggeduser.setPfp("");
        loggeduser.setGroups("");
        return "mainpage";
        }


  @GetMapping(
    path = "/loginerror"
  )
  public String getLoginFormError(Map<String, Object> model) {
    User user = new User();
    model.put("user", user);
    return "loginerror";
  }

  @PostMapping(
    path = "/loginerror",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleBrowserLoginErrorSubmit(Map<String, Object> model, User user) throws Exception {
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
                return "loginerror";
            }
        }
        System.out.println("username not exist :(");
      }
      return "loginerror";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

@GetMapping(
    path = "/accdb"
  )
  public String getAccountDatabase(Map<String, Object> model, User user) {
      try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
      ArrayList<User> output = new ArrayList<User>();
      while (rs.next()) {
        User tempuser = new User();
        int id = rs.getInt("id");
        String tname = rs.getString("username");
        String password = rs.getString("password");
        int age = rs.getInt("age");
        String sex = rs.getString("sex");
        String region = rs.getString("region");
        String bio = rs.getString("bio");
        String pfp = rs.getString("pfp");
        String groups = rs.getString("groups");
        tempuser.setUsername(tname);
        tempuser.setPassword(password);
        tempuser.setId(id);
        tempuser.setAge(age);
        tempuser.setSex(sex);
        tempuser.setRegion(region);
        tempuser.setBio(bio);
        tempuser.setPfp(pfp);
        tempuser.setGroups(groups);
        output.add(tempuser);
      }
      model.put("records", output);
      return "accdb";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

@GetMapping(
    path = "/logincheck"
  )
  public String getAccountDatabase(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model, User user) {
      try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ArrayList<User> output = new ArrayList<User>();
      output.add(loggeduser);
      model.put("records", output);
      return "logincheck";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

@GetMapping("/accdb/delaccdb")
  public String deleteAllRectangles(Map<String, Object> model){
   try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DROP TABLE accounts");
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial, username varchar(20), password varchar(20), age integer, sex varchar(20), region varchar(20), bio varchar(150), pfp varchar(30), groups varchar(20))");
    return "redirect:/accdb";
   
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

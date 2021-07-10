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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS grouptable (id serial, groupname varchar(20), membercount integer, game varchar(20), members varchar(300))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial, username varchar(20), password varchar(20), access varchar(20), age integer, gender varchar(20), region varchar(20), bio varchar(150), pfp varchar(30), groups varchar(20))");
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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial, username varchar(20), password varchar(20), access varchar(20), age integer, gender varchar(20), region varchar(20), bio varchar(150), pfp varchar(30), groups varchar(20))");
            ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
            while(rs.next()){
                String tname = rs.getString("username");
                if(user.getUsername().equals(tname)){
                    model.put("message", "Username is already taken. Try again.");
                    return "signup";
                }
            }
            String sql = "INSERT INTO accounts (username,password,access) VALUES ('" + user.getUsername() + "','" + user.getPassword() + "','user')";
            stmt.executeUpdate(sql);
            return "login";
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
                String gender = rs.getString("gender");
                String region = rs.getString("region");
                String bio = rs.getString("bio");
                String pfp = rs.getString("pfp");
                String groups = rs.getString("groups");
                String access = rs.getString("access");
                if(user.getUsername().equals(tname)){
                    if(user.getPassword().equals(tpassword)) {
                        System.out.println("LOGGED IN AS1 " + user.getUsername());
                        loggeduser.setId(id);
                        loggeduser.setUsername(tname);
                        loggeduser.setPassword(tpassword);
                        loggeduser.setId(id);
                        loggeduser.setAge(age);
                        loggeduser.setGender(gender);
                        loggeduser.setRegion(region);
                        loggeduser.setBio(bio);
                        loggeduser.setPfp(pfp);
                        loggeduser.setGroups(groups);
                        loggeduser.setAccess(access);
                        return "redirect:/profile";
                    }else{
                        model.put("message", "Username and password combination is incorrect. Try again.");
                        return "login";
                    }
                }
            }
            model.put("message", "Username and password combination is incorrect. Try again.");
            return "login";
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
        loggeduser.setGender("");
        loggeduser.setRegion("");
        loggeduser.setBio("");
        loggeduser.setPfp("");
        loggeduser.setGroups("");
        loggeduser.setAccess("");
        return "mainpage";
    }


    @GetMapping(
            path = "/groupdb"
    )
    public String getGroupDatabase(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS grouptable (id serial, groupname varchar(20), membercount integer, game varchar(20), members varchar(300))");
            ResultSet rs = stmt.executeQuery("SELECT * FROM grouptable");
            ArrayList<ObjGroup> output = new ArrayList<ObjGroup>();
            while (rs.next()) {
                ObjGroup tgroup = new ObjGroup();
                String gname = rs.getString("groupname");
                int mcount = rs.getInt("membercount");
                String game = rs.getString("game");
                String mems = rs.getString("members");
                int id = rs.getInt("id");
                tgroup.setGroupname(gname);
                tgroup.setMaxmembers(mcount);
                tgroup.setGame(game);
                tgroup.setGid(id);
                output.add(tgroup);
            }
            model.put("records", output);
            return "groupdb";
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }

    @GetMapping(
            path = "/group/create"
    )
    public String getGroupCreate(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model) {
        User user = new User();
        ObjGroup objgroup = new ObjGroup();
        model.put("objgroup", objgroup);
        model.put("user", user);
        return "creategroup";
    }
    @PostMapping(
            path = "/group/create",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public String handleGroupCreate(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model, ObjGroup objgroup) {
        if(loggeduser.getId() == 0){
            System.out.println("you must be logged in");
            return "mainpage";
        }else{
            try (Connection connection = dataSource.getConnection()) {
                Statement stmt = connection.createStatement();
                System.out.println("0");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS grouptable (id serial, groupname varchar(20), membercount integer, game varchar(20), members varchar(300))");
                String sql = "INSERT INTO grouptable (groupname,membercount,game,members) VALUES ('" + objgroup.getGroupname() + "','" + objgroup.getMaxmembers() + "','" + objgroup.getGame() + "','" + loggeduser.getId() + "')";
                stmt.executeUpdate(sql);
                return "mainpage";
            } catch (Exception e) {
                model.put("message", e.getMessage());
                return "error";
            }
        }
    }


    @GetMapping(
            path = "/accdb"
    )
    public String getAccountDatabase(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model, User user) {
        if(!(loggeduser.getAccess().equals("admin"))){
            model.put("message", "You need to be an admin to do that");
            model.put("records", loggeduser);
            return "profile";
        }else{
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
                    String gender = rs.getString("gender");
                    String region = rs.getString("region");
                    String bio = rs.getString("bio");
                    String pfp = rs.getString("pfp");
                    String groups = rs.getString("groups");
                    String access = rs.getString("access");
                    tempuser.setUsername(tname);
                    tempuser.setPassword(password);
                    tempuser.setId(id);
                    tempuser.setAge(age);
                    tempuser.setGender(gender);
                    tempuser.setRegion(region);
                    tempuser.setBio(bio);
                    tempuser.setPfp(pfp);
                    tempuser.setGroups(groups);
                    tempuser.setAccess(access);
                    output.add(tempuser);
                }
                model.put("records", output);
                return "accdb";
            } catch (Exception e) {
                model.put("message", e.getMessage());
                return "error";
            }
        }
    }


    @GetMapping(
            path = "/user/{pid}"
    )
    public String getAccountDatabase(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model, User user, @PathVariable String pid) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM accounts WHERE id="+pid);
            ArrayList<User> output = new ArrayList<User>();
            while (rs.next()) {
                User tempuser = new User();
                int id = rs.getInt("id");
                String tname = rs.getString("username");
                String password = rs.getString("password");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String region = rs.getString("region");
                String bio = rs.getString("bio");
                String pfp = rs.getString("pfp");
                String groups = rs.getString("groups");
                String access = rs.getString("access");
                tempuser.setUsername(tname);
                tempuser.setPassword(password);
                tempuser.setId(id);
                tempuser.setAge(age);
                tempuser.setGender(gender);
                tempuser.setRegion(region);
                tempuser.setBio(bio);
                tempuser.setPfp(pfp);
                tempuser.setGroups(groups);
                tempuser.setAccess(access);
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
            path = "/profile"
    )
    public String getLoginCheck(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model, User user) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ArrayList<User> output = new ArrayList<User>();
            if(loggeduser.getId() == 0){
                model.put("message", "You must be logged in");
            }else{
                ResultSet rs = stmt.executeQuery("SELECT * FROM accounts WHERE id="+loggeduser.getId());
                while(rs.next()){
                    String tname = rs.getString("username");
                    String tpassword = rs.getString("password");
                    int id = rs.getInt("id");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    String region = rs.getString("region");
                    String bio = rs.getString("bio");
                    String pfp = rs.getString("pfp");
                    String groups = rs.getString("groups");
                    String access = rs.getString("access");
                    loggeduser.setId(id);
                    loggeduser.setUsername(tname);
                    loggeduser.setPassword(tpassword);
                    loggeduser.setId(id);
                    loggeduser.setAge(age);
                    loggeduser.setGender(gender);
                    loggeduser.setRegion(region);
                    loggeduser.setBio(bio);
                    loggeduser.setPfp(pfp);
                    loggeduser.setGroups(groups);
                    loggeduser.setAccess(access);
                }
                output.add(loggeduser);
                model.put("records", output);
            }
            return "profile";
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }

    @GetMapping(
            path = "/profile/edit"
    )
    public String getProfileEdit(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model, User user) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ArrayList<User> output = new ArrayList<User>();
            if(loggeduser.getId() == 0){
                return "profile";
            }
            output.add(loggeduser);
            model.put("records", output);
            return "editprofile";
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }
    @PostMapping(
            path = "/profile/edit",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public String handleProfileEdit(@ModelAttribute("loggeduser") User loggeduser, Map<String, Object> model, User user) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ArrayList<User> output = new ArrayList<User>();
            if(loggeduser.getId() == 0){
                return "profile";
            }

            ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
            while(rs.next()){
                if(rs.getString("username").equals(user.getUsername())){
                    model.put("records", loggeduser);
                    model.put("message", "Username taken");
                    return "profile";
                }
            }
            if(!(user.getUsername().length() == 0)){
                stmt.executeUpdate("UPDATE accounts SET username='"+user.getUsername()+"' WHERE id="+loggeduser.getId());
            }
            if(!(user.getPassword().length() == 0)){
                stmt.executeUpdate("UPDATE accounts SET password='"+user.getPassword()+"' WHERE id="+loggeduser.getId());
            }
            if(user.getAge() != 0){
                stmt.executeUpdate("UPDATE accounts SET age='"+user.getAge()+"' WHERE id="+loggeduser.getId());
            }
            if(!(user.getGender().length() == 0)){
                stmt.executeUpdate("UPDATE accounts SET gender='"+user.getGender()+"' WHERE id="+loggeduser.getId());
            }
            if(!(user.getRegion().length() == 0)){
                stmt.executeUpdate("UPDATE accounts SET region='"+user.getRegion()+"' WHERE id="+loggeduser.getId());
            }
            if(!(user.getBio().length() == 0)){
                stmt.executeUpdate("UPDATE accounts SET bio='"+user.getBio()+"' WHERE id="+loggeduser.getId());
            }
            if(user.getAdminkey().equals("bobby276")){
                stmt.executeUpdate("UPDATE accounts SET access='admin' WHERE id="+loggeduser.getId());
            }
            output.add(loggeduser);
            model.put("records", output);
            return "redirect:/profile";
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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial, username varchar(20), password varchar(20), access varchar(20), age integer, gender varchar(20), region varchar(20), bio varchar(150), pfp varchar(30), groups varchar(20))");
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

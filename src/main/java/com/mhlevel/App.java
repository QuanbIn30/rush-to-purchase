package com.mhlevel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.mhlevel"})
@RestController
@MapperScan("com.mhlevel.dao")
public class App {

    @Autowired
    private UserDOMapper userDOMapper;

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/hello")
    public String hello(){
        UserDO userDO = userDOMapper.selectByPrimaryKey("1");
        if(userDO == null){
            return "user does not exists";
        }else{
            return userDO.getUsername();
        }
    }
}

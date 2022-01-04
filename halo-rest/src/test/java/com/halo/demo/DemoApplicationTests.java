package com.halo.demo;

import com.alibaba.fastjson.JSON;
import com.halo.api.PeopleServiceApi;
import com.halo.common.Result;
import com.halo.demo.thread.synchronizeds.Demo;
import com.halo.entity.People;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//@SpringBootTest
//@RunWith(SpringRunner.class)
class DemoApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PeopleServiceApi peopleServiceApi;

    @Test
    void contextLoads() throws SQLException {
        Connection connection = dataSource.getConnection();
        People people = new People();
        people.setId(3);
        Result<People> result = peopleServiceApi.queryPeople(people);
        System.out.println(JSON.toJSONString(result));
    }


    public static void main(String[] args) {
        Demo demo = new Demo();
        new Thread(()->{
            try {
                demo.addNumber();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            int number = demo.getNumber();
            System.out.println("已进入");
            System.out.println(number);
        }).start();
        System.out.println(11111111);
    }

}

package com.example.halo.mybatis;

import com.alibaba.fastjson.JSON;
import com.example.halo.demo.entity.People;
import com.example.halo.demo.mapper.PeopleMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/12 11:59
 */
@Deprecated
public class MybatisMain {

    public static void main(String[] args) {
        String resource = "mybatis/mybatis-config-for-test.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = null;
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            PeopleMapper roleMapper = sqlSession.getMapper(PeopleMapper.class);
            People people = new People();
            people.setId(1);
            people.setName("zhaoliu");
            People peoplePO = roleMapper.queryPeople(people);
            System.out.println(JSON.toJSONString(peoplePO));
            sqlSession.commit();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            sqlSession.rollback();
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
}

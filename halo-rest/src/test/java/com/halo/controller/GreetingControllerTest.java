package com.halo.controller;

import com.halo.common.Result;
import com.halo.enums.PriceTypeEnum;
import com.halo.exception.BizException;
import com.halo.req.ParseTheWeekReq;
import com.halo.vo.ClassVO;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GreetingControllerTest {
    /**
     * Method under test: {@link GreetingController#importClassIds(ClassVO)}
     */
    @Test
    public void testImportClassIds() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();

        ClassVO classVO = new ClassVO();
        classVO.setClassId(new ArrayList<>());
        Result<List<String>> actualImportClassIdsResult = greetingController.importClassIds(classVO);
        assertEquals(0, actualImportClassIdsResult.getCode());
        assertTrue(actualImportClassIdsResult.isRtnResult());
        assertNull(actualImportClassIdsResult.getVersion());
        assertEquals("查询无数据", actualImportClassIdsResult.getMsg());
        assertNull(actualImportClassIdsResult.getData());
        assertEquals(0, actualImportClassIdsResult.getCount());
    }

    /**
     * Method under test: {@link GreetingController#importClassIds(ClassVO)}
     */
    @Test
    public void testImportClassIds2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ClassVO classVO = mock(ClassVO.class);
        when(classVO.getClassId()).thenReturn(new ArrayList<>());
        doNothing().when(classVO).setClassId(Mockito.<List<String>>any());
        classVO.setClassId(new ArrayList<>());
        Result<List<String>> actualImportClassIdsResult = greetingController.importClassIds(classVO);
        assertEquals(0, actualImportClassIdsResult.getCode());
        assertTrue(actualImportClassIdsResult.isRtnResult());
        assertNull(actualImportClassIdsResult.getVersion());
        assertEquals("查询无数据", actualImportClassIdsResult.getMsg());
        assertNull(actualImportClassIdsResult.getData());
        assertEquals(0, actualImportClassIdsResult.getCount());
        verify(classVO).getClassId();
        verify(classVO).setClassId(Mockito.<List<String>>any());
    }

    /**
     * Method under test: {@link GreetingController#importClassIds(ClassVO)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testImportClassIds3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R011 Sandboxing policy violation.
        //   Diffblue Cover ran code in your project that tried
        //     to access 'sun.misc'.
        //   Diffblue Cover's default sandboxing policy disallows this in order to prevent
        //   your code from damaging your system environment.
        //   See https://diff.blue/R011 to resolve this issue.

        GreetingController greetingController = new GreetingController();

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("查询无数据");
        ClassVO classVO = mock(ClassVO.class);
        when(classVO.getClassId()).thenReturn(stringList);
        doNothing().when(classVO).setClassId(Mockito.<List<String>>any());
        classVO.setClassId(new ArrayList<>());
        greetingController.importClassIds(classVO);
    }

    /**
     * Method under test: {@link GreetingController#importClassIds(ClassVO)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testImportClassIds4() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R011 Sandboxing policy violation.
        //   Diffblue Cover ran code in your project that tried
        //     to access 'sun.misc'.
        //   Diffblue Cover's default sandboxing policy disallows this in order to prevent
        //   your code from damaging your system environment.
        //   See https://diff.blue/R011 to resolve this issue.

        GreetingController greetingController = new GreetingController();

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("42");
        stringList.add("查询无数据");
        ClassVO classVO = mock(ClassVO.class);
        when(classVO.getClassId()).thenReturn(stringList);
        doNothing().when(classVO).setClassId(Mockito.<List<String>>any());
        classVO.setClassId(new ArrayList<>());
        greetingController.importClassIds(classVO);
    }

    /**
     * Method under test: {@link GreetingController#importClassIds(String)}
     */
    @Test
    public void testImportClassIds5() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //   See https://diff.blue/R013 to resolve this issue.

        Result<String> actualImportClassIdsResult = (new GreetingController()).importClassIds("42");
        assertEquals(0, actualImportClassIdsResult.getCode());
        assertTrue(actualImportClassIdsResult.isRtnResult());
        assertNull(actualImportClassIdsResult.getVersion());
        assertEquals("42", actualImportClassIdsResult.getMsg());
        assertNull(actualImportClassIdsResult.getData());
        assertEquals(0, actualImportClassIdsResult.getCount());
    }

    /**
     * Method under test: {@link GreetingController#importClassIds(String)}
     */
    @Test
    public void testImportClassIds6() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //   See https://diff.blue/R013 to resolve this issue.

        Result<String> actualImportClassIdsResult = (new GreetingController()).importClassIds("");
        assertEquals(0, actualImportClassIdsResult.getCode());
        assertTrue(actualImportClassIdsResult.isRtnResult());
        assertNull(actualImportClassIdsResult.getVersion());
        assertEquals("", actualImportClassIdsResult.getMsg());
        assertNull(actualImportClassIdsResult.getData());
        assertEquals(0, actualImportClassIdsResult.getCount());
    }

    /**
     * Method under test: {@link GreetingController#importClassIds(String, Integer)}
     */
    @Test
    public void testImportClassIds7() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //   See https://diff.blue/R013 to resolve this issue.

        Result<String> actualImportClassIdsResult = (new GreetingController()).importClassIds("janedoe", 1);
        assertEquals(0, actualImportClassIdsResult.getCode());
        assertTrue(actualImportClassIdsResult.isRtnResult());
        assertNull(actualImportClassIdsResult.getVersion());
        assertEquals("username:janedoe -- age:1", actualImportClassIdsResult.getMsg());
        assertNull(actualImportClassIdsResult.getData());
        assertEquals(0, actualImportClassIdsResult.getCount());
    }

    /**
     * Method under test: {@link GreetingController#queryTest()}
     */
    @Test
    public void testQueryTest() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //   See https://diff.blue/R013 to resolve this issue.

        Result<Object> actualQueryTestResult = (new GreetingController()).queryTest();
        assertEquals(0, actualQueryTestResult.getCode());
        assertTrue(actualQueryTestResult.isRtnResult());
        assertNull(actualQueryTestResult.getVersion());
        assertEquals("SUCCESS", actualQueryTestResult.getMsg());
        assertNull(actualQueryTestResult.getData());
        assertEquals(0, actualQueryTestResult.getCount());
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(1);
        when(parseTheWeekReq.getArrangeType()).thenReturn(1);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        assertNull(greetingController.parseTheWeek(parseTheWeekReq));
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenThrow(new BizException("yyyy-MM-dd"));
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(1);
        when(parseTheWeekReq.getArrangeType()).thenReturn(1);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        greetingController.parseTheWeek(parseTheWeekReq);
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(3);
        when(parseTheWeekReq.getArrangeType()).thenReturn(1);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        assertNull(greetingController.parseTheWeek(parseTheWeekReq));
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testParseTheWeek4() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: 解锁规则错误,code=0
        //       at com.halo.enums.ArrangeRuleTypeEnum.valueOf(ArrangeRuleTypeEnum.java:43)
        //       at com.halo.controller.GreetingController.parseTheWeek(GreetingController.java:311)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(0);
        when(parseTheWeekReq.getArrangeType()).thenReturn(1);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        greetingController.parseTheWeek(parseTheWeekReq);
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek5() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(1);
        when(parseTheWeekReq.getArrangeType()).thenReturn(3);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        Result<List<Date>> actualParseTheWeekResult = greetingController.parseTheWeek(parseTheWeekReq);
        assertEquals(0, actualParseTheWeekResult.getCode());
        assertTrue(actualParseTheWeekResult.isRtnResult());
        assertNull(actualParseTheWeekResult.getVersion());
        assertEquals("操作成功", actualParseTheWeekResult.getMsg());
        assertEquals(1, actualParseTheWeekResult.getData().size());
        assertEquals(1, actualParseTheWeekResult.getCount());
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek6() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(1);
        when(parseTheWeekReq.getArrangeType()).thenReturn(0);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        Result<List<Date>> actualParseTheWeekResult = greetingController.parseTheWeek(parseTheWeekReq);
        assertEquals(0, actualParseTheWeekResult.getCode());
        assertTrue(actualParseTheWeekResult.isRtnResult());
        assertNull(actualParseTheWeekResult.getVersion());
        assertEquals("操作成功", actualParseTheWeekResult.getMsg());
        assertEquals(1, actualParseTheWeekResult.getData().size());
        assertEquals(1, actualParseTheWeekResult.getCount());
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testParseTheWeek7() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   cn.hutool.core.date.DateException: Parse [yyyy-MM-dd] with format [yyyy-MM-dd] error!
        //       at cn.hutool.core.date.DateTime.parse(DateTime.java:1096)
        //       at cn.hutool.core.date.DateTime.<init>(DateTime.java:281)
        //       at cn.hutool.core.date.DateUtil.parse(DateUtil.java:731)
        //       at com.halo.controller.GreetingController.parseTheWeek(GreetingController.java:303)
        //   java.text.ParseException: Unparseable date: "yyyy-MM-dd"
        //       at java.text.DateFormat.parse(DateFormat.java:366)
        //       at cn.hutool.core.date.DateTime.parse(DateTime.java:1088)
        //       at cn.hutool.core.date.DateTime.<init>(DateTime.java:281)
        //       at cn.hutool.core.date.DateUtil.parse(DateUtil.java:731)
        //       at com.halo.controller.GreetingController.parseTheWeek(GreetingController.java:303)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(1);
        when(parseTheWeekReq.getArrangeType()).thenReturn(1);
        when(parseTheWeekReq.getPayDate()).thenReturn("yyyy-MM-dd");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        greetingController.parseTheWeek(parseTheWeekReq);
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testParseTheWeek8() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: Date String must be not blank !
        //       at cn.hutool.core.lang.Assert.lambda$notBlank$5(Assert.java:340)
        //       at cn.hutool.core.lang.Assert.notBlank(Assert.java:319)
        //       at cn.hutool.core.lang.Assert.notBlank(Assert.java:340)
        //       at cn.hutool.core.date.DateTime.parse(DateTime.java:1086)
        //       at cn.hutool.core.date.DateTime.<init>(DateTime.java:281)
        //       at cn.hutool.core.date.DateUtil.parse(DateUtil.java:731)
        //       at com.halo.controller.GreetingController.parseTheWeek(GreetingController.java:303)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(1);
        when(parseTheWeekReq.getArrangeType()).thenReturn(1);
        when(parseTheWeekReq.getPayDate()).thenReturn("");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        greetingController.parseTheWeek(parseTheWeekReq);
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek9() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(3);
        when(parseTheWeekReq.getArrangeType()).thenReturn(3);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        Result<List<Date>> actualParseTheWeekResult = greetingController.parseTheWeek(parseTheWeekReq);
        assertEquals(0, actualParseTheWeekResult.getCode());
        assertTrue(actualParseTheWeekResult.isRtnResult());
        assertNull(actualParseTheWeekResult.getVersion());
        assertEquals("操作成功", actualParseTheWeekResult.getMsg());
        assertTrue(actualParseTheWeekResult.getData().isEmpty());
        assertEquals(1, actualParseTheWeekResult.getCount());
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek10() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(2);
        when(parseTheWeekReq.getArrangeType()).thenReturn(3);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        Result<List<Date>> actualParseTheWeekResult = greetingController.parseTheWeek(parseTheWeekReq);
        assertEquals(0, actualParseTheWeekResult.getCode());
        assertTrue(actualParseTheWeekResult.isRtnResult());
        assertNull(actualParseTheWeekResult.getVersion());
        assertEquals("操作成功", actualParseTheWeekResult.getMsg());
        assertEquals(1, actualParseTheWeekResult.getData().size());
        assertEquals(1, actualParseTheWeekResult.getCount());
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek11() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(3);
        when(parseTheWeekReq.getArrangeType()).thenReturn(3);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(integerList);
        Result<List<Date>> actualParseTheWeekResult = greetingController.parseTheWeek(parseTheWeekReq);
        assertEquals(0, actualParseTheWeekResult.getCode());
        assertTrue(actualParseTheWeekResult.isRtnResult());
        assertNull(actualParseTheWeekResult.getVersion());
        assertEquals("操作成功", actualParseTheWeekResult.getMsg());
        assertEquals(1, actualParseTheWeekResult.getData().size());
        assertEquals(1, actualParseTheWeekResult.getCount());
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek12() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(stringList);
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(2);
        when(parseTheWeekReq.getArrangeType()).thenReturn(3);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        Result<List<Date>> actualParseTheWeekResult = greetingController.parseTheWeek(parseTheWeekReq);
        assertEquals(0, actualParseTheWeekResult.getCode());
        assertTrue(actualParseTheWeekResult.isRtnResult());
        assertNull(actualParseTheWeekResult.getVersion());
        assertEquals("操作成功", actualParseTheWeekResult.getMsg());
        assertEquals(1, actualParseTheWeekResult.getData().size());
        assertEquals(1, actualParseTheWeekResult.getCount());
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek13() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("yyyy-MM-dd");
        stringList.add("foo");
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(stringList);
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(2);
        when(parseTheWeekReq.getArrangeType()).thenReturn(3);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(new ArrayList<>());
        Result<List<Date>> actualParseTheWeekResult = greetingController.parseTheWeek(parseTheWeekReq);
        assertEquals(0, actualParseTheWeekResult.getCode());
        assertTrue(actualParseTheWeekResult.isRtnResult());
        assertNull(actualParseTheWeekResult.getVersion());
        assertEquals("操作成功", actualParseTheWeekResult.getMsg());
        assertEquals(1, actualParseTheWeekResult.getData().size());
        assertEquals(1, actualParseTheWeekResult.getCount());
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testParseTheWeek14() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //       at java.util.ArrayList.rangeCheck(ArrayList.java:659)
        //       at java.util.ArrayList.get(ArrayList.java:435)
        //       at com.halo.controller.GreetingController.unlockForDay1(GreetingController.java:389)
        //       at com.halo.controller.GreetingController.parseTheWeek(GreetingController.java:323)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        integerList.add(1);
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(new ArrayList<>());
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(1);
        when(parseTheWeekReq.getArrangeType()).thenReturn(3);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(integerList);
        greetingController.parseTheWeek(parseTheWeekReq);
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    public void testParseTheWeek15() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        integerList.add(1);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("42");
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(stringList);
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(1);
        when(parseTheWeekReq.getArrangeType()).thenReturn(3);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(integerList);
        Result<List<Date>> actualParseTheWeekResult = greetingController.parseTheWeek(parseTheWeekReq);
        assertEquals(0, actualParseTheWeekResult.getCode());
        assertTrue(actualParseTheWeekResult.isRtnResult());
        assertNull(actualParseTheWeekResult.getVersion());
        assertEquals("操作成功", actualParseTheWeekResult.getMsg());
        assertEquals(2, actualParseTheWeekResult.getData().size());
        assertEquals(2, actualParseTheWeekResult.getCount());
        verify(parseTheWeekReq).getArrangeRuleType();
        verify(parseTheWeekReq).getArrangeType();
        verify(parseTheWeekReq).getPayDate();
        verify(parseTheWeekReq).getArrangeRuleContent();
        verify(parseTheWeekReq).getCurriculumNos();
    }

    /**
     * Method under test: {@link GreetingController#parseTheWeek(ParseTheWeekReq)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testParseTheWeek16() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.halo.req.ParseTheWeekReq$MockitoMock$3YAd7vV0["mockitoInterceptor"]->org.mockito.internal.creation.bytebuddy.MockMethodInterceptor["serializationSupport"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NumberFormatException: For input string: "yyyy-MM-dd"
        //       at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
        //       at java.lang.Integer.parseInt(Integer.java:580)
        //       at java.lang.Integer.parseInt(Integer.java:615)
        //       at com.halo.controller.GreetingController.unlockForDay1(GreetingController.java:389)
        //       at com.halo.controller.GreetingController.parseTheWeek(GreetingController.java:323)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController greetingController = new GreetingController();

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        integerList.add(1);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("yyyy-MM-dd");
        stringList.add("42");
        ParseTheWeekReq parseTheWeekReq = mock(ParseTheWeekReq.class);
        when(parseTheWeekReq.getArrangeRuleContent()).thenReturn(stringList);
        when(parseTheWeekReq.getArrangeRuleType()).thenReturn(1);
        when(parseTheWeekReq.getArrangeType()).thenReturn(3);
        when(parseTheWeekReq.getPayDate()).thenReturn("2020-03-01");
        when(parseTheWeekReq.getCurriculumNos()).thenReturn(integerList);
        greetingController.parseTheWeek(parseTheWeekReq);
    }

    /**
     * Method under test: {@link GreetingController#calculationCucPrice(PriceTypeEnum, double, Integer)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testCalculationCucPrice() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   com.diffblue.fuzztest.shared.proxy.BeanInstantiationException: Could not instantiate bean: greetingController defined in null
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'greetingController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.security.AccessController.doPrivileged(Native Method)
        //       at java.util.Optional.map(Optional.java:215)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.ArrayIndexOutOfBoundsException: 1
        //       at com.halo.controller.GreetingController.calculationCucPrice(GreetingController.java:551)
        //   See https://diff.blue/R013 to resolve this issue.

        (new GreetingController()).calculationCucPrice(PriceTypeEnum.COURSE, 10.0d, 1);
    }

    /**
     * Method under test: {@link GreetingController#calculationCucPrice(PriceTypeEnum, double, Integer)}
     */
    @Test
    public void testCalculationCucPrice2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   com.diffblue.fuzztest.shared.proxy.BeanInstantiationException: Could not instantiate bean: greetingController defined in null
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'greetingController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.security.AccessController.doPrivileged(Native Method)
        //       at java.util.Optional.map(Optional.java:215)
        //   See https://diff.blue/R026 to resolve this issue.

        assertTrue((new GreetingController()).calculationCucPrice(PriceTypeEnum.CURRICULUM, 10.0d, 1).isEmpty());
    }

    /**
     * Method under test: {@link GreetingController#calculationCucPrice(PriceTypeEnum, double, Integer)}
     */
    @Test
    public void testCalculationCucPrice3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   com.diffblue.fuzztest.shared.proxy.BeanInstantiationException: Could not instantiate bean: greetingController defined in null
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'greetingController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.security.AccessController.doPrivileged(Native Method)
        //       at java.util.Optional.map(Optional.java:215)
        //   See https://diff.blue/R026 to resolve this issue.

        assertTrue((new GreetingController()).calculationCucPrice(PriceTypeEnum.CLASS_TIME, 10.0d, 1).isEmpty());
    }

    /**
     * Method under test: {@link GreetingController#calculationCucPrice(PriceTypeEnum, double, Integer)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testCalculationCucPrice4() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   com.diffblue.fuzztest.shared.proxy.BeanInstantiationException: Could not instantiate bean: greetingController defined in null
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'greetingController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.security.AccessController.doPrivileged(Native Method)
        //       at java.util.Optional.map(Optional.java:215)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: Illegal initial capacity: -1249367607
        //       at java.util.HashMap.<init>(HashMap.java:450)
        //       at java.util.HashMap.<init>(HashMap.java:469)
        //       at com.halo.controller.GreetingController.calculationCucPrice(GreetingController.java:537)
        //   See https://diff.blue/R013 to resolve this issue.

        (new GreetingController()).calculationCucPrice(PriceTypeEnum.COURSE, 10.0d, -1249367607);
    }

    /**
     * Method under test: {@link GreetingController#splitDateRange(String, String)}
     */
    @Test
    public void testSplitDateRange() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   com.diffblue.fuzztest.shared.proxy.BeanInstantiationException: Could not instantiate bean: greetingController defined in null
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'greetingController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.security.AccessController.doPrivileged(Native Method)
        //       at java.util.Optional.map(Optional.java:215)
        //   See https://diff.blue/R026 to resolve this issue.

        assertEquals(1, GreetingController.splitDateRange("2020-03-01", "2020-03-01").size());
    }

    /**
     * Method under test: {@link GreetingController#splitDateRange(String, String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testSplitDateRange2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   com.diffblue.fuzztest.shared.proxy.BeanInstantiationException: Could not instantiate bean: greetingController defined in null
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'greetingController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.security.AccessController.doPrivileged(Native Method)
        //       at java.util.Optional.map(Optional.java:215)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.time.format.DateTimeParseException: Text '2020/03/01' could not be parsed at index 4
        //       at java.time.format.DateTimeFormatter.parseResolved0(DateTimeFormatter.java:1949)
        //       at java.time.format.DateTimeFormatter.parse(DateTimeFormatter.java:1851)
        //       at java.time.LocalDate.parse(LocalDate.java:400)
        //       at com.halo.controller.GreetingController.splitDateRange(GreetingController.java:572)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController.splitDateRange("2020/03/01", "2020-03-01");
    }

    /**
     * Method under test: {@link GreetingController#splitDateRange(String, String)}
     */
    @Test
    @Ignore("TODO: Complete this test")
    public void testSplitDateRange3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   com.diffblue.fuzztest.shared.proxy.BeanInstantiationException: Could not instantiate bean: greetingController defined in null
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'greetingController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'redisTemplate': Invocation of init method failed; nested exception is java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.util.Optional.map(Optional.java:215)
        //   java.lang.IllegalStateException: RedisConnectionFactory is required
        //       at java.security.AccessController.doPrivileged(Native Method)
        //       at java.util.Optional.map(Optional.java:215)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.time.format.DateTimeParseException: Text '2020/03/01' could not be parsed at index 4
        //       at java.time.format.DateTimeFormatter.parseResolved0(DateTimeFormatter.java:1949)
        //       at java.time.format.DateTimeFormatter.parse(DateTimeFormatter.java:1851)
        //       at java.time.LocalDate.parse(LocalDate.java:400)
        //       at com.halo.controller.GreetingController.splitDateRange(GreetingController.java:573)
        //   See https://diff.blue/R013 to resolve this issue.

        GreetingController.splitDateRange("2020-03-01", "2020/03/01");
    }
}


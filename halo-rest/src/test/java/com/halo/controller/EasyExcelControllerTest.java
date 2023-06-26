package com.halo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

@ContextConfiguration(classes = {EasyExcelController.class})
@ExtendWith(SpringExtension.class)
class EasyExcelControllerTest {
    @Autowired
    private EasyExcelController easyExcelController;

    /**
     * Method under test: {@link EasyExcelController#downloadProductIdTemplateExcel(HttpServletResponse)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testDownloadProductIdTemplateExcel() throws Exception {
        // TODO: Complete this test.
        //   Reason: R020 Temporary files were created but not deleted.
        //   The method under test created the following temporary files without deleting
        //   them:
        //     /var/folders/7l/yzyh4s290m705lrgkzd4phl80000gn/T/f8c9b911-947d-4051-b3d1-407a94012f21
        //     /var/folders/7l/yzyh4s290m705lrgkzd4phl80000gn/T/f8c9b911-947d-4051-b3d1-407a94012f21/excache
        //     /var/folders/7l/yzyh4s290m705lrgkzd4phl80000gn/T/f8c9b911-947d-4051-b3d1-407a94012f21/poifiles
        //   Please ensure that temporary files are deleted in the method under test.
        //   See https://diff.blue/R020

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/file/downloadProductIdTemplateExcel");
        MockMvcBuilders.standaloneSetup(easyExcelController).build().perform(requestBuilder);
    }

    /**
     * Method under test: {@link EasyExcelController#downloadTemplateExcel(HttpServletResponse)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testDownloadTemplateExcel() throws Exception {
        // TODO: Complete this test.
        //   Reason: R020 Temporary files were created but not deleted.
        //   The method under test created the following temporary files without deleting
        //   them:
        //     /var/folders/7l/yzyh4s290m705lrgkzd4phl80000gn/T/f8c9b911-947d-4051-b3d1-407a94012f21/poifiles
        //   Please ensure that temporary files are deleted in the method under test.
        //   See https://diff.blue/R020

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/file/downloadTemplateExcel");
        MockMvcBuilders.standaloneSetup(easyExcelController).build().perform(requestBuilder);
    }

    /**
     * Method under test: {@link EasyExcelController#exportProductId(HttpServletResponse)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testExportProductId() throws Exception {
        // TODO: Complete this test.
        //   Reason: R020 Temporary files were created but not deleted.
        //   The method under test created the following temporary files without deleting
        //   them:
        //     /var/folders/7l/yzyh4s290m705lrgkzd4phl80000gn/T/f8c9b911-947d-4051-b3d1-407a94012f21/poifiles
        //   Please ensure that temporary files are deleted in the method under test.
        //   See https://diff.blue/R020

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/file/exportProductId");
        MockMvcBuilders.standaloneSetup(easyExcelController).build().perform(requestBuilder);
    }

    /**
     * Method under test: {@link EasyExcelController#importProductId(MultipartFile)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testImportProductId() throws Exception {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class java.io.ByteArrayInputStream and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: org.springframework.mock.web.MockMultipartFile["inputStream"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/file/importProductId")
                .contentType(MediaType.APPLICATION_JSON);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("Name",
                new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(mockMultipartFile));
        MockMvcBuilders.standaloneSetup(easyExcelController).build().perform(requestBuilder);
    }

    /**
     * Method under test: {@link EasyExcelController#importProductIdV2(MultipartFile)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testImportProductIdV2() throws Exception {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class java.io.ByteArrayInputStream and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: org.springframework.mock.web.MockMultipartFile["inputStream"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1306)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:733)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:774)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4624)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3869)
        //   See https://diff.blue/R013 to resolve this issue.

        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/file/importProductIdV2")
                .contentType(MediaType.APPLICATION_JSON);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("Name",
                new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")));

        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(mockMultipartFile));
        MockMvcBuilders.standaloneSetup(easyExcelController).build().perform(requestBuilder);
    }
}


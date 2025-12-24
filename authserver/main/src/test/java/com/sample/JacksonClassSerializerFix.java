package com.sample;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JacksonClassSerializerFix {


    public static void main(String[] args) throws Exception {

        // 待序列化的 Map 实例
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "测试");
        dataMap.put("userType", "com.example.User"); // 补充完整值


        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // 关键点：序列化器泛型声明为 Class<?>，兼容所有 Class 对象
        module.addSerializer(Map.class, new JsonSerializer<Map>() {
            @Override
            public void serialize(Map clazz, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeStringField("@class", clazz.getClass().getName());
                gen.writeEndObject();
            }
        });

        objectMapper.registerModule(module);

        String result = objectMapper.writeValueAsString(dataMap);

        System.out.println(result);
        // 输出：{"@class":"java.lang.Class","name":"java.lang.Class"}
    }
}
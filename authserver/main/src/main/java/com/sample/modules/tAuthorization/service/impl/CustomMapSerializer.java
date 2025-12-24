package com.sample.modules.tAuthorization.service.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomMapSerializer extends StdSerializer<Map<?, ?>> {


    protected CustomMapSerializer(Class<Map<?, ?>> t) {
        super(t);
    }

    @Override
    public void serialize(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject(); // 开始写对象字段
        gen.writeStringField("@class", value.getClass().getName()); // 添加类信息字段
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            gen.writeObjectField(entry.getKey().toString(), entry.getValue()); // 写入键值对字段
        }
        gen.writeEndObject(); // 结束对象字段
    }
}

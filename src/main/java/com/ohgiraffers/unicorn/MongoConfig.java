package com.ohgiraffers.unicorn;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.ohgiraffers.unicorn.space.entity.BrandSpace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "brand_space";
    }

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                Arrays.asList(new LocationReadingConverter(), new LocationWritingConverter()));
    }

    @ReadingConverter
    public static class LocationReadingConverter implements Converter<DBObject, BrandSpace.Item.Transform.Location> {
        @Override
        public BrandSpace.Item.Transform.Location convert(DBObject source) {
            BrandSpace.Item.Transform.Location location = new BrandSpace.Item.Transform.Location();
            location.setX(Double.parseDouble(source.get("x").toString()));
            location.setY(Double.parseDouble(source.get("y").toString()));
            location.setZ(Double.parseDouble(source.get("z").toString()));
            return location;
        }
    }

    @WritingConverter
    public static class LocationWritingConverter implements Converter<BrandSpace.Item.Transform.Location, DBObject> {
        @Override
        public DBObject convert(BrandSpace.Item.Transform.Location source) {
            DBObject dbObject = new BasicDBObject();
            dbObject.put("x", source.getX());
            dbObject.put("y", source.getY());
            dbObject.put("z", source.getZ());
            return dbObject;
        }
    }
}

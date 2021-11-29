package ru.javawebinar.topjava.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MealListSerializer extends StdSerializer<List<Meal>> {

    public MealListSerializer() {
        this(null);
    }

    public MealListSerializer(Class<List<Meal>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Meal> mealList,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<MealMock> ids = new ArrayList<>();
        for (Meal meal : mealList) {
            ids.add(new MealMock(meal));
        }
        generator.writeObject(ids);
// Не знаю какой вариант лучше, а может оказаться и так что и то и то плохо,
//
//        generator.writeStartArray("meals");
//        for (Meal meal : mealList) {
//            generator.writeStartObject();
//            generator.writeObjectField("id", meal.getId());
//            generator.writeObjectField("dateTime", meal.getDateTime());
//            generator.writeStringField("description", meal.getDescription());
//            generator.writeNumberField("calories", meal.getCalories());
//            generator.writeEndObject();
//        }
//        generator.writeEndArray();
    }

    private class MealMock extends AbstractBaseEntity {
        private final LocalDateTime dateTime;

        private final String description;

        private final int calories;

        public MealMock(Meal meal) {
            this.id = meal.getId();
            this.description = meal.getDescription();
            this.dateTime = meal.getDateTime();
            this.calories = meal.getCalories();
        }
    }
}

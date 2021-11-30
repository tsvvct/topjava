package ru.javawebinar.topjava.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.javawebinar.topjava.model.Meal;

import java.io.IOException;
import java.util.List;

public class MealListDeserializer extends StdDeserializer<List<Meal>> {

    public MealListDeserializer() {
        this(null);
    }

    public MealListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Meal> deserialize(
            JsonParser jsonparser,
            DeserializationContext context)
            throws IOException, JsonProcessingException {

        List<Meal> mealList = jsonparser.readValueAs(new TypeReference<List<Meal>>() {
        });
        return mealList;
    }

}

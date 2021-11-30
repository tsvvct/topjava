package ru.javawebinar.topjava.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.javawebinar.topjava.model.Meal;

import java.io.IOException;
import java.time.LocalDateTime;
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

        List<FakeMeal> fakeMealList = mealList.stream()
                .map(FakeMeal::new)
                .toList();

        generator.writeObject(fakeMealList);
    }

    private class FakeMeal {
        private final int id;
        private final LocalDateTime dateTime;
        private final String description;
        private final int calories;

        public FakeMeal(Meal meal) {
            this.id = meal.getId();
            this.description = meal.getDescription();
            this.dateTime = meal.getDateTime();
            this.calories = meal.getCalories();
        }
    }
}

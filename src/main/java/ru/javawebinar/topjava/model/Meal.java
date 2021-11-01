package ru.javawebinar.topjava.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static javax.persistence.ConstraintMode.CONSTRAINT;

@Entity
@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal meal WHERE meal.id=:id AND meal.user.id=:userid"),
        @NamedQuery(name = Meal.BY_ID, query = "SELECT meal FROM Meal meal WHERE meal.user.id=:userid AND meal.id=:id"),
        @NamedQuery(name = Meal.ALL_SORTED, query = "SELECT meal FROM Meal meal WHERE meal.user.id=:userid" +
                " ORDER BY meal.dateTime DESC"),
        @NamedQuery(name = Meal.FILTERED_SORTED, query = "SELECT meal FROM Meal meal " +
                "WHERE meal.user.id=:userid AND meal.dateTime>=:startDateTime AND meal.dateTime<:endDateTime " +
                "ORDER BY meal.dateTime DESC"),
})
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date_time"},
        name = "meals_unique_user_datetime_idx"))
public class Meal extends AbstractBaseEntity {

    public static final String DELETE = "Meal.delete";
    public static final String BY_ID = "Meal.getById";
    public static final String ALL_SORTED = "Meal.getAllSorted";
    public static final String FILTERED_SORTED = "Meal.getFilteredSorted";

    @Column(name = "date_time", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;


    @Column(name = "calories", nullable = false)
    @Range(min = 10, max = 10000)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(CONSTRAINT))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}

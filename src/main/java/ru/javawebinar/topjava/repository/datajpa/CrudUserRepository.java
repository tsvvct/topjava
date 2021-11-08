package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
//    @Query(name = User.DELETE)
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.meals WHERE u.email=:email")
    User getByEmailFetchMeal(@Param("email") String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.meals WHERE u.id=:id")
    User getByIdFetchMeal(@Param("id") int id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.meals")
    List<User> getAllFetchMeal(Sort sort);

    User getByEmail(String email);
}

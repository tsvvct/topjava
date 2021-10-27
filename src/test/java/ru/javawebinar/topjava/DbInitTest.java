package ru.javawebinar.topjava;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration("classpath:spring/spring-db.xml")
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/initDB.sql", "classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class DbInitTest {

    @Test
    public void initDbTest() {
        Assert.assertTrue(true);
    }
}

package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private static final User bob = new User(1001,"bob","$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "[Authority{name=ROLE_USER}]");
    private static final User user = new User(1002,"user", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy","[Authority{name=ROLE_USER}]");

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    @Test
    public void find_id_by_username_works_correctly() {
        int test = sut.findIdByUsername("bob");
        Assert.assertEquals(bob.getId(), test);

        int test1 = sut.findIdByUsername("user");
        Assert.assertEquals(user.getId(), test1);
    }

    @Test
    public void find_all_works_correctly() {
        List<User> test = sut.findAll();
        Assert.assertEquals(2, test.size());
        assertUsersMatch(bob, test.get(0));
        assertUsersMatch(user, test.get(1));
    }

    @Test
    public void find_by_username_works_correctly() {
        User test = sut.findByUsername("bob");
        assertUsersMatch(bob,test);

        User test1 = sut.findByUsername("user");
        assertUsersMatch(user, test1);
    }

    @Test
    public void find_by_user_id_works_correctly() {
        User test = sut.findByUserId(1001);
        assertUsersMatch(bob, test);

        User test1 = sut.findByUserId(1002);
        assertUsersMatch(user, test1);
    }

    private void assertUsersMatch(User expected, User actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
    }
}

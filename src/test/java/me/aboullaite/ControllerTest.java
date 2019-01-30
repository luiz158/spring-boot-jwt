package me.aboullaite;

import me.aboullaite.model.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

public class ControllerTest extends AbstractControllerTest {

    @Test
    public void createUser() throws Exception {
        String uri = "/user/register";
        User user = new User();
        user.setEmail("hgg@gmail.com");
        user.setPassword("Ginger123!");

        String inputJson = mapToJson(user);
        this.setUp();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }
}

package org.springframework.cloud.config.server.data;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.test.context.junit4.SpringRunner;

class AbstractDataConfigServerTest {
    @LocalServerPort
    private int port;

    @Test
    public void whenReadConfigurationThenIsOK() {
        Environment environment = new TestRestTemplate().getForObject("http://localhost:"
                + port + "/my-app/default", Environment.class);
        Assert.assertEquals("my-app-default", environment.getPropertySources().get(0).getName());
        Assert.assertEquals(1, environment.getPropertySources().size());
        Assert.assertEquals(true, environment.getPropertySources().get(0).getSource().containsKey("key"));
        Assert.assertEquals("value", environment.getPropertySources().get(0).getSource().get("key"));
    }
}

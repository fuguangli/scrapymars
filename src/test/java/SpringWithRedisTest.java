import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * name fuguangli
 * date 2019/12/30
 * contact businessfgl@163.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:app-context-with-redis.xml",
        "classpath:app-redis.xml",
        "classpath:app-props.xml"
})
public class SpringWithRedisTest {
    @Test
    public void test() throws InterruptedException {
        System.out.println("bean init");
//        Thread.sleep(Long.MAX_VALUE);
    }
}

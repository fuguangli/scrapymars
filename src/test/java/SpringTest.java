import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * name fuguangli
 * date 2019/12/27
 * contact businessfgl@163.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:app-context.xml",
})
public class SpringTest {

    @Test
    public void test() throws InterruptedException {
        System.out.println("bean init");
//        Thread.sleep(Long.MAX_VALUE);
    }
}

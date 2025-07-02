package rag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * TDDO
 *
 * @author Xu JianXing
 * @since 0.1
 */
@SpringBootTest(classes = Main.class)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application.yml")
public class AzureOpenAIEmbeddingTest {
    @Autowired
    private EmbeddingModel embeddingModel;

    @Test
    public void test() {
      float[] results=  embeddingModel.embed("Hello, sample");
      System.out.println(Arrays.toString(results));
    }
}

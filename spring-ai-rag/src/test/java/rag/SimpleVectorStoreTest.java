package rag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TDDO
 *
 * @author Xu JianXing
 * @since 0.1
 */
@SpringBootTest(classes = Main.class)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application.yml")
public class SimpleVectorStoreTest {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Test
    public void test() {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        List<Document> documents =
                List.of(new Document("I like Spring Boot"),
                        new Document("I like Apple"),
                        new Document("I love Java programming language"));
        vectorStore.add(documents);
        // Retrieve embeddings
        SearchRequest query = new SearchRequest.Builder()
                .query("Spring Boot").topK(2)
                .build();
        List<Document> similarDocuments = vectorStore.similaritySearch(query);
        String relevantData = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
        System.out.println(relevantData);
    }
}

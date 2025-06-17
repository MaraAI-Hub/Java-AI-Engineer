package ai.mara.studio;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;

public class ChatClientBuilder3 {
    private static final String DEEP_SEEK_BASE_URL = "https://api.deepseek.com";
    private static final String DEEP_SEEK_MODEL = "deepseek-chat";
    private static final String DEEP_SEEK_API_KEY = System.getenv("DEEP_SEEK_API_KEY");

    public static void main(String[] args) {
        DeepSeekApi deepSeekApi = DeepSeekApi.builder()
                .baseUrl(DEEP_SEEK_BASE_URL)
                .apiKey(DEEP_SEEK_API_KEY)
                .build();
        ChatModel deepSeekChatModel = DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .defaultOptions(DeepSeekChatOptions.builder()
                        .model(DEEP_SEEK_MODEL)
                        .build())
                .build();


        ChatClient chatClient = ChatClient
                .builder(deepSeekChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();


        System.out.println(chatClient.prompt()
                .system("You are a helpful assistant")
                .user("Hi")
                .call()
                .content());
    }
}
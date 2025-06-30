package chatmodel;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;

public class ChatClientBuilder {
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
//        ChatClient.Builder builder = ChatClient.builder(deepSeekChatModel)
//                .defaultSystem("You are a helpful assistant");
//
//        ChatClient chatClient = builder.build();
//
//        chatClient.prompt().system("You are a helpful assistant")
//                        .user("Hi");
//        System.out.println(chatClient.prompt("Hi").call().content());

        ChatClient.Builder builder = ChatClient.builder(deepSeekChatModel);
        ChatClient chatClient = builder.build();
        System.out.println(chatClient.prompt()
                .system("You are a helpful assistant")
                .user("Hi")
                .call()
                .content());
    }
}
package ai.mara.studio;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;

public class ChatClientBuilder {
    private static final String DEEP_SEEK_BASE_URL = "https://api.deepseek.com";
    private static final String DEFAULT_DEEP_SEEK_MODEL = "deepseek-chat";
    private static final String DEFAULT_DEEP_SEEK_API_KEY = "sk-4ea07705f6a64ad4a87e73b318c16fec";

    public static void main(String[] args) {
        DeepSeekApi deepSeekApi = DeepSeekApi.builder()
                .baseUrl(DEEP_SEEK_BASE_URL)
                .apiKey(DEFAULT_DEEP_SEEK_API_KEY)
                .build();

        ChatModel deepSeekChatModel = DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .defaultOptions(DeepSeekChatOptions.builder()
                        .model(DEFAULT_DEEP_SEEK_MODEL)
                        .build())
                .build();
        ChatClient.Builder builder = ChatClient.builder(deepSeekChatModel)
                .defaultSystem("你是一个翻译专家，将中文翻译成英文");

        ChatClient chatClient = builder.build();
     System.out.println(chatClient.prompt("你好").call().chatClientResponse().toString());
    }
}
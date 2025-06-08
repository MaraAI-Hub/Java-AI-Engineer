package ai.mara.studio;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;


public class DeepSeekModelNative {
    private static final String DEEP_SEEK_BASE_URL = "https://api.deepseek.com";
    private static final String DEFAULT_DEEP_SEEK_MODEL = "deepseek-chat";
    private static final String DEFAULT_DEEP_SEEK_API_KEY = "sk-4ea07705f6a64ad4a87e73b318c16fec";

    public static void main(String[] args) {
        DeepSeekApi deepSeekApi = DeepSeekApi.builder().baseUrl(DEEP_SEEK_BASE_URL).apiKey(DEFAULT_DEEP_SEEK_API_KEY).build();
        DeepSeekChatModel deepSeekChatModel = DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .defaultOptions(DeepSeekChatOptions.builder()
                        .model(DEFAULT_DEEP_SEEK_MODEL).build())
                .build();
        ChatClient chatClient = ChatClient.builder(deepSeekChatModel)
                .build();
        String userQuestion = "利雅得的天气怎么样";
        System.out.println("> USER: " + userQuestion);
        System.out.println("> ASSISTANT: " + chatClient.prompt(userQuestion).call().content());
    }
}
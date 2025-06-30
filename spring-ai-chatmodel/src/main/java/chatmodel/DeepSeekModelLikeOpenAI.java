package chatmodel;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;


public class DeepSeekModelLikeOpenAI {
    private static final String DEEP_SEEK_BASE_URL = "https://api.deepseek.com";
    private static final String DEFAULT_DEEP_SEEK_MODEL = "deepseek-chat";
    private static final String DEFAULT_DEEP_SEEK_API_KEY = "sk-4ea07705f6a64ad4a87e73b318c16fec";

    public static void main(String[] args) {
        OpenAiApi openAiApi = OpenAiApi.builder().baseUrl(DEEP_SEEK_BASE_URL).apiKey(DEFAULT_DEEP_SEEK_API_KEY).build();
        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(DEFAULT_DEEP_SEEK_MODEL).build())
                .build();
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .build();
        String userQuestion = "利雅得的天气怎么样";
        System.out.println("> USER: " + userQuestion);
        System.out.println("> ASSISTANT: " + chatClient.prompt(userQuestion).call().content());
    }
}
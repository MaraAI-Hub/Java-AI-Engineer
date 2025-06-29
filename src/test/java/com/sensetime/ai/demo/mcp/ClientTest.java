package com.sensetime.ai.demo.mcp;

/**
 * TDDO
 *
 * @author Xu JianXing
 * @since 0.1
 */

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Map;

@SpringBootTest
public class ClientTest {

    private static final String DEEPSEEK_BASE_URL = "https://api.deepseek.com";

    private static final String DEFAULT_DEEPSEEK_MODEL = "deepseek-chat";

    private static final String DEFAULT_DEEPSEEK_API_KEY = "sk-4ea07705f6a64ad4a87e73b318c16fec";


    @Test
    public void DirectTest() {
        HttpClientSseClientTransport transport = new HttpClientSseClientTransport("http://localhost:8080");
        McpSyncClient client = McpClient.sync(transport).build();
        client.initialize();
        McpSchema.CallToolResult weatherResult = client.callTool(
                new McpSchema.CallToolRequest("getTemperature", Map.of("latitude", "39", "longitude", "116.4074"))
        );


        System.out.println(weatherResult.content().get(0));

        SyncMcpToolCallbackProvider mcpToolProvider = new SyncMcpToolCallbackProvider(Arrays.asList(client));

        OpenAiApi openAiApi = OpenAiApi.builder().baseUrl(DEEPSEEK_BASE_URL).apiKey(DEFAULT_DEEPSEEK_API_KEY).build();

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(DEFAULT_DEEPSEEK_MODEL).build())
                .build();
        ChatClient chatClient = ChatClient.builder(openAiChatModel).defaultTools(mcpToolProvider).build();



        String userQuestion = "利雅得的天气怎么样";

        System.out.println("> USER: " + userQuestion);
        System.out.println("> ASSISTANT: " + chatClient.prompt(userQuestion).call().content());

        String userQuestion2 = "北京的天气怎么样";

        System.out.println("> USER: " + userQuestion2);

        System.out.println("> ASSISTANT: " + chatClient.prompt(userQuestion2).call().content());
    }


}

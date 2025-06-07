package ai.mara.studio;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        ChatClient.Builder builder=
        this.dashScopeChatClient = chatClientBuilder
                .defaultSystem(DEFAULT_PROMPT)
                // TODO
                // 实现 Chat Memory 的 Advisor
                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
//				 .defaultAdvisors(
//						 new MessageChatMemoryAdvisor(new InMemoryChatMemory())
//				 )
                // 实现 Logger 的 Advisor
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();

    }
}


https://api-docs.deepseek.com/zh-cn/api/create-chat-completion
都是普通的调用，Spring ai进行了非常深度的封装。

```java
curl -L -X POST 'https://api.deepseek.com/chat/completions' \
-H 'Content-Type: application/json' \
-H 'Accept: application/json' \
-H 'Authorization: Bearer sk-xxx' \
--data-raw '{
  "messages": [
    {
      "content": "You are a helpful assistant",
      "role": "system"
    },
    {
      "content": "Hi",
      "role": "user"
    }
  ],
  "model": "deepseek-chat",
  "frequency_penalty": 0,
  "max_tokens": 2048,
  "presence_penalty": 0,
  "response_format": {
    "type": "text"
  },
  "stop": null,
  "stream": false,
  "stream_options": null,
  "temperature": 1,
  "top_p": 1,
  "tools": null,
  "tool_choice": "none",
  "logprobs": false,
  "top_logprobs": null
}'
```


```json
{
  "id": "6d369aa3-ae76-4657-8880-a79de5e2aa76",
  "object": "chat.completion",
  "created": 1749382434,
  "model": "deepseek-chat",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "Hello! How can I assist you today? 😊"
      },
      "logprobs": null,
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 9,
    "completion_tokens": 11,
    "total_tokens": 20,
    "prompt_tokens_details": {
      "cached_tokens": 0
    },
    "prompt_cache_hit_tokens": 0,
    "prompt_cache_miss_tokens": 9
  },
  "system_fingerprint": "fp_8802369eaa_prod0425fp8"
}
```
```java
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
        ChatClient.Builder builder = ChatClient.builder(deepSeekChatModel)
                .defaultSystem("You are a helpful assistant");

        ChatClient chatClient = builder.build();
        System.out.println(chatClient.prompt("Hi").call().content());
    }
}
```
可以换成更加Fluent 的调用：
```java
   ChatClient.Builder builder = ChatClient.builder(deepSeekChatModel);
        ChatClient chatClient = builder.build();
        System.out.println(chatClient.prompt()
                .system("You are a helpful assistant")
                .user("Hi")
                .call()
                .content());
```
二者只是形式不同。

# 结构化返回


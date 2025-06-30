
package advisors;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

public class SimpleLoggerAdvisor implements CallAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor.class);

    private ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public String getName() {
        return "SimpleLoggerAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }


//    private AdvisedRequest before(AdvisedRequest request) {
//        try {
//            logger.debug("request: {}",  objectMapper.writeValueAsString(request.messages()));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return request;
//    }
//
//    private void observeAfter(AdvisedResponse advisedResponse) {
//        try {
//            logger.debug("response: {}", objectMapper.writeValueAsString(advisedResponse.response()));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public String toString() {
        return org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor.class.getSimpleName();
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            System.out.println(objectMapper.writeValueAsString(chatClientRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ChatClientResponse res = callAdvisorChain.nextCall(chatClientRequest);
        try {
            System.out.println(objectMapper.writeValueAsString(res));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return  res;
    }

//    /**
//     * 循环通知，执行前和执行后都会调用
//     * @param advisedRequest the advised request
//     * @param chain the advisor chain
//     * @return
//     */
//    @Override
//    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
//
//        advisedRequest = before(advisedRequest);
//
//        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
//
//        observeAfter(advisedResponse);
//
//        return advisedResponse;
//    }


}

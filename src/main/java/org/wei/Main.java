package org.wei;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.util.List;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.Map;


/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年03月17日 11:54
 */
public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        // 创建 OpenAPI 对象
        OpenAPI openAPI = new OpenAPI();

        // 设置基本信息
        Info info = new Info()
                .title("Dynamic API")
                .version("1.0.0");
        openAPI.setInfo(info);

        // 定义请求体内容
        Schema<?> requestSchema = new Schema<Map<String, Object>>()
                .addProperties("type", new Schema<String>().type("string")._enum(List.of("text", "image", "video", "audio", "file")))
                .addProperties("data", new Schema<String>().type("string"));

        Content requestBodyContent = new Content()
                .addMediaType("application/json", new MediaType().schema(requestSchema));

        RequestBody requestBody = new RequestBody()
                .required(true)
                .content(requestBodyContent);

        // 定义响应体内容
        Schema<?> responseSchema = new Schema<Map<String, Object>>()
                .addProperties("status", new Schema<String>().type("string"))
                .addProperties("result", new Schema<String>().type("string"));

        Content responseContent = new Content()
                .addMediaType("application/json", new MediaType().schema(responseSchema));

        ApiResponse apiResponse = new ApiResponse()
                .description("Success response")
                .content(responseContent);

        ApiResponses apiResponses = new ApiResponses()
                .addApiResponse("200", apiResponse);

        // 定义路径及操作
        Operation operation = new Operation()
                .summary("动态返回响应")
                .requestBody(requestBody)
                .responses(apiResponses);

        PathItem pathItem = new PathItem()
                .post(operation);

        Paths paths = new Paths()
                .addPathItem("/dynamic-endpoint", pathItem);

        openAPI.setPaths(paths);

        // 序列化为json
        ObjectMapper objectMapper = new ObjectMapper();
        String yaml = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(openAPI);


        System.out.println(yaml);
    }
}

package bible.bible.service;

import bible.bible.domain.CleanBotResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CleanBotService {

    private static final String GROQ_CHAT_URL = "https://api.groq.com/openai/v1/chat/completions";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    
    @Value("${groq.api.key.file:./secrets/groq-api-key.txt}")
    private String groqApiKeyFilePath;

    private String groqApiKeyCache;

    @Value("${groq.model.text:groq/compound}")
    private String textModel;

    @Value("${groq.model.vision:meta-llama/llama-4-scout-17b-16e-instruct}")
    private String visionModel;

    public CleanBotService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public CleanBotResult checkContent(String userText) {
        String prompt = "너는 게시판 관리자야. 다음 텍스트를 분석해.\n" +
                "텍스트: \"" + userText + "\"\n" +
                "공격적이거나 비속어가 있는지 판단해서 무조건 다음 JSON 형식으로만 답해(마크다운 backtick 없이):\n" +
                "{ \"isSafe\": boolean, \"reason\": string }\n" +
                "reason은 한국어로만 대답해." +
                "안전하다면 reason은 '건전한 게시글입니다'라고 써.";

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> requestBody = Map.of(
                "model", textModel,
                "messages", List.of(message),
                "response_format", Map.of("type", "json_object")
        );

        try {
            String responseString = postToGroq(requestBody);
            JsonNode root = objectMapper.readTree(responseString);
            String content = root.path("choices").path(0).path("message").path("content").asText();

            String jsonContent = extractJsonFromContent(content);
            return objectMapper.readValue(jsonContent, CleanBotResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Groq API 텍스트 검사 실패: " + e.getMessage());
        }
    }

    public CleanBotResult checkImage(MultipartFile file) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            String mimeType = file.getContentType() != null ? file.getContentType() : "image/png";
            String dataUrl = "data:" + mimeType + ";base64," + base64Image;

            String promptText = "이미지를 분석하고 유해성을 판단해. 기준: 선정적, 폭력적, 혐오스러움, 피, 공포. "
                    + "유해 요소가 있으면 isSafe를 false로, 없으면 true로 설정해. "
                    + "reason 필드에는 판단 이유를 한글로 작성해. 반드시 JSON 형식으로만 답해: { \"isSafe\": boolean, \"reason\": string }";

            List<Map<String, Object>> contentParts = List.of(
                    Map.of("type", "text", "text", promptText),
                    Map.of("type", "image_url", "image_url", Map.of("url", dataUrl))
            );

            Map<String, Object> message = Map.of(
                    "role", "user",
                    "content", contentParts
            );

            Map<String, Object> requestBody = Map.of(
                    "model", visionModel,
                    "messages", List.of(message),
                    "response_format", Map.of("type", "json_object")
            );

            String responseString = postToGroq(requestBody);
            JsonNode root = objectMapper.readTree(responseString);
            String content = root.path("choices").path(0).path("message").path("content").asText();

            String jsonContent = extractJsonFromContent(content);
            return objectMapper.readValue(jsonContent, CleanBotResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Groq API 이미지 분석 실패: " + e.getMessage(), e);
        }
    }

    private String getGroqApiKey() {
        if (groqApiKeyCache != null) {
            return groqApiKeyCache;
        }
        if (groqApiKeyFilePath == null || groqApiKeyFilePath.isBlank()) {
            throw new IllegalStateException(
                    "groq.api.key.file 이 설정되지 않았습니다. API 키를 담은 파일 경로를 application.properties에 넣어주세요.");
        }
        try {
            Path path = Paths.get(groqApiKeyFilePath.trim());
            if (!Files.exists(path)) {
                throw new IllegalStateException("Groq API 키 파일이 없습니다: " + path.toAbsolutePath());
            }
            String content = Files.readString(path).trim();
            int newline = content.indexOf('\n');
            groqApiKeyCache = (newline > 0) ? content.substring(0, newline).trim() : content;
            if (groqApiKeyCache.isEmpty()) {
                throw new IllegalStateException("Groq API 키 파일이 비어 있습니다: " + path.toAbsolutePath());
            }
            return groqApiKeyCache;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Groq API 키 파일을 읽는 중 오류: " + e.getMessage(), e);
        }
    }

    private String postToGroq(Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getGroqApiKey());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(GROQ_CHAT_URL, HttpMethod.POST, entity, String.class).getBody();
    }

    private String extractJsonFromContent(String content) {
        int firstBrace = content.indexOf('{');
        int lastBrace = content.lastIndexOf('}');
        if (firstBrace != -1 && lastBrace != -1 && lastBrace > firstBrace) {
            return content.substring(firstBrace, lastBrace + 1);
        }
        return content;
    }
}

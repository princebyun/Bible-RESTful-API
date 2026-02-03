package bible.bible.dto;

import bible.bible.domain.Video;
import java.util.List;

/**
 * 주일말씀 목록 API 응답 DTO
 */
public class SermonsDto {

    private List<Video> videos;
    private String channelUrl;
    private String error;

    public SermonsDto() {
    }

    public static SermonsDto error(String message) {
        SermonsDto dto = new SermonsDto();
        dto.setError(message);
        return dto;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

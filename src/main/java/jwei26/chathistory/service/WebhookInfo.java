package jwei26.chathistory.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class WebhookInfo {
    private String url;
    private boolean hasCustomCertificate;
    private int pendingUpdateCount;
    @JsonProperty("last_error_date")
    private Integer lastErrorDate;
    @JsonProperty("last_error_message")
    private String lastErrorMessage;

    // Getter 和 Setter 方法
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHasCustomCertificate() {
        return hasCustomCertificate;
    }

    public void setHasCustomCertificate(boolean hasCustomCertificate) {
        this.hasCustomCertificate = hasCustomCertificate;
    }

    public int getPendingUpdateCount() {
        return pendingUpdateCount;
    }

    public void setPendingUpdateCount(int pendingUpdateCount) {
        this.pendingUpdateCount = pendingUpdateCount;
    }

    public Integer getLastErrorDate() {
        return lastErrorDate;
    }

    public void setLastErrorDate(Integer lastErrorDate) {
        this.lastErrorDate = lastErrorDate;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    // toString 方法便于调试
    @Override
    public String toString() {
        return "WebhookInfo{" +
                "url='" + url + '\'' +
                ", hasCustomCertificate=" + hasCustomCertificate +
                ", pendingUpdateCount=" + pendingUpdateCount +
                ", lastErrorDate=" + lastErrorDate +
                ", lastErrorMessage='" + lastErrorMessage + '\'' +
                '}';
    }
}


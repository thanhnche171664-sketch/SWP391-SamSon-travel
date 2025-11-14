package entity;

import java.util.Date;

public class Banner {

    private int bannerId;
    private String imageUrl;
    private String title;
    private String description;
    private String targetUrl;
    private Date startAt;
    private Date endAt;
    private String status;

    public Banner() {
    }

    public Banner(int bannerId, String imageUrl, String title, String description,
            String targetUrl, Date startAt, Date endAt, String status) {
        this.bannerId = bannerId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.targetUrl = targetUrl;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

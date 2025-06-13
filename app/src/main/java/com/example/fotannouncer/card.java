package com.example.fotannouncer;

public class card {
    private String title;
    private String content;
    private String imageUrl;
    private long timestamp; // Add timestamp to your model

    // Public no-argument constructor is REQUIRED for Firebase
    public card() {
        // Default constructor required for calls to DataSnapshot.getValue(card.class)
    }

    // You can also add a constructor with arguments if you create card objects programmatically
    public card(String title, String content, String imageUrl, long timestamp) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    // Getters for all fields you want to retrieve from Firebase
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getTimestamp() { // Getter for timestamp
        return timestamp;
    }

    // Optional: Setters if you plan to write data back to Firebase through this object
    // public void setTitle(String title) { this.title = title; }
    // public void setContent(String content) { this.content = content; }
    // public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    // public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

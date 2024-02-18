package edu.northeastern.numad24sp_group4.EventsDisplay_RecyclerView;

public class EventCard{

    private String location;
    private String dateTime;
    private String url;
    private String image;

    private String name;

    public EventCard(String location, String dateTime, String url, String image, String name) {
        this.name = name;
        this.location = location;
        this.dateTime = dateTime;
        this.url = url;
        this.image = image;
    }


    public String toString() {
        return "{name='" + name + "', location=" + location + "', DateTime='" +dateTime+"', url='"+url+"', image='"+image+"'}";
    }
    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() { return location; }

    public String getDateTime() {
        return dateTime;
    }

    public String getUrl() {
        return url;
    }
}

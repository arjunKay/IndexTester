public class Record {

    private String wikiTopic;
    private String url;
    private String abstractText;

    public Record(String wikiTopicRaw, String url, String abstractText) {
        this.wikiTopic = wikiTopicRaw.replace("Wikipedia: ", "");
        this.url = url;
        this.abstractText = abstractText;
    }

    public String getWikiTopic() {
        return wikiTopic;
    }

    public String getUrl() {
        return url;
    }

    public String getAbstractText() {
        return abstractText;
    }
}

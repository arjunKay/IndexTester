public abstract class Metered {

    private String name;
    public Metered(String name) {
        this.name = name;
    }

    public void setQueryData(QuerySet querySet) {

    }

    public String getName() {
        return name;
    }

    public abstract void init();
    public abstract void index(Record record);
    public abstract Statistics query(QuerySet querySet);

    public abstract void teardown();
}

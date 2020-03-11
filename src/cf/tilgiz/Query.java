package cf.tilgiz;

public class Query {
    private String queryString;
    private boolean skip;

    public Query(String queryString) {
        this.queryString = queryString;
    }
    public Query(String queryString, boolean skip) {
        this.queryString = queryString;
        this.skip = skip;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }
}

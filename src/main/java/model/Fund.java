package model;

public class Fund {
    private long id;
    private String name;
    private FundType type;

    public Fund(long id, String name, FundType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FundType getType() {
        return type;
    }

    public void setType(FundType type) {
        this.type = type;
    }
}

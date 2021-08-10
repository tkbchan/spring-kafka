package ph.globe.com.edo.kafka.model;

public class User {
    private String name;
    private int studNo;

    public User(String name, int studNo) {
        this.name = name;
        this.studNo = studNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudNo() {
        return studNo;
    }

    public void setStudNo(int studNo) {
        this.studNo = studNo;
    }
}

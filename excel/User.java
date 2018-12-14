package excel;

public class User {

    private String name;
    private String account;
    private String dept;
    private boolean gender;
    private String Email;

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getDept() {
        return dept;
    }

    public boolean isGender() {
        return gender;
    }

    public String getEmail() {
        return Email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", account=" + account + ", dept=" + dept + ", gender=" + gender + ", Email="
                + Email + "]";
    }


}

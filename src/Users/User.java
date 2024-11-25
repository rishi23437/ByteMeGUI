package Users;

public abstract class User {
    protected String name;
    protected String password;

    public User() {
        this.name = null;
    }

    public User(String name) {
        this.name = name;
    }

    public abstract void print_functionalities();
}
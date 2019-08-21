package configs;

public class ConnectionProfile {
    private String name;
    private String address;
    private String port;
    private String clientName;
    private boolean erasable;

    public ConnectionProfile() {
    }

    public ConnectionProfile(String name, String address, String port, String clientName, boolean erasable) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.clientName = clientName;
        this.erasable = erasable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isErasable() {
        return erasable;
    }

    public void setErasable(boolean erasable) {
        this.erasable = erasable;
    }
}

import java.time.Instant;

public class CustomerInfo {
    private String fullName;
    private String address;
    private Instant orderDate;
    private String invoiceId;

    public CustomerInfo(String fullName, String address, Instant orderDate, String invoiceId) {
        this.fullName = fullName;
        this.address = address;
        this.orderDate = orderDate;
        this.invoiceId = invoiceId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public String toString() {
        return "CustomerInfo{" +
                "fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", orderDate=" + orderDate +
                ", invoiceId='" + invoiceId + '\'' +
                '}';
    }
}

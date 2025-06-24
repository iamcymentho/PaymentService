import com.example.Payment.Domain.PaymentRequest;


public void validatePaymentRequest(PaymentRequest request)
{
    if (request == null || request.getAmount() == null || request.getCustomerEmail() == null)
    {
        throw new IllegalArgumentException("Invalid payment request");
    }

}

void main() {
}
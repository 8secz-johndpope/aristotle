package com.aristotle.core.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "PaymentGatewayDonation")
public class PaymentGatewayDonation extends Donation {

	@Column(name = "merchant_reference_number")
	private String merchantReferenceNumber;
	@Column(name = "payment_gateway")
	private String paymentGateway;
	@Column(name = "payment_gateway_trn_type")
	private String transactionType;
	@Column(name = "pg_error_msg")
	private String pgErrorMessage;
	@Column(name = "pg_error_detail")
	private String pgErrorDetail;

    public String getMerchantReferenceNumber() {
        return merchantReferenceNumber;
    }

    public void setMerchantReferenceNumber(String merchantReferenceNumber) {
        this.merchantReferenceNumber = merchantReferenceNumber;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPgErrorMessage() {
        return pgErrorMessage;
    }

    public void setPgErrorMessage(String pgErrorMessage) {
        this.pgErrorMessage = pgErrorMessage;
    }

    public String getPgErrorDetail() {
        return pgErrorDetail;
    }

    public void setPgErrorDetail(String pgErrorDetail) {
        this.pgErrorDetail = pgErrorDetail;
    }
	

}

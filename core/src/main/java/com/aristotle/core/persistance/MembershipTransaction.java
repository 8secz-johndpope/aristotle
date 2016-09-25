package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "membership_transactions")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="Account", include="all")
public class MembershipTransaction extends BaseEntity {

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "source")
    private String source;
    
    @Column(name = "source_txn_id")
    private String sourceTransactionId;
    
    @Column(name = "amount")
    private String amount;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY )
    @JoinColumn(name="membership_id")
    private Membership membership;
	@Column(name="membership_id", insertable=false,updatable=false)
	private Long membershipId;
	
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSourceTransactionId() {
		return sourceTransactionId;
	}
	public void setSourceTransactionId(String sourceTransactionId) {
		this.sourceTransactionId = sourceTransactionId;
	}
	public Membership getMembership() {
		return membership;
	}
	public void setMembership(Membership membership) {
		this.membership = membership;
	}
	public Long getMembershipId() {
		return membershipId;
	}
	public void setMembershipId(Long membershipId) {
		this.membershipId = membershipId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "MembershipTransaction [transactionDate=" + transactionDate + ", source=" + source + ", sourceTransactionId=" + sourceTransactionId + ", amount=" + amount + ", membershipId=" + membershipId
				+ ", toString()=" + super.toString() + "]";
	}
	
	
}

package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.aristotle.core.enums.PlannedPostStatus;
import com.aristotle.core.enums.PostLocationType;

@Entity
@Table(name = "planned_sms")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "target_type")
public class PlannedSms extends BaseEntity {



	@Column(name = "message", length = 140)
	private String message;

	@Column(name = "posting_time")
	private Date postingTime;

    @Column(name = "target_type", insertable = false, updatable = false)
    private String targetType;

    @Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private PlannedPostStatus status;

	@Column(name = "location_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private PostLocationType locationType;

    @Column(name = "location_id")
	private Long locationId;

	@Column(name = "error_message")
	private String errorMessage;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getPostingTime() {
		return postingTime;
	}

	public void setPostingTime(Date postingTime) {
		this.postingTime = postingTime;
	}

	public PlannedPostStatus getStatus() {
		return status;
	}

	public void setStatus(PlannedPostStatus status) {
		this.status = status;
	}

	public PostLocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(PostLocationType locationType) {
		this.locationType = locationType;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
	

}

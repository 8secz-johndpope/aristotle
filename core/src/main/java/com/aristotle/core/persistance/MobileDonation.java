package com.aristotle.core.persistance;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "MobileDonation")
public class MobileDonation extends Donation {

}

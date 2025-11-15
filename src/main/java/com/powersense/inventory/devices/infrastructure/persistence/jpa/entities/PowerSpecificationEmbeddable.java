package com.powersense.inventory.devices.infrastructure.persistence.jpa.entities;

import com.powersense.inventory.devices.domain.model.valueobjects.PowerSpecification;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PowerSpecificationEmbeddable {
	@Column(nullable = false)
	private int watts;

	@Column
	private Integer voltage;

	@Column
	private Integer amperage;

	public PowerSpecificationEmbeddable() {
	}

	public PowerSpecificationEmbeddable(int watts, Integer voltage, Integer amperage) {
		this.watts = watts;
		this.voltage = voltage;
		this.amperage = amperage;
	}

	public static PowerSpecificationEmbeddable fromDomain(PowerSpecification power) {
		return new PowerSpecificationEmbeddable(power.watts(), power.voltage(), power.amperage());
	}

	public PowerSpecification toDomain() {
		return new PowerSpecification(watts, voltage, amperage);
	}

	public int getWatts() {
		return watts;
	}

	public void setWatts(int watts) {
		this.watts = watts;
	}

	public Integer getVoltage() {
		return voltage;
	}

	public void setVoltage(Integer voltage) {
		this.voltage = voltage;
	}

	public Integer getAmperage() {
		return amperage;
	}

	public void setAmperage(Integer amperage) {
		this.amperage = amperage;
	}
}



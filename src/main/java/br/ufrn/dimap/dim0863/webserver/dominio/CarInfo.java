package br.ufrn.dimap.dim0863.webserver.dominio;

import java.util.Date;

import br.ufrn.dimap.dim0863.webserver.util.DateUtil;

public class CarInfo {

	private Date date;
	private int speed;
	private int rpm;
	
	public CarInfo() {}
	
	public CarInfo(String date, int speed, int rpm) {
		this.date = DateUtil.convertFromString(date);
		this.speed = speed;
		this.rpm = rpm;
	}

	public String getDate() {
		return DateUtil.convertToString(date);
	}

	public void setDate(String date) {
		this.date = DateUtil.convertFromString(date);
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getRpm() {
		return rpm;
	}

	public void setRpm(int rpm) {
		this.rpm = rpm;
	}
	
	@Override
	public String toString() {
		return String.format("%s | Speed = %d | RPM = %d", DateUtil.convertToString(date), speed, rpm);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarInfo other = (CarInfo) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

}

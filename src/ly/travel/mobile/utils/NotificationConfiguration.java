package ly.travel.mobile.utils;

public class NotificationConfiguration {

	private boolean notifyOnStatusChanged = false,
			notifyOnTerminalChanged = false, notifyOnGateChanged = false,
			notifyOnScheduledTimeChanged = false;

	public NotificationConfiguration() {
	}

	public NotificationConfiguration(boolean gateChange,
			boolean terminalChange, boolean statusChange, boolean scheduledTimeChange) {
		this.setNotifyOnGateChanged(gateChange);
		this.setNotifyOnStatusChanged(statusChange);
		this.setNotifyOnScheduledTimeChanged(scheduledTimeChange);
		this.setNotifyOnTerminalChanged(terminalChange);
	}

	public boolean isNotifyOnStatusChanged() {
		return notifyOnStatusChanged;
	}

	public void setNotifyOnStatusChanged(boolean notifyOnStatusChanged) {
		this.notifyOnStatusChanged = notifyOnStatusChanged;
	}

	public boolean isNotifyOnTerminalChanged() {
		return notifyOnTerminalChanged;
	}

	public void setNotifyOnTerminalChanged(boolean notifyOnTerminalChanged) {
		this.notifyOnTerminalChanged = notifyOnTerminalChanged;
	}

	public boolean isNotifyOnGateChanged() {
		return notifyOnGateChanged;
	}

	public void setNotifyOnGateChanged(boolean notifyOnGateChanged) {
		this.notifyOnGateChanged = notifyOnGateChanged;
	}

	public boolean isNotifyOnScheduledTimeChanged() {
		return notifyOnScheduledTimeChanged;
	}

	public void setNotifyOnScheduledTimeChanged(boolean notifyOnScheduledTimeChanged) {
		this.notifyOnScheduledTimeChanged = notifyOnScheduledTimeChanged;
	}

}

package abistech.resseract.config;

public enum Boolean {

	TRUE(true), FALSE(false);

	private final boolean value;

	Boolean(boolean value) {
		this.value = value;
	}

	public boolean isValue() {
		return value;
	}
}

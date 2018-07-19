
public enum StarshipClass {
	SALAMANDER("res/Ship 1/shipTemplate.cfg"), NEUTHIAN("res/Ship 2/shipTemplate2.cfg"),
	ARATECH("res/Ship 3/shipTemplate2.cfg");
	public String path;

	StarshipClass(String configFilePath) {
		this.path = configFilePath;
	}
}

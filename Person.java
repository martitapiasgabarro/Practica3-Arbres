public class Person {
    private String name;
    private String placeOfOrigin;
    private int maritalStatus;

    public static final int SINGLE = 0;
    public static final int MARRIED = 1;
    public static final int DIVORCED = 2;
    public static final int WIDOWED = 3;

    // Constructor que accepta tres paràmetres individuals
    public Person(String name, String placeOfOrigin, int maritalStatus) {
        if (maritalStatus < SINGLE || maritalStatus > WIDOWED) {
            throw new IllegalArgumentException("Invalid marital status.");
        }
        this.name = name;
        this.placeOfOrigin = placeOfOrigin;
        this.maritalStatus = maritalStatus;
    }

    // Constructor que accepta una cadena de text amb format específic
    public Person(String personData) {
        String[] parts = personData.split(", ");
        for (String part : parts) {
            String[] keyValue = part.split(": ");
            switch (keyValue[0].trim()) {
                case "Name" -> this.name = keyValue[1].trim();
                case "place of Origin" -> this.placeOfOrigin = keyValue[1].trim();
                case "marital status" -> this.maritalStatus = parseMaritalStatus(keyValue[1].trim());
            }
        }
    }

    private int parseMaritalStatus(String status) {
        return switch (status.toLowerCase()) {
            case "single" -> SINGLE;
            case "married" -> MARRIED;
            case "divorced" -> DIVORCED;
            case "widowed" -> WIDOWED;
            default -> throw new IllegalArgumentException("Invalid marital status: " + status);
        };
    }

    @Override
    public String toString() {
        String status;
        switch (maritalStatus) {
            case SINGLE:
                status = "Single";
                break;
            case MARRIED:
                status = "Married";
                break;
            case DIVORCED:
                status = "Divorced";
                break;
            case WIDOWED:
                status = "Widowed";
                break;
            default:
                throw new IllegalStateException("Invalid marital status.");
        }
        return "Name: " + name + ", place of Origin: " + placeOfOrigin + ", marital status: " + status;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public int getMaritalStatus() {
        return maritalStatus;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }

    public void setMaritalStatus(int maritalStatus) {
        if (maritalStatus < 0 || maritalStatus > 3) {
            throw new IllegalArgumentException("Invalid marital status.");
        }
        this.maritalStatus = maritalStatus;
    }
}

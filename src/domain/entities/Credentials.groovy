package domain.entities

class Credentials {
    String accessKey
    String secretKey
    String region

    public Credentials(String accessKey, String secretKey, String region) {
        this.accessKey = accessKey
        this.secretKey = secretKey
        this.region = region
    }
}

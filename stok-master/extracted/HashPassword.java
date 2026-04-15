import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class HashPassword {
    public static void main(String[] args) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 32, 1, 16384, 2);
        System.out.print(encoder.encode("moemen123"));
    }
}

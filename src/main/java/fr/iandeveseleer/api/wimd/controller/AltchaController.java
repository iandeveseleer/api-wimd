package fr.iandeveseleer.api.wimd.controller;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.altcha.altcha.Altcha;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/altcha")
@RequiredArgsConstructor
@Setter
public class AltchaController {

    @Value("${ALTCHA_HMAC_KEY:secret-key}") // Default value if env variable is not set
    private String hmacKey;

    @GetMapping("/challenge")
    @CrossOrigin(origins = "*")
    public Altcha.Challenge altcha() {
        try {
            Altcha.ChallengeOptions options = new Altcha.ChallengeOptions();
            options.algorithm = Altcha.Algorithm.SHA256;
            options.hmacKey = hmacKey;

            return Altcha.createChallenge(options);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating challenge", e);
        }
    }

    @PostMapping("/submit")
    @CrossOrigin(origins = "*")
    public Map<String, Object> submit(@RequestParam Map<String, String> formData) {
        Map<String, Object> response = new HashMap<>();
        try {
            String payload = formData.get("altcha");

            if (payload == null) {
                response.put("success", false);
                response.put("error", "'altcha' field is missing");
                return response;
            }

            boolean isValid = Altcha.verifySolution(payload, hmacKey, true);
            response.put("success", isValid);
            response.put("data", formData);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Error verifying solution: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/submit_spam_filter")
    @CrossOrigin(origins = "*")
    public Map<String, Object> submitSpamFilter(@RequestParam Map<String, String> formData) {
        Map<String, Object> response = new HashMap<>();
        try {
            String payload = formData.get("altcha");

            Altcha.ServerSignatureVerification verification;

            verification = Altcha.verifyServerSignature(payload, hmacKey);

            response.put("success", verification.verified);
            response.put("data", formData);
            response.put("verificationData", verification.verificationData);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Error verifying server signature: " + e.getMessage());
        }
        return response;
    }
}

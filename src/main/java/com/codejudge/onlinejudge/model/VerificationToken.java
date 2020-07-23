package com.codejudge.onlinejudge.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "verificationtokens")
@Getter
@Setter
public class VerificationToken extends Auditable {

    private static final int VALIDITY_TIME = 4 * 60;

    @NotBlank
    private String token;

    @OneToOne(targetEntity = User.class)
    private User user;

    private Date expiryTime;

    public VerificationToken() {
        super();
    }

    public VerificationToken(User user) {

        this.token = generateRandomUniqueToken();
        
        this.user = user;
        
        this.expiryTime = calculateExpiryTime();
    }

    private void updateToken() {
        this.token = generateRandomUniqueToken();
        this.expiryTime = calculateExpiryTime();
    }

    private String generateRandomUniqueToken() {
        return UUID.randomUUID().toString();
    }

    private Date calculateExpiryTime() {

        Calendar calendar = Calendar.getInstance();
        Date currentTimeAndDate = new Date();

        calendar.setTimeInMillis(currentTimeAndDate.getTime());
        calendar.add(Calendar.MINUTE, VALIDITY_TIME);

        return calendar.getTime();
    }
}

package com.optimax.pms.guest;

import com.optimax.pms.security.EncryptedStringAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Example entity with encrypted PII fields to demonstrate GDPR compliance.
 */
@Entity
@Table(name = "guests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Convert(converter = EncryptedStringAttributeConverter.class)
    @Column(nullable = false, length = 512)
    private String email;

    @Convert(converter = EncryptedStringAttributeConverter.class)
    @Column(length = 512)
    private String phone;

    @Convert(converter = EncryptedStringAttributeConverter.class)
    @Column(length = 1024)
    private String address;
}


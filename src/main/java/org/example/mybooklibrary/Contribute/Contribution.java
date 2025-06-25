package org.example.mybooklibrary.Contribute;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookName;
    private String authorName;
    private String category;
    private String language;
    private String contributorName;
    private LocalDateTime contributionDate;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<String> availableFormats;
    @Enumerated(EnumType.STRING)
    private ContributionStatus status;
}

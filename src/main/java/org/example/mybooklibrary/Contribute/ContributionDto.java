package org.example.mybooklibrary.Contribute;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

    @Data
    public class ContributionDto {
        private String bookName;
        private String authorName;
        private String category;
        private String language;
        private String contributorName;
        private LocalDateTime contributionDate;
        private List<String> availableFormats;
    }




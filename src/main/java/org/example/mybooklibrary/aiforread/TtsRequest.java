package org.example.mybooklibrary.aiforread;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TtsRequest {
    private String text;

    private String voice = "Brian";
}

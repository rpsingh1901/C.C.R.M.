package edu.ccrm.service;

import edu.ccrm.domain.Transcript;

import java.util.UUID;

public interface TranscriptService {
    Transcript generateTranscript(UUID studentId);
}

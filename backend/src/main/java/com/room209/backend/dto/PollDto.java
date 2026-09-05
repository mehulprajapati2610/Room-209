package com.room209.backend.dto;

import com.room209.backend.entity.Poll;
import com.room209.backend.entity.PollOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PollDto {
    private Long id;
    private String question;
    private UserDto createdBy;
    private boolean active;
    private LocalDateTime createdAt;
    private int totalVotes;
    private List<PollOptionDto> options;
    private boolean hasVoted;
    private Long userVotedOptionId;

    public static class PollOptionDto {
        private Long id;
        private String optionText;
        private int voteCount;
        private int percentage;

        public PollOptionDto() {}
        public PollOptionDto(PollOption option, int totalVotes) {
            this.id = option.getId();
            this.optionText = option.getOptionText();
            this.voteCount = option.getVoteCount();
            this.percentage = totalVotes > 0 ? (int) Math.round(((double) option.getVoteCount() / totalVotes) * 100) : 0;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getOptionText() { return optionText; }
        public void setOptionText(String optionText) { this.optionText = optionText; }

        public int getVoteCount() { return voteCount; }
        public void setVoteCount(int voteCount) { this.voteCount = voteCount; }

        public int getPercentage() { return percentage; }
        public void setPercentage(int percentage) { this.percentage = percentage; }
    }

    public PollDto() {}

    public PollDto(Poll poll, Long currentUserId, Long userVotedOptionId) {
        this.id = poll.getId();
        this.question = poll.getQuestion();
        this.createdBy = new UserDto(poll.getCreatedBy());
        this.active = poll.isActive();
        this.createdAt = poll.getCreatedAt();

        int sum = 0;
        if (poll.getOptions() != null) {
            for (PollOption opt : poll.getOptions()) {
                sum += opt.getVoteCount();
            }
        }
        this.totalVotes = sum;

        final int total = sum;
        if (poll.getOptions() != null) {
            this.options = poll.getOptions().stream()
                    .map(opt -> new PollOptionDto(opt, total))
                    .collect(Collectors.toList());
        }
        this.hasVoted = userVotedOptionId != null;
        this.userVotedOptionId = userVotedOptionId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public UserDto getCreatedBy() { return createdBy; }
    public void setCreatedBy(UserDto createdBy) { this.createdBy = createdBy; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getTotalVotes() { return totalVotes; }
    public void setTotalVotes(int totalVotes) { this.totalVotes = totalVotes; }

    public List<PollOptionDto> getOptions() { return options; }
    public void setOptions(List<PollOptionDto> options) { this.options = options; }

    public boolean isHasVoted() { return hasVoted; }
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }

    public Long getUserVotedOptionId() { return userVotedOptionId; }
    public void setUserVotedOptionId(Long userVotedOptionId) { this.userVotedOptionId = userVotedOptionId; }
}

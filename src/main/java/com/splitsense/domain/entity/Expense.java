package com.splitsense.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    private LocalDateTime date;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private Set<ExpenseSplit> splits;

    // Constructors, getters, setters
    public Expense() {}

    public Expense(String description, BigDecimal amount, User paidBy, Group group, LocalDateTime date) {
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.group = group;
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public User getPaidBy() { return paidBy; }
    public void setPaidBy(User paidBy) { this.paidBy = paidBy; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Set<ExpenseSplit> getSplits() { return splits; }
    public void setSplits(Set<ExpenseSplit> splits) { this.splits = splits; }
}

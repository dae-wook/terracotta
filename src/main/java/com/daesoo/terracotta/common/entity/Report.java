package com.daesoo.terracotta.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "reports")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @ManyToOne
    private Member reportedBy;

    @ManyToOne
    private SchematicPost schematicPost;

    @ManyToOne
    private Comment comment;
    
    @ManyToOne
    private Reply reply;
}

package com.corkili.husky.schedule;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;
import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.corkili.husky.common.Constants;
import com.corkili.husky.email.EmailPO;

@Entity
@Table(name = "t_reminder")
@SQLDelete(sql = "update t_reminder set deleted = " + Constants.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_reminder set deleted = " + Constants.DELETED + " where id = ?")
@Where(clause = "deleted = " + Constants.EXISTED)
@WhereJoinTable(clause = "deleted = " + Constants.EXISTED)
@Getter
@Setter
@ToString
public class ReminderPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "create_time", updatable = false, nullable = false,
            columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "update_time", nullable = false,
            columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = "deleted", nullable = false)
    @Range(min = Constants.EXISTED, max = Constants.DELETED)
    @NotNull
    private byte deleted;

    @Column(name = "reminder_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date reminderTime;

    @Column(name = "reminder_type", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private ReminderType reminderType;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "email_id")
    @Fetch(FetchMode.JOIN)
    private EmailPO reminderEmail;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "schedule_id")
    @Fetch(FetchMode.JOIN)
    private SchedulePO belongSchedule;
    
}

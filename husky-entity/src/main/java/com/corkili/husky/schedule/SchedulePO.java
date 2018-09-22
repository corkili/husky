package com.corkili.husky.schedule;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
import com.corkili.husky.user.UserPO;

@Entity
@Table(name = "t_schedule")
@SQLDelete(sql = "update t_schedule set deleted = " + Constants.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_schedule set deleted = " + Constants.DELETED + " where id = ?")
@Where(clause = "deleted != " + Constants.DELETED)
@WhereJoinTable(clause = "deleted != " + Constants.DELETED)
@Getter
@Setter
@ToString
public class SchedulePO {

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

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date endTime;

    @Column(name = "summary", nullable = false, length = 128)
    @NotBlank
    @Size(min = 1, max = 128)
    private String summary;

    @Column(name = "describes", nullable = false, length = 512)
    @NotNull
    @Size(max = 512)
    private String describes;

    @OneToMany(mappedBy = "belongSchedule", fetch = FetchType.EAGER)
    private List<ReminderPO> reminders;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.JOIN)
    private UserPO belongUser;

}

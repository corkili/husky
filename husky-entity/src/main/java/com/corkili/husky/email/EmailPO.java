package com.corkili.husky.email;

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
@Table(name = "t_email")
@SQLDelete(sql = "update t_email set deleted = " + Constants.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_email set deleted = " + Constants.DELETED + " where id = ?")
@Where(clause = "deleted = " + Constants.EXISTED)
@WhereJoinTable(clause = "deleted = " + Constants.EXISTED)
@Getter
@Setter
@ToString
public class EmailPO {

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

    @Column(name = "email_address", nullable = false)
    @javax.validation.constraints.Email
    @NotBlank
    private String emailAddress;


    @Column(name = "state", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    @NotNull
    private EmailState state;

    @Column(name = "auth_code", nullable = false, length = 128)
    @Size(max = 128)
    @NotNull
    private String authCode;

    @Column(name = "is_accessible", nullable = false)
    private boolean accessible;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.JOIN)
    private UserPO belongUser;
}

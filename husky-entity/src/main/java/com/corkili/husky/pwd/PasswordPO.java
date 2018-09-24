package com.corkili.husky.pwd;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "t_password")
@SQLDelete(sql = "update t_password set deleted = " + Constants.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_password set deleted = " + Constants.DELETED + " where id = ?")
@Where(clause = "deleted = " + Constants.EXISTED)
@WhereJoinTable(clause = "deleted = " + Constants.EXISTED)
@Getter
@Setter
@ToString
public class PasswordPO {

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
    private byte deleted;

    @Column(name = "accounts", nullable = false, length = 512)
    @NotBlank
    @Size(min = 1, max = 512)
    private String accounts;

    @Column(name = "summary", nullable = false, length = 512)
    @NotBlank
    @Size(min = 1, max = 512)
    private String summary;

    @Column(name = "password", nullable = false, length = 512)
    @NotBlank
    @Size(min = 1, max = 512)
    private String password;

    @Column(name = "phones", nullable = false, length = 128)
    @NotNull
    @Size(max = 128)
    private String phones;

    @Column(name = "emails", nullable = false, length = 512)
    @NotNull
    @Size(max = 512)
    private String emails;

    @Column(name = "qqs", nullable = false, length = 128)
    @NotNull
    @Size(max = 128)
    private String qqs;

    @Column(name = "wechats", nullable = false, length = 128)
    @NotNull
    @Size(max = 128)
    private String wechats;

    @Column(name = "others", nullable = false, length = 1024)
    @NotNull
    @Size(max = 1024)
    private String remark;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.JOIN)
    private UserPO belongUser;

}

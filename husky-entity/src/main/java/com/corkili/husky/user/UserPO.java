package com.corkili.husky.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;
import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.corkili.husky.common.State;

@Entity
@Table(name = "t_user")
@SQLDelete(sql = "update t_user set state = " + State.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_user set state = " + State.DELETED + " where id = ?")
@Where(clause = "state != " + State.DELETED)
@WhereJoinTable(clause = "state != " + State.DELETED)
@Getter
@Setter
@ToString
public class UserPO {

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

    @Column(name = "state", nullable = false)
    @Range(min = State.INIT, max = State.DELETED)
    @NotNull
    private int state;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 128)
    @NotBlank
    @Size(min = 6, max = 128)
    private String password;

}

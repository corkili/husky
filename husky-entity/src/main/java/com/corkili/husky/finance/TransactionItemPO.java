package com.corkili.husky.finance;

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
import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.corkili.husky.common.State;
import com.corkili.husky.user.UserPO;

@Entity
@Table(name = "t_transaction_item")
@SQLDelete(sql = "update t_transaction_item set state = " + State.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_transaction_item set state = " + State.DELETED + " where id = ?")
@Where(clause = "state != " + State.DELETED)
@Getter
@Setter
@ToString
public class TransactionItemPO {

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

    @Column(name = "book_time", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @NotNull
    private Date bookTime;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionType transactionType;

    @Column(name = "money", nullable = false, scale = 3)
    @NotNull
    private double money;

    @Column(name = "summary", nullable = false, length = 64)
    @Size(min = 1, max = 64)
    @NotBlank
    private String summary;

    @Column(name = "describe", nullable = false, length = 1024)
    @Size(min = 1, max = 1024)
    @NotBlank
    private String describe;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.JOIN)
    private UserPO belongUser;

}

package com.corkili.husky.finance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionItemRepository extends JpaRepository<TransactionItemPO, Long> {

}

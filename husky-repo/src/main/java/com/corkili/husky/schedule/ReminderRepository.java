package com.corkili.husky.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<ReminderPO, Long> {

}

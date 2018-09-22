package com.corkili.husky.diary;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryBookRepository extends JpaRepository<DiaryBookPO, Long> {

}

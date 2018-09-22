package com.corkili.husky.diary;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<DiaryPO, Long> {

}

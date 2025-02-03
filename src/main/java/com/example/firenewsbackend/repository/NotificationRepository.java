package com.example.firenewsbackend.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Repository;

import javax.management.Notification;
import java.util.List;

//@Repository
//public interface NotificationRepository extends JpaRepository<Notification, Long> {
//
//    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.startTime <= CURRENT_TIMESTAMP AND n.endTime >= CURRENT_TIMESTAMP AND n.isDelete = 0")
//    List<Notification> findActiveNotifications(@Param("userId") String userId);
//}


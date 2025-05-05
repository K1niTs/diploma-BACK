// src/main/java/com/example/rental/repo/MessageRepo.java
package com.example.rental.repo;

import com.example.rental.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    @Query("""
      select m from Message m
       where (m.sender.id = :me   and m.recipient.id = :other)
          or (m.sender.id = :other and m.recipient.id = :me)
       order by m.createdAt
    """)
    List<Message> findConversation(Long me, Long other);
}

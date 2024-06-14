package jwei26.chathistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jwei26.chathistory.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

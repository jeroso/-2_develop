package com.github.prgrms.social.repository.user;

import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.user.ConnectedUser;
import com.github.prgrms.social.model.user.Email;
import com.github.prgrms.social.model.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static com.github.prgrms.social.util.DateTimeUtils.dateTimeOf;
import static com.github.prgrms.social.util.DateTimeUtils.timestampOf;
import static java.util.Optional.ofNullable;

@Repository
public class JdbcUserRepository implements UserRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public User insert(User user) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(conn -> {
      // TODO 이름 프로퍼티 처리
      PreparedStatement ps = conn.prepareStatement("INSERT INTO users(seq,email,passwd,name,login_count,last_login_at,create_at) VALUES (null,?,?,?,?,?,?)", new String[]{"seq"});
      ps.setString(1, user.getEmail().getAddress());
      ps.setString(2, user.getPassword());
      ps.setString(3, user.getName());
      ps.setInt(4, user.getLoginCount());
      ps.setTimestamp(5, timestampOf(user.getLastLoginAt().orElse(null)));
      ps.setTimestamp(6, timestampOf(user.getCreateAt()));
      return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    long generatedSeq = key != null ? key.longValue() : -1;
    return new User.Builder(user)
      .seq(generatedSeq)
      .build();
  }

  @Override
  public void update(User user) {
    // TODO 이름 프로퍼티 처리
    jdbcTemplate.update(
      "UPDATE users SET passwd=?,name=?,login_count=?,last_login_at=? WHERE seq=?",
      user.getPassword(),
      user.getName(),
      user.getLoginCount(),
      user.getLastLoginAt().orElse(null),
      user.getSeq()
    );
  }

  @Override
  public Optional<User> findById(Id<User, Long> userId) {
    List<User> results = jdbcTemplate.query(
      "SELECT * FROM users WHERE seq=?",
      mapper,
      userId.value()
    );
    return ofNullable(results.isEmpty() ? null : results.get(0));
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    List<User> results = jdbcTemplate.query(
      "SELECT * FROM users WHERE email=?",
      mapper,
      email.getAddress()
    );
    return ofNullable(results.isEmpty() ? null : results.get(0));
  }

  @Override
  public List<ConnectedUser> findAllConnectedUser(Id<User, Long> userId) {
    // TODO 이름 프로퍼티 처리
    return jdbcTemplate.query(
      "SELECT u.seq, u.email, u.name, c.granted_at FROM connections c JOIN users u ON c.target_seq = u.seq WHERE c.user_seq=? AND c.granted_at is not null ORDER BY seq DESC",
      (rs, rowNum) -> new ConnectedUser(
        rs.getLong("seq"),
        new Email(rs.getString("email")),
        rs.getString("name"),
        dateTimeOf(rs.getTimestamp("granted_at"))
      ),
      userId.value()
    );
  }

  @Override
  public List<Id<User, Long>> findConnectedIds(Id<User, Long> userId) {
    return jdbcTemplate.query(
      "SELECT target_seq FROM connections WHERE user_seq=? AND granted_at is not null ORDER BY target_seq",
      (rs, rowNum) -> Id.of(User.class, rs.getLong("target_seq")),
      userId.value()
    );
  }

  // TODO 이름 프로퍼티 처리
  static RowMapper<User> mapper = (rs, rowNum) -> new User.Builder()
    .seq(rs.getLong("seq"))
    .email(new Email(rs.getString("email")))
    .name(rs.getString("name"))
    .password(rs.getString("passwd"))
    .loginCount(rs.getInt("login_count"))
    .lastLoginAt(dateTimeOf(rs.getTimestamp("last_login_at")))
    .createAt(dateTimeOf(rs.getTimestamp("create_at")))
    .build();

}

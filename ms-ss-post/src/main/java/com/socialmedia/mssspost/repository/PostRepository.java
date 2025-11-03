package com.socialmedia.mssspost.repository;

import com.socialmedia.mssspost.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUsername(String username, Sort createdAt);
    List<Post> findAllByUsernameIn(List<String> usernames, Sort sort);
    @Query(value = """
        SELECT p.*
        FROM post p
        WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(p.content) LIKE CONCAT('%', LOWER(:keyword), '%'))
          AND (:createdBefore IS NULL
               OR p.created_at < :createdBefore
               OR (p.created_at = :createdBefore AND p.id < :idBefore))
        ORDER BY p.created_at DESC, p.id DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Post> searchFeed(@Param("keyword") String keyword,
                          @Param("createdBefore") LocalDateTime createdBefore,
                          @Param("idBefore") Integer idBefore,
                          @Param("limit") int limit );

    @Query(value = """
        SELECT p.*
        FROM post p
        WHERE (:createdBefore IS NULL
               OR p.created_at < :createdBefore
               OR (p.created_at = :createdBefore AND p.id < :idBefore))
        ORDER BY p.created_at DESC, p.id DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Post> fetchFeed(
            @Param("createdBefore") LocalDateTime createdBefore,
            @Param("idBefore") Integer idBefore,
            @Param("limit") int limit
    );

    @Query(value = """
                SELECT p.*
                FROM post p
                WHERE ((:createdBefore IS NULL
                      OR p.created_at < :createdBefore)
                      AND p.username = :username)
                ORDER BY p.created_at DESC
                LIMIT :limit
            """, nativeQuery = true)
    List<Post> fetchByUsername(@Param("username") String username,
                               @Param("createdBefore") LocalDateTime createdBefore,
                               @Param("limit") int limit);

    @Query(value = """
            SELECT p.*
            FROM post p
            WHERE ((:createdBefore IS NULL
               OR p.created_at < :createdBefore
               OR (p.created_at = :createdBefore AND p.id < :idBefore))
               AND p.username IN (:usernames)
               )
            ORDER BY p.created_at DESC, p.id DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Post> fetchFollowed(@Param("usernames")List<String> usernames,
                             @Param("createdBefore") LocalDateTime createdBefore,
                             @Param("idBefore") Integer idBefore,
                             @Param("limit") int limit);

    Page<Post> findByUsername(String username, Pageable pageable);
    Page<Post> findByUsernameIn(List<String> usernames, Pageable pageable);
    List<Post> findByReported(boolean reported);
    long countByReportedTrue();
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.reported = true WHERE p.id = :postId")
    void reportPost(Integer postId);

    void deleteAllByUsername(String username);
}

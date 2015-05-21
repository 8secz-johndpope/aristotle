package com.aristotle.core.forums.persistance.repo;




import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.forums.persistance.Post;
import com.aristotle.core.persistance.repo.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
@DatabaseSetup(PostRepositoryTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { PostRepositoryTest.DATASET })

public class PostRepositoryTest extends BaseRepositoryTest {
    protected static final String DATASET = "classpath:PostRepository/test01.xml";

    @Autowired
    private PostRepository postRepository;

    @Test
    public void testfindPostbyId() {
        Post post = postRepository.findById(1L);
        Assert.assertEquals(1L, post.getId());
        System.out.println("Post=" + post);
    }
}

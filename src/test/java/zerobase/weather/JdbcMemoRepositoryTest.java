package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;
import zerobase.weather.repository.JdbcMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest(){
        //given
        Memo newMemo = new Memo(1,"memo");
        //when
        jdbcMemoRepository.save(newMemo);
        //then
        Optional<Memo> memo = jdbcMemoRepository.findById(1);
        assertEquals(memo.get().getText(), "memo");
    }

    @Test
    void findAllMemoTest(){
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }
}

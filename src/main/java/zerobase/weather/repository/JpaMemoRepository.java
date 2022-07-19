package zerobase.weather.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.weather.domain.Memo;

@Repository
public interface JpaMemoRepository extends JpaRepository<Memo, Integer>{

}

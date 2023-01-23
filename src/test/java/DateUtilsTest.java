import app.foot.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateUtilsTest {
    app.foot.utils.DateUtils dateUtils = new DateUtils();

    @Test
    void hello(){ assertEquals(1,1); }

//    Test the method parseDate function
    @Test
    void parse_date_ok(){
        assertEquals(LocalDate.now().toString() , dateUtils.parseDate(Instant.now()));
    }

//    AssertThrows( expection expected, function executed )
    @Test
    void parse_date_ko(){
        assertThrows( NullPointerException.class , () -> dateUtils.parseDate(null));
    }

}

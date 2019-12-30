package developer.android.com.enlightme

import developer.android.com.enlightme.Debate.ConcurentOp.DeleteStr
import developer.android.com.enlightme.Debate.ConcurentOp.InsertStr
import developer.android.com.enlightme.Objects.DebateEntity
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class InsertStrUnitTest {
    @Test
    fun test_perform() {
        val debateEntity = DebateEntity()
        debateEntity.title = "ABC"
        debateEntity.description = "ABC"
        var insertStrTitle = InsertStr( 0, "0", "title")
        var insertStrDesc = InsertStr( 0, "0", "description")
        insertStrTitle.perform(debateEntity)
        assert(debateEntity.title == "0ABC")
        insertStrDesc.perform(debateEntity)
        assert(debateEntity.description == "0ABC")
        insertStrTitle = InsertStr( 4, "1", "title")
        insertStrDesc = InsertStr( 4, "1", "description")
        insertStrTitle.perform(debateEntity)
        assert(debateEntity.title == "0ABC1")
        insertStrDesc.perform(debateEntity)
        assert(debateEntity.description == "0ABC1")
        insertStrTitle = InsertStr( 3, "2", "title")
        insertStrDesc = InsertStr( 3, "2", "description")
        insertStrTitle.perform(debateEntity)
        assert(debateEntity.title == "0AB2C1")
        insertStrDesc.perform(debateEntity)
        assert(debateEntity.description == "0AB2C1")
    }
    @Test
    fun test_forward(){
        //No change expected
        val debateEntity = DebateEntity()
        debateEntity.title = "on systems"
        var insertStr = InsertStr( 2, "e", "title")
        var deleteStr = DeleteStr( 9,1, "title")
        insertStr.forward(deleteStr)
        deleteStr.perform(debateEntity)
        insertStr.perform(debateEntity)
        assert(debateEntity.title == "one system")

        debateEntity.description = "on systems"
        insertStr = InsertStr( 2, "e", "description")
        deleteStr = DeleteStr( 9,1, "description")
        insertStr.forward(deleteStr)
        deleteStr.perform(debateEntity)
        insertStr.perform(debateEntity)
        assert(debateEntity.description == "one system")

        //Changes expected here
        debateEntity.title = "one system"
        insertStr = InsertStr( 10, "s", "title")
        deleteStr = DeleteStr( 2,1, "title")
        insertStr.forward(deleteStr)
        deleteStr.perform(debateEntity)
        insertStr.perform(debateEntity)
        assert(debateEntity.title == "on systems")

        //Changes expected here with two insertion
        debateEntity.title = "one system"
        val insertStr0 = InsertStr( 1, "o", "title")
        val insertStr1 = InsertStr( 2,"n", "title")
        insertStr1.forward(insertStr0)
        insertStr0.perform(debateEntity)
        insertStr1.perform(debateEntity)
        assert(debateEntity.title == "oonne system")
    }
    @Test
    fun test_backward(){
        val debateEntity = DebateEntity()
        debateEntity.title = "ABC"
        var insertStr = InsertStr( 2, "b", "title")
        var deleteStr = DeleteStr( 1, 1, "title")
        insertStr.perform(debateEntity)
        deleteStr.perform(debateEntity)
        assert(debateEntity.title == "AbC")
        debateEntity.title = "ABC"
        val ins_bck = deleteStr.backward(insertStr)
        deleteStr.perform(debateEntity)
        ins_bck.perform(debateEntity)
        assert(debateEntity.title == "AbC")
    }
    @Test
    fun test_buver(){
        val debateEntity = DebateEntity()
        // Test site 1
        debateEntity.title = "buver"
        var insertStr0 = InsertStr( 2, "f", "title")
        var deleteStr = DeleteStr( 2, 1, "title")
        var insertStr1 = InsertStr( 3, "f", "title")
        insertStr0.perform(debateEntity)
        //System.out.println(debateEntity.title)
        deleteStr.forward(insertStr0)
        deleteStr.perform(debateEntity)
        //System.out.println(debateEntity.title)
        insertStr1.forward(insertStr0)
        insertStr1.forward(deleteStr)
        insertStr1.perform(debateEntity)
        //System.out.println(debateEntity.title)
        assert(debateEntity.title == "buffer")

        // Test site 2
        debateEntity.title = "buver"
        insertStr0 = InsertStr( 2, "f", "title")
        deleteStr = DeleteStr( 2, 1, "title")
        insertStr1 = InsertStr( 3, "f", "title")
        deleteStr.perform(debateEntity)
        //System.out.println(debateEntity.title)
        insertStr0.forward(deleteStr)
        //System.out.println(insertStr0.del_after.toString())
        //System.out.println(insertStr0.del_befor.toString())
        insertStr0.perform(debateEntity)
        //System.out.println(debateEntity.title)
        insertStr1.forward(deleteStr)
        //System.out.println(insertStr1.del_after.toString())
        //System.out.println(insertStr1.del_befor.toString())
        insertStr1.forward(insertStr0)
        insertStr1.perform(debateEntity)
        //System.out.println(debateEntity.title)
        assert(debateEntity.title == "buffer")

        // Test site 3
        debateEntity.title = "buver"
        insertStr0 = InsertStr( 2, "f", "title")
        deleteStr = DeleteStr( 2, 1, "title")
        insertStr1 = InsertStr( 3, "f", "title")
        insertStr1.perform(debateEntity)
        //System.out.println(debateEntity.title)
        insertStr0.forward(insertStr1)
        insertStr0.perform(debateEntity)
        //System.out.println(debateEntity.title)
        deleteStr.forward(insertStr1)
        deleteStr.forward(insertStr0)
        deleteStr.perform(debateEntity)
        //System.out.println(debateEntity.title)
        assert(debateEntity.title == "buffer")
    }
}

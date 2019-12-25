package developer.android.com.enlightme

import developer.android.com.enlightme.Debate.ConcurentOp.DeleteStr
import developer.android.com.enlightme.Debate.ConcurentOp.InsertStr
import developer.android.com.enlightme.objects.DebateEntity
import org.junit.Test

class DeleteStrUnitTest {
    @Test
    fun test_perform() {
        val debateEntity = DebateEntity()
        debateEntity.title = "ABC"
        debateEntity.description = "ABC"
        var deleteStrTitle = DeleteStr(0, 1, "title")
        var deleteStrDesc = DeleteStr( 0, 1, "description")
        deleteStrTitle.perform(debateEntity)
        assert(debateEntity.title == "BC")
        deleteStrDesc.perform(debateEntity)
        assert(debateEntity.description == "BC")
        debateEntity.title = "ABC"
        debateEntity.description = "ABC"
        deleteStrTitle = DeleteStr( 1, 1, "title")
        deleteStrDesc = DeleteStr( 1, 1, "description")
        deleteStrTitle.perform(debateEntity)
        assert(debateEntity.title == "AC")
        deleteStrDesc.perform(debateEntity)
        assert(debateEntity.description == "AC")
        debateEntity.title = "ABC"
        debateEntity.description = "ABC"
        deleteStrTitle = DeleteStr( 2, 1, "title")
        deleteStrDesc = DeleteStr( 2, 1, "description")
        deleteStrTitle.perform(debateEntity)
        assert(debateEntity.title == "AB")
        deleteStrDesc.perform(debateEntity)
        assert(debateEntity.description == "AB")
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

        debateEntity.title = "one system"
        insertStr = InsertStr( 10, "s", "title")
        deleteStr = DeleteStr( 2,1, "title")
        deleteStr.forward(insertStr)
        deleteStr.perform(debateEntity)
        insertStr.perform(debateEntity)
        assert(debateEntity.title == "on systems")

        debateEntity.title = "one system"
        val deleteStr0 = DeleteStr( 1, 1, "title")
        val deleteStr1 = DeleteStr( 2,1, "title")
        deleteStr1.forward(deleteStr0)
        deleteStr0.perform(debateEntity)
        deleteStr1.perform(debateEntity)
        assert(debateEntity.title == "o system")
    }
    @Test
    fun test_backward(){
        val debateEntity = DebateEntity()
        debateEntity.title = "ABC"
        var insertStr = InsertStr( 1, "b", "title")
        var deleteStr = DeleteStr( 1, 1, "title")
        deleteStr.perform(debateEntity)
        insertStr.perform(debateEntity)
        assert(debateEntity.title == "AbC")
        debateEntity.title = "ABC"
        val del_bck = insertStr.backward(deleteStr)
        insertStr.perform(debateEntity)
        del_bck.perform(debateEntity)
        assert(debateEntity.title == "AbC")
    }
}
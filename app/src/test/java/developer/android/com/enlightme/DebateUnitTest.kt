package developer.android.com.enlightme

import developer.android.com.enlightme.Debate.ConcurentOp.DeleteStr
import developer.android.com.enlightme.Debate.ConcurentOp.InsertStr
import developer.android.com.enlightme.Objects.Debate
import developer.android.com.enlightme.Objects.DebateEntity
import developer.android.com.enlightme.Objects.HistElt
import developer.android.com.enlightme.Objects.UpdatePayload
import org.junit.Test

class DebateUnitTest {
    lateinit var dS1: Debate
    lateinit var dS2: Debate
    lateinit var dS3: Debate
    lateinit var histEltS1Op1: HistElt
    lateinit var histEltS1Op2: HistElt
    lateinit var histEltS2Op1: HistElt
    lateinit var histEltS3Op1: HistElt
    lateinit var debS1: DebateEntity
    lateinit var debS2: DebateEntity
    lateinit var debS3: DebateEntity

    fun initTelefonTest(target: String){
        // Telephone test for integration
        //debate entities
        dS1 = Debate()
        debS1 = DebateEntity()
        dS1.debateEntity = debS1
        debS1.title = "telefone"
        debS1.description = "telefone"
        dS2 = Debate()
        debS2 = DebateEntity()
        dS2.debateEntity = debS2
        debS2.title = "telefone"
        debS2.description = "telefone"
        //operations
        var s2Op1 = DeleteStr(4,1, target)
        var s1Op1 = InsertStr(4,"p", target)
        var s1Op2 = InsertStr(5,"h", target)
        //HistElt that contains the operations
        histEltS1Op1 = HistElt("s1", s1Op1, debS1.state_vector)
        histEltS1Op2 = HistElt("s1", s1Op2, debS1.state_vector)
        histEltS2Op1 = HistElt("s2", s2Op1, debS2.state_vector)
    }
    fun initBuverTest(target: String){
        // Buver test for integration
        //debate entities
        dS1 = Debate()
        debS1 = DebateEntity()
        dS1.debateEntity = debS1
        debS1.title = "buver"
        debS1.description = "buver"
        dS2 = Debate()
        debS2 = DebateEntity()
        dS2.debateEntity = debS2
        debS2.title = "buver"
        debS2.description = "buver"
        dS3 = Debate()
        debS3 = DebateEntity()
        dS3.debateEntity = debS3
        debS3.title = "buver"
        debS3.description = "buver"
        //operations
        var s1Op1 = InsertStr(2,"f", target)
        var s2Op1 = DeleteStr(2,1, target)
        var s3Op1 = InsertStr(3,"f", target)
        //HistElt that contains the operations
        histEltS1Op1 = HistElt("s1", s1Op1, debS1.state_vector)
        histEltS2Op1 = HistElt("s2", s2Op1, debS2.state_vector)
        histEltS3Op1 = HistElt("s3", s3Op1, debS3.state_vector)
    }
    @Test
    fun test_integrate(){
        this.initTelefonTest("title")
        //Integrating operations
        dS1.integrate(histEltS1Op1, debS1.path_to_root)
        dS1.integrate(histEltS1Op2, debS2.path_to_root)
        dS1.integrate(histEltS2Op1, debS1.path_to_root)
        assert(debS1.title == "telephone")

        this.initTelefonTest("description")
        //Integrating operations
        dS1.integrate(histEltS1Op1, debS1.path_to_root)
        dS1.integrate(histEltS1Op2, debS2.path_to_root)
        dS1.integrate(histEltS2Op1, debS1.path_to_root)
        assert(debS1.title == "telephone")

        this.initTelefonTest("title")
        //Integrating operations
        dS2.integrate(histEltS2Op1, debS2.path_to_root)
        dS2.integrate(histEltS1Op1, debS1.path_to_root)
        debS1.state_vector["s1"] = 1
        dS2.integrate(histEltS1Op2, debS1.path_to_root)
        assert(debS2.title == "telephone")

        this.initTelefonTest("description")
        //Integrating operations
        dS2.integrate(histEltS2Op1, debS2.path_to_root)
        dS2.integrate(histEltS1Op1, debS1.path_to_root)
        debS1.state_vector["s1"] = 1
        dS2.integrate(histEltS1Op2, debS1.path_to_root)
        assert(debS2.title == "telephone")

        this.initBuverTest("title")
        dS1.integrate(histEltS1Op1, debS1.path_to_root)
        debS1.state_vector["s1"] = 1
        dS1.integrate(histEltS2Op1, debS2.path_to_root)
        debS1.state_vector["s2"] = 1
        dS1.integrate(histEltS3Op1, debS3.path_to_root)
        debS1.state_vector["s3"] = 1
        assert(debS1.title == "buffer")

        this.initBuverTest("title")
        dS2.integrate(histEltS2Op1, debS2.path_to_root)
        debS2.state_vector["s2"] = 1
        dS2.integrate(histEltS1Op1, debS1.path_to_root)
        debS2.state_vector["s1"] = 1
        dS2.integrate(histEltS3Op1, debS3.path_to_root)
        debS2.state_vector["s3"] = 1
        assert(debS2.title == "buffer")

        this.initBuverTest("title")
        dS3.integrate(histEltS3Op1, debS3.path_to_root)
        debS3.state_vector["s3"] = 1
        dS3.integrate(histEltS1Op1, debS1.path_to_root)
        debS3.state_vector["s1"] = 1
        dS3.integrate(histEltS2Op1, debS2.path_to_root)
        debS3.state_vector["s2"] = 1
        assert(debS3.title == "buffer")
    }
    @Test
    fun test_manageOthersUpdate(){
        this.initTelefonTest("title")
        var updatePayload = UpdatePayload(histEltS1Op1, debS1.path_to_root)
        dS1.manageOthersUpdate(updatePayload, "s1")
        updatePayload = UpdatePayload(histEltS1Op2, debS1.path_to_root)
        dS1.manageOthersUpdate(updatePayload, "s1")
        updatePayload = UpdatePayload(histEltS2Op1, debS2.path_to_root)
        dS1.manageOthersUpdate(updatePayload, "s2")
        assert(debS1.title == "telephone")

        this.initTelefonTest("title")
        updatePayload = UpdatePayload(histEltS2Op1, debS2.path_to_root)
        dS2.manageOthersUpdate(updatePayload, "s2")
        updatePayload = UpdatePayload(histEltS1Op1, debS1.path_to_root)
        dS2.manageOthersUpdate(updatePayload, "s1")
        updatePayload = UpdatePayload(histEltS1Op2, debS1.path_to_root)
        dS2.manageOthersUpdate(updatePayload, "s1")
        assert(debS2.title == "telephone")
    }
}
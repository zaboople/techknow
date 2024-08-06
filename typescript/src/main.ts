// Yes, even though they are .ts files, we have to
// declare our imports as using .js files:
import {test as testMod} from "./mymodule-test.js";
import {test as testObjects} from "./myobjects.js";
import {test as testExc} from "./myexclamate.js";
import {test as testIfc} from "./myinterface.js";
import {test as testFunc} from "./myfunctions.js";
import {test as testAsConst} from "./myas-const.js";
import {test as testAssert} from "./myassert.js";
import {test as testNever} from "./mynever.js";
import {test as testConstr} from "./myconstructor.js";
import {test as testUnknown} from "./myunknown.js";
import {test as testVarargs} from "./myvarargs.js";
import {test as testTuple} from "./mytuple.js";
import {test as testKeyOf} from "./mykeyof.js";
import {test as testClass} from "./myclass.js";
import {test as testMixin} from "./mymixin.js";
import {test as testEnum} from "./myenum.js";
import {test as testQuestionMark} from "./myquestion-mark.js";

//testMod();
//testExc();
//testIfc();
//testAssert();
//testNever();
//testConstr();
//testUnknown();
//testVarargs();
//testAsConst();
//testFunc();
//testObjects();
//testTuple();
//testKeyOf();
//testClass();
//testMixin();
testEnum();
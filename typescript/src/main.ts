// Yes, even though they are .ts files, we have to
// declare our imports as using .js files:
import {test as testMod} from "./mymodule-test.js";
import {test as testObjects} from "./myobjects.js";
import {test as testExc} from "./exclamate.js";
import {test as testIf} from "./myinterface.js";
import {test as testFunc} from "./myfunctions.js";
import {test as testAsConst} from "./asconst.js";

//testMod();
//testExc();
//testObjects();
//testIf();
//testFunc();
testAsConst();
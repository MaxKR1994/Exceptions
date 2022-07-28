package ExeptionsPart2.OVERRIDING_THROWS;

import java.io.FileNotFoundException;
import java.io.IOException;

public class OverridingThrows {

    static class Option1 {
        //При переопределении (overriding) список исключений потомка не обязан совпадать с таковым у предка.
        //Но он должен быть «не сильнее» списка предка:
        // предок пугает IOException и InterruptedException
        public void f() throws IOException, InterruptedException {}
    }

    class Child extends Option1 {
        // а потомок пугает только потомком IOException
        @Override
        public void f() throws FileNotFoundException {}
    }
    static class Option2 {
        //Однако тут мы попытались «расширить тип» бросаемых исключений
        public void f() throws IOException, InterruptedException {}
    }
    class ChildB extends Option2 {
        @Override
        public void f() throws Exception {}
        //>> COMPILATION ERROR: overridden method does not throw 'java.lang.Exception'
    }
}

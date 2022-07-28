package ExeptionsPart2.THROW_THROWS;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ThrowThrows {

    static class Option1 {
        //Мы не можем бросать, но не предупредить
        public static void main(String[] args) {
            throw new Exception(); // тут ошибка компиляции
        }
        //>> COMPILATION ERROR: unhandled exception: java.lang.Exception
    }

    static class Option2 {
        //Мы не можем бросать, но предупредить о «меньшем»
        public static void main(String[] args) throws IOException {
            throw new Exception(); // тут ошибка компиляции
        }
        //>> COMPILATION ERROR: unhandled exception: java.lang.Exception
    }

    static class Option3 {
        //Мы можем предупредить точно о том, что бросаем
        public static void main(String[] args) throws Exception { // предупреждаем о Exception
            throw new Exception(); // и кидаем Exception
        }
    }

    static class Option4 {
        //Мы можем предупредить о большем, чем мы бросаем
        public static void main(String[] args) throws Throwable { // предупреждаем "целом" Throwable
            throw new Exception(); // а кидаем только Exception
        }
    }

    static class Option5 {
        //Можем даже предупредить о том, чего вообще нет
        public static void main(String[] args) throws Exception {
            // пугаем, но ничего не бросаем
        }
    }

    static class Option6 {
        //Даже если предупреждаем о том, чего нет — все обязаны бояться
        public static void main(String[] args) {
            f(); // тут ошибка компиляции
        }
        public static void f() throws Exception {
        }
        //>> COMPILATION ERROR: unhandled exception: java.lang.Exception
    }

    static class Option7 {
        //Хотя они (испугавшиеся) могут перепугать остальных еще больше
        // они пугают целым Throwable
        public static void main(String[] args) throws Throwable {
            f();
        }
        // хотя мы пугали всего-лишь Exception
        public static void f() throws Exception {
        }
    }

    static class Option8 {
        //throws с непроверяемым (unchecked) исключением
        //Вызов метода, который «пугает» unchecked исключением не накладывает на нас никаких обязанностей.
        public static void main(String[] args) {
            f();
        }
        public static void f() throws RuntimeException {
        }
        //Эта конструкция служит цели «указать» программисту-читателю кода на то,
        //что ваш метод может выбросить некоторое непроверяемое (unchecked) исключение.
    }

    static class Option9 {
        //Пример (java.lang.NumberFormatException — непроверяемое исключение):
        //...
        /**
         * ...
         *
         * @param s    a {@code String} containing the {@code int}
         *             representation to be parsed
         * @return     the integer value represented by the argument in decimal.
         * @exception  NumberFormatException  if the string does not contain a
         *               parsable integer.
         */
        public static int parseInt(String s) throws NumberFormatException {
            return parseInt(s,10);
        }
        //Integer.parseInt() может бросить unchecked NumberFormatException
        //на неподходящем аргументе (int k = Integer.parseInt(«123abc»)), это отразили
        //— в сигнатуре метода: throws NumberFormatException
        //— в документации (javadoc): @ exception
        //но это ни к чему нас не обязывает.
    }

    /**Множественные исключения*/

    static class Option10 {
        //Мы можем точно указать, что выбрасываем
        //Пугаем ОБОИМИ исключениями
        public static void main(String[] args) throws EOFException, FileNotFoundException {
            if (System.currentTimeMillis() % 2 == 0) {
                throw new EOFException();
            } else {
                throw new FileNotFoundException();
            }
        }
    }

    static class Option11 {
        //пугаем ОБОИМИ исключениями
        public static void main(String[] args) throws EOFException, FileNotFoundException {
            f0();
            f1();
        }

        public static void f0() throws EOFException {//...}
            public static void f1 () throws FileNotFoundException {//...}
            }
        }
    }

    static class Option12 {
        //можем «испугать» сильнее (предком обоих исключений)
        //пугаем ПРЕДКОМ исключений
        public static void main(String[] args) throws IOException {
            if (System.currentTimeMillis() % 2 == 0) {
                throw new EOFException();
            } else {
                throw new FileNotFoundException();
            }
        }
    }

    static class Option13 {

        public static void main(String[] args) throws IOException {
            f0();
            f1();
        }
        public static void f0() throws EOFException {...}
        public static void f1() throws FileNotFoundException {...}
    }

    static class Option14 {
        //Можно и вот так: EOFException и FileNotFoundException «обобщаем до» IOException,
        //а InterruptedException «пропускаем нетронутым» (InterruptedException — НЕ потомок IOException)
        public static void main(String[] args) throws IOException, InterruptedException {
            f0();
            f1();
            f2();
        }
        public static void f0() throws EOFException {...}
        public static void f1() throws FileNotFoundException {...}
        public static void f2() throws InterruptedException {...}
    }

    static class Option15 {
        //Не надо пугать тем, что вы перехватили
        //так
        public static void main(String[] args) {
            try {
                throw new Exception();
            } catch (Exception e) {
                // ...
            }
        }
    }

    static class Option16 {
        //или так (ставим catch по предку и точно перехватываем)
        public static void main(String[] args) {
            try {
                throw new Exception();
            } catch (Throwable e) {
                // ...
            }
        }
    }

    static class Option17 {
        //Но если перехватили только потомка, то не выйдет
        public static void main(String[] args) {
            try {
                throw new Throwable();
            } catch (Exception e) {
                // ...
            }
        }
        //>> COMPILATION ERROR: unhandled exception: java.lang.Throwable
    }

    static class Option18 {
        //Не годится, естественно, и перехватывание «брата»
        public static void main(String[] args) {
            try {
                throw new Exception();
            } catch (Error e) {
                // ...
            }
        }
        //>> COMPILATION ERROR: unhandled exception: java.lang.Exception
    }

    static class Option19 {
        //Если вы часть перехватили, то можете этим не пугать
        public static void main(String[] args) throws FileNotFoundException {
            try {
                if (System.currentTimeMillis() % 2 == 0) {
                    throw new EOFException();
                } else {
                    throw new FileNotFoundException();
                }
            } catch (EOFException e) {
                // ...
            }
        }
    }

    /**Поведение компилятора/JVM*/

    static class Option20 {
        //Необходимо понимать, что
        //— проверка на checked исключения происходит в момент компиляции (compile-time checking)
        //— перехват исключений (catch) происходит в момент выполнения (runtime checking)
        // пугаем Exception
        public static void main(String[] args) throws Exception {
            Throwable t = new Exception(); // и лететь будет Exception
            throw t; // но тут ошибка компиляции
        }
        //>> COMPILATION ERROR: unhandled exception: java.lang.Throwable
    }

    static class Option21 {
        //Полная аналогия с
        public static void main(String[] args) throws Exception {
            Object ref = "Hello!";  // ref указывает на строку
            char c = ref.charAt(0); // но тут ошибка компиляции
        }
        //>> COMPILATION ERROR: Cannot resolve method 'charAt(int)'
    }

    static class Option22 {
        //НЕ КОМПИЛИРУЕТСЯ! ИССЛЕДУЕМ ГИПОТЕТИЧЕСКУЮ СИТУАЦИЮ!
        public static void f0(Throwable t) throws Exception {
            throw t;
        }
        public static void f1(Object ref){
            char c = ref.charAt(0);
        }
    }

    static class Option23 {
        //Компилятор не пропустит этот код, хотя метод main ГАРАНТИРОВАННО НЕ ВЫБРОСИТ ИСКЛЮЧЕНИЯ
        // пугаем Exception
        public static void main(String[] args) throws Exception {
            try {
                Throwable t = new Exception(); // и лететь будет Exception
                throw t; // но тут ошибка компиляции
            } catch (Exception e) {
                System.out.println("Перехвачено!");
            }
        }
        //>> COMPILATION ERROR: unhandled exception: java.lang.Throwable
    }

    static class Option24 {
        //ТЕПЕРЬ пугаем Throwable
        public static void main(String[] args) throws Throwable {
            try {
                Throwable t = new Exception(); // а лететь будет Exception
                throw t;
            } catch (Exception e) { // и мы перехватим Exception
                System.out.println("Перехвачено!");
            }
        }
        //>> Перехвачено!
    }

}

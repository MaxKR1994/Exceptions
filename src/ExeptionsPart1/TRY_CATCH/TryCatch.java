package ExeptionsPart1.TRY_CATCH;

public class TryCatch {

    static class Option1 {
        // catch, throw, throws можно использовать исключительно с java.lang.Throwable или его потомками.
        public static void main(String[] args) throws Throwable {}
    }

    static class Option2 {
        public static void main(String[] args) {
            try {
            } catch (Throwable t) {}
        }
    }

    static class Option3 {
        public static void main(String[] args) {
            // Error - потомок Throwable
            throw new Error();
        }
    }

    static class Option4 {
        //throw и new — это две независимых операции.
        // В следующем коде мы независимо создаем объект исключения и «бросаем» его
        public static void main(String[] args) {
            Error ref = new Error(); // создаем экземпляр
            throw ref;               // "бросаем" его
        }
        // RUNTIME ERROR: Exception in thread "main" java.lang.Error
    }

    static class Option5 {
        public static void main(String[] args) {
            f(null);
        }
        public static void f(NullPointerException e) {
            try {
                throw e;
            } catch (NullPointerException npe) {
                f(npe);
            }
        }
        //RUNTIME ERROR: Exception in thread "main" java.lang.StackOverflowError
    }

    //Почему используем System.err, а не System.out
    static class Option6 {
        public static void main(String[] args) {
            System.out.println("java");
            throw new Error();
        }
        //System.out — buffered-поток вывода, а System.err — нет.
        // Таким образом вывод может быть как таким:
        //Вариант 1
        //>> RUNTIME ERROR: Exception in thread "main" java.lang.Error
        //>> java
        //Вариант 2
        //>> java
        //>> RUNTIME ERROR: Exception in thread "main" java.lang.Error
    }

    static class Option7 {
        //Компилятор отслеживает, что бы мы что-то вернули,
        // так как иначе непонятно, что должна была бы напечатать данная программа
        public static void main(String[] args) {
            double d = sqr(10.0); // ну, и чему равно d?
            System.out.println(d);
        }
        public static double sqr(double arg) {
            // nothing
        }
        //COMPILATION ERROR: Missing return statement
    }

    static class Option8 {
        //Из-забавного, можно ничего не возвращать, а «повесить метод»
        public static double sqr(double arg) {
            while (true); // Удивительно, но КОМПИЛИРУЕТСЯ!
        }
    }

    static class Option9 {
        //Тут в d никогда ничего не будет присвоено, так как метод sqr повисает
        public static void main(String[] args) {
            double d = sqr(10.0);  // sqr - навсегда "повиснет", и
            System.out.println(d); // d - НИКОГДА НИЧЕГО НЕ БУДЕТ ПРИСВОЕНО!
        }
        public static double sqr(double arg) {
            while (true); // Вот тут мы на века "повисли"
        }
    }

    static class Option10 {
        //Компилятор пропустит «вилку» (таки берем в квадрат ИЛИ висим)
        public static double sqr(double arg) {
            if (System.currentTimeMillis() % 2 == 0) {
                return arg * arg; // ну ладно, вот твой double
            } else {
                while (true);     // а тут "виснем" навсегда
            }
        }
    }

    static class Option11 {
        //Компилятор пропустит «вилку» (таки берем в квадрат ИЛИ висим)
        public static double sqr(double arg) {
            if (System.currentTimeMillis() % 2 == 0) {
                return arg * arg; // ну ладно, вот твой double
            } else {
                while (true);     // а тут "виснем" навсегда
            }
        }
    }

    static class Option12 {
        //Но механизм исключений позволяет НИЧЕГО НЕ ВОЗВРАЩАТЬ!
        public static double sqr(double arg) {
            throw new RuntimeException();
        }
    }

    static class Option13 {
        //Но КАКОЙ ЖЕ double вернет функция, бросающая RuntimeException?
        //А НИКАКОЙ!
        public static void main(String[] args) {
            // sqr - "сломается" (из него "выскочит" исключение),
            double d = sqr(10.0);  // выполнение метода main() прервется в этой строчке и
            // d - НИКОГДА НИЧЕГО НЕ БУДЕТ ПРИСВОЕНО!
            System.out.println(d); // и печатать нам ничего не придется!
        }
        public static double sqr(double arg) {
            throw new RuntimeException(); // "бросаем" исключение
        }
        //RUNTIME ERROR: Exception in thread "main" java.lang.RuntimeException
    }

    //Нелокальная передача управления (non local control transfer)

    static class Option14 {
        //return — выходим из ОДНОГО фрейма (из фрейма #4(метод h()))
        public static void main(String[] args) {
            System.err.println("#1.in");
            f(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println("#1.out"); // вернулись
        } // выходим из текущего фрейма, кончились инструкции

        public static void f() {
            System.err.println(".   #2.in");
            g(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println(".   #2.out");  //вернулись
        } // выходим из текущего фрейма, кончились инструкции

        public static void g() {
            System.err.println(".   .   #3.in");
            h(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println(".   .   #3.out"); // вернулись
        } // выходим из текущего фрейма, кончились инструкции

        public static void h() {
            System.err.println(".   .   .   #4.in");
            if (true) {
                System.err.println(".   .   .   #4.RETURN");
                return; // выходим из текущего фрейма по 'return'
            }
            System.err.println(".   .   .   #4.out"); // ПРОПУСКАЕМ
        }
        /*
          >> #1.in
          >> .   #2.in
          >> .   .   #3.in
          >> .   .   .   #4.in
          >> .   .   .   #4.RETURN
          >> .   .   #3.out
          >> .   #2.out
          >> #1.out
        */
    }

    static class Option15 {
        //throw — выходим из ВСЕХ фреймов
        public static void main(String[] args) {
            System.err.println("#1.in");
            f(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println("#1.out"); // ПРОПУСТИЛИ!
        }

        public static void f() {
            System.err.println(".   #2.in");
            g(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println(".   #2.out"); // ПРОПУСТИЛИ!
        }

        public static void g() {
            System.err.println(".   .   #3.in");
            h(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println(".   .   #3.out"); // ПРОПУСТИЛИ!
        }

        public static void h() {
            System.err.println(".   .   .   #4.in");
            if (true) {
                System.err.println(".   .   .   #4.THROW");
                throw new Error(); // выходим со всей пачки фреймов ("раскрутка стека") по 'throw'
            }
            System.err.println(".   .   .   #4.out"); // ПРОПУСТИЛИ!
        }
        /*
        >> #1.in
        >> .   #2.in
        >> .   .   #3.in
        >> .   .   .   #4.in
        >> .   .   .   #4.THROW
        >> RUNTIME ERROR: Exception in thread "main" java.lang.Error
         */
    }

    static class Option16 {
        //Останавливаем через 3 фрейма, пролетаем фрейм #4(метод h()) + пролетаем фрейм #3(метод g()) + фрейм #2(метод f())
        public static void main(String[] args) {
            System.err.println("#1.in");
            try {
                f(); // создаем фрейм, помещаем в стек, передаем в него управление
            } catch (Error e) { // "перехватили" "летящее" исключение
                System.err.println("#1.CATCH");  // и работаем
            }
            System.err.println("#1.out");  // работаем дальше
        }

        public static void f() {
            System.err.println(".   #2.in");
            g(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println(".   #2.out"); // ПРОПУСТИЛИ!
        }

        public static void g() {
            System.err.println(".   .   #3.in");
            h(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println(".   .   #3.out"); // ПРОПУСТИЛИ!
        }

        public static void h() {
            System.err.println(".   .   .   #4.in");
            if (true) {
                System.err.println(".   .   .   #4.THROW");
                throw new Error(); // выходим со всей пачки фреймов ("раскрутка стека") по 'throw'
            }
            System.err.println(".   .   .   #4.out"); // ПРОПУСТИЛИ!
        }
        /*
        >> #1.in
        >> .   #2.in
        >> .   .   #3.in
        >> .   .   .   #4.in
        >> .   .   .   #4.THROW
        >> #1.CATCH
        >> #1.out
         */
    }

    static class Option17 {
        //Останавливаем через 2 фрейма, пролетаем фрейм #4(метод h()) + пролетаем фрейм #3(метод g())
        public static void main(String[] args) {
            System.err.println("#1.in");
            f(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println("#1.out"); // вернулись и работаем
        }

        public static void f() {
            System.err.println(".   #2.in");
            try {
                g(); // создаем фрейм, помещаем в стек, передаем в него управление
            } catch (Error e) { // "перехватили" "летящее" исключение
                System.err.println(".   #2.CATCH");  // и работаем
            }
            System.err.println(".   #2.out");  // работаем дальше
        }

        public static void g() {
            System.err.println(".   .   #3.in");
            h(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println(".   .   #3.out"); // ПРОПУСТИЛИ!
        }

        public static void h() {
            System.err.println(".   .   .   #4.in");
            if (true) {
                System.err.println(".   .   .   #4.THROW");
                throw new Error(); // выходим со всей пачки фреймов ("раскрутка стека") по 'throw'
            }
            System.err.println(".   .   .   #4.out"); // ПРОПУСТИЛИ!
        }
        /*
        >> #1.in
        >> .   #2.in
        >> .   .   #3.in
        >> .   .   .   #4.in
        >> .   .   .   #4.THROW
        >> .   #2.CATCH
        >> .   #2.out
        >> #1.out
         */
    }

    static class Option18 {
        //Останавливаем через 1 фрейм (фактически аналог return, просто покинули фрейм «другим образом»)
        public static void main(String[] args) {
            System.err.println("#1.in");
            f(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println("#1.out"); // вернулись и работаем
        }

        public static void f() {
            System.err.println(".   #2.in");
            g(); // создаем фрейм, помещаем в стек, передаем в него управление
            System.err.println(".   #2.out"); // вернулись и работаем
        }

        public static void g() {
            System.err.println(".   .   #3.in");
            try {
                h(); // создаем фрейм, помещаем в стек, передаем в него управление
            } catch (Error e) { // "перехватили" "летящее" исключение
                System.err.println(".   .   #3.CATCH");  // и работаем
            }
            System.err.println(".   .   #3.out");  // работаем дальше
        }

        public static void h() {
            System.err.println(".   .   .   #4.in");
            if (true) {
                System.err.println(".   .   .   #4.THROW");
                throw new Error(); // выходим со всей пачки фреймов ("раскрутка стека") по 'throw'
            }
            System.err.println(".   .   .   #4.out"); // ПРОПУСТИЛИ!
        }
        /*
        >> #1.in
        >> .   #2.in
        >> .   .   #3.in
        >> .   .   .   #4.in
        >> .   .   .   #4.THROW
        >> .   .   #3.CATCH
        >> .   .   #3.out
        >> .   #2.out
        >> #1.out
         */
    }

    //try + catch (catch — полиморфен)
    static class Option19 {
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                if (true) {throw new RuntimeException();}
                System.err.print(" 1");
            } catch (Exception e) { // catch по Exception ПЕРЕХВАТЫВАЕТ RuntimeException
                System.err.print(" 2");
            }
            System.err.println(" 3");
        }
        //>> 0 2 3
    }

    static class Option20 {
        //Даже так: в блоке catch мы будем иметь ссылку типа Exception на объект типа RuntimeException
        public static void main(String[] args) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    RuntimeException re = (RuntimeException) e;
                    System.err.print("Это RuntimeException на самом деле!!!");
                } else {
                    System.err.print("В каком смысле не RuntimeException???");
                }
            }
        }
        // >> Это RuntimeException на самом деле!!!
    }

    static class Option21 {
        //catch по потомку не может поймать предка
        public static void main(String[] args) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    RuntimeException re = (RuntimeException) e;
                    System.err.print("Это RuntimeException на самом деле!!!");
                } else {
                    System.err.print("В каком смысле не RuntimeException???");
                }
            }
        }
        // >> 0
        //>> RUNTIME EXCEPTION: Exception in thread "main" java.lang.Exception
    }

    static class Option22 {
        //catch по одному «брату» не может поймать другого «брата»
        // (Error и Exception не находятся в отношении предок-потомок, они из параллельных веток наследования от Throwable)
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                if (true) {throw new Error();}
                System.err.print(" 1");
            } catch (Exception e) {
                System.err.print(" 2");
            }
            System.err.print(" 3");
        }
        // >> 0
        // >> RUNTIME EXCEPTION: Exception in thread "main" java.lang.Error
    }

    static class Option23 {
        //А что будет, если мы зашли в catch, и потом бросили исключение ИЗ catch?
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                if (true) {throw new RuntimeException();}
                System.err.print(" 1");
            } catch (RuntimeException e) {     // перехватили RuntimeException
                System.err.print(" 2");
                if (true) {throw new Error();} // но бросили Error
            }
            System.err.println(" 3");          // пропускаем - уже летит Error
        }
        // >> 0 2
        // >> RUNTIME EXCEPTION: Exception in thread "main" java.lang.Error
    }

    static class Option24 {
        //Мы можем даже кинуть тот объект, что у нас есть «на руках»
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                if (true) {throw new RuntimeException();}
                System.err.print(" 1");
            } catch (RuntimeException e) { // перехватили RuntimeException
                System.err.print(" 2");
                if (true) {throw e;}       // и бросили ВТОРОЙ раз ЕГО ЖЕ
            }
            System.err.println(" 3");      // пропускаем - опять летит RuntimeException
        }
        // >> 0 2
        // >> RUNTIME EXCEPTION: Exception in thread "main" java.lang.RuntimeException
    }

    static class Option25 {
        //И мы не попадем в другие секции catch, если они есть
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                if (true) {throw new RuntimeException();}
                System.err.print(" 1");
            } catch (RuntimeException e) {     // перехватили RuntimeException
                System.err.print(" 2");
                if (true) {throw new Error();} // и бросили новый Error
            } catch (Error e) { // хотя есть cath по Error "ниже", но мы в него не попадаем
                System.err.print(" 3");
            }
            System.err.println(" 4");
        }
        // >> 0 2
        // >> RUNTIME EXCEPTION: Exception in thread "main" java.lang.Error
    }

    static class Option26 {
        //Как покажем ниже — можно строить вложенные конструкции, но вот пример, «исправляющий» эту ситуацию
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                if (true) {throw new RuntimeException();}
                System.err.print(" 1");
            } catch (RuntimeException e) { // перехватили RuntimeException
                System.err.print(" 2.1");
                try {
                    System.err.print(" 2.2");
                    if (true) {throw new Error();} // и бросили новый Error
                    System.err.print(" 2.3");
                } catch (Throwable t) {            // перехватили Error
                    System.err.print(" 2.4");
                }
                System.err.print(" 2.5");
            } catch (Error e) { // хотя есть cath по Error "ниже", но мы в него не попадаем
                System.err.print(" 3");
            }
            System.err.println(" 4");
        }
        // >> 0 2.1 2.2 2.4 2.5 4
    }

    //try + catch + catch + ...

    static class Option27 {
        //есть такое правило — нельзя ставить потомка после предка! (RuntimeException после Exception)
        public static void main(String[] args) {
            try {
            } catch (Exception e) {
            } catch (RuntimeException e) {
            }
        }
        // >> COMPILATION ERROR: Exception 'java.lang.RuntimeException' has already been caught
    }

    static class Option28 {
        //Ставить брата после брата — можно (RuntimeException после Error)
        public static void main(String[] args) {
            try {
            } catch (Error e) {
            } catch (RuntimeException e) {
            }
        }
    }

    static class Option29 {
        //JVM идет сверху-вниз до тех пор, пока не найдет такой catch что в нем указано ваше исключение
        // или его предок — туда и заходит. Ниже — не идет.
        public static void main(String[] args) {
            try {
                throw new Exception();
            } catch (RuntimeException e) {
                System.err.println("catch RuntimeException");
            } catch (Exception e) {
                System.err.println("catch Exception");
            } catch (Throwable e) {
                System.err.println("catch Throwable");
            }
            System.err.println("next statement");
        }
        /*
        >> catch Exception
        >> next statement
         */
    }

    static class Option30 {
        //Выбор catch осуществляется в runtime (а не в compile-time),
        // значит учитывается не тип ССЫЛКИ (Throwable), а тип ССЫЛАЕМОГО (Exception)
        public static void main(String[] args) {
            try {
                Throwable t = new Exception(); // ссылка типа Throwable указывает на объект типа Exception
                throw t;
            } catch (RuntimeException e) {
                System.err.println("catch RuntimeException");
            } catch (Exception e) {
                System.err.println("catch Exception");
            } catch (Throwable e) {
                System.err.println("catch Throwable");
            }
            System.err.println("next statement");
        }
        /*
        >> catch Exception
        >> next statement
         */
    }

}

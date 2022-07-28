package ExeptionsPart1.TRY_CATCH_FINALLY;

public class TryCatchFinally {

    static class Option1{
        //Нет исключения
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                // nothing
                System.err.print(" 1");
            } catch(Error e) {
                System.err.print(" 2");
            } finally {
                System.err.print(" 3");
            }
            System.err.print(" 4");
        }
        //>> 0 1 3 4
        //Не заходим в catch, заходим в finally, продолжаем после оператора
    }

    static class Option2{
        //Есть исключение и есть подходящий catch
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                if (true) {throw new Error();}
                System.err.print(" 1");
            } catch(Error e) {
                System.err.print(" 2");
            } finally {
                System.err.print(" 3");
            }
            System.err.print(" 4");
        }
        //>> 0 2 3 4
        //Заходим в catch, заходим в finally, продолжаем после оператора
    }

    static class Option3 {
        //Есть исключение но нет подходящего catch
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                if (true) {throw new RuntimeException();}
                System.err.print(" 1");
            } catch(Error e) {
                System.err.print(" 2");
            } finally {
                System.err.print(" 3");
            }
            System.err.print(" 4");
        }
        //>> 0 3
        //>> RUNTIME ERROR: Exception in thread "main" java.lang.RuntimeException
        //Не заходим в catch, заходим в finally, не продолжаем после оператора — вылетаем с не перехваченным исключением
    }

    static class Option4 {
        //Операторы обычно допускают неограниченное вложение.
        //Пример с if
        public static void main(String[] args) {
            if (args.length > 1) {
                if (args.length > 2) {
                    if (args.length > 3) {
                    //...
                    }
                }
            }
        }
    }

    static class Option5 {
        //Пример с for
        public static void main(String[] args) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; i++) {
                    for (int k = 0; k < 10; k++) {
                    //...
                    }
                }
            }
        }
    }

    static class Option6 {
        //Суть в том, что try-cacth-finally тоже допускает неограниченное вложение.
        //Например, вот так
        public static void main(String[] args) {
            try {
                try {
                    try {
                    //...
                    } catch (Exception e) {
                    } finally {}
                } catch (Exception e) {
                } finally {}
            } catch (Exception e) {
            } finally {}
        }
    }

    static class Option7 {
        public static void main(String[] args) {
            try {
                try {
                //...
                } catch (Exception e) {
                //...
                } finally {
                //...
                }
            } catch (Exception e) {
                try {
                //...
                } catch (Exception e) {
                //...
                } finally {
                //...
                }
            } finally {
                try {
                //...
                } catch (Exception e) {
                //...
                } finally {
                //...
                }
            }
        }
    }

    static class Option8 {
        //Вложенный try-catch-finally без исключения
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                try {
                    System.err.print(" 1");
                    // НИЧЕГО
                    System.err.print(" 2");
                } catch (RuntimeException e) {
                    System.err.print(" 3"); // НЕ заходим - нет исключения
                } finally {
                    System.err.print(" 4"); // заходим всегда
                }
                System.err.print(" 5");     // заходим - выполнение в норме
            } catch (Exception e) {
                System.err.print(" 6");     // НЕ заходим - нет исключения
            } finally {
                System.err.print(" 7");     // заходим всегда
            }
            System.err.print(" 8");         // заходим - выполнение в норме
        }
        //>> 0 1 2 4 5 7 8
        //Мы НЕ заходим в обе catch-секции (нет исключения),
        //заходим в обе finally-секции и выполняем обе строки ПОСЛЕ finally.
    }

    static class Option9 {
        //Вложенный try-catch-finally с исключением, которое ПЕРЕХВАТИТ ВНУТРЕННИЙ catch
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                try {
                    System.err.print(" 1");
                    if (true) {throw new RuntimeException();}
                    System.err.print(" 2");
                } catch (RuntimeException e) {
                    System.err.print(" 3"); // ЗАХОДИМ - есть исключение
                } finally {
                    System.err.print(" 4"); // заходим всегда
                }
                System.err.print(" 5");     // заходим - выполнение УЖЕ в норме
            } catch (Exception e) {
                System.err.print(" 6");     // не заходим - нет исключения, УЖЕ перехвачено
            } finally {
                System.err.print(" 7");     // заходим всегда
            }
            System.err.print(" 8");         // заходим - выполнение УЖЕ в норме
        }
        //>> 0 1 3 4 5 7 8
        //Мы заходим в ПЕРВУЮ catch-секцию (печатаем «3»),
        //но НЕ заходим во ВТОРУЮ catch-секцию (НЕ печатаем «6», так как исключение УЖЕ перехвачено первым catch),
        // заходим в обе finally-секции (печатаем «4» и «7»), в обоих случаях выполняем код после finally
        // (печатаем «5»и «8», так как исключение остановлено еще первым catch).
    }

    static class Option10 {
        //Вложенный try-catch-finally с исключением, которое ПЕРЕХВАТИТ ВНЕШНИЙ catch
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                try {
                    System.err.print(" 1");
                    if (true) {throw new Exception();}
                    System.err.print(" 2");
                } catch (RuntimeException e) {
                    System.err.print(" 3"); // НЕ заходим - есть исключение, но НЕПОДХОДЯЩЕГО ТИПА
                } finally {
                    System.err.print(" 4"); // заходим всегда
                }
                System.err.print(" 5");     // не заходим - выполнение НЕ в норме
            } catch (Exception e) {
                System.err.print(" 6");     // ЗАХОДИМ - есть подходящее исключение
            } finally {
                System.err.print(" 7");     // заходим всегда
            }
            System.err.print(" 8");         // заходим - выполнение УЖЕ в норме
        }
        //>> 0 1 4 6 7 8
        //Мы НЕ заходим в ПЕРВУЮ catch-секцию (не печатаем «3»), но заходим в ВТОРУЮ catch-секцию (печатаем «6»),
        //заходим в обе finally-секции (печатаем «4» и «7»),
        //в ПЕРВОМ случае НЕ выполняем код ПОСЛЕ finally (не печатаем «5», так как исключение НЕ остановлено),
        //во ВТОРОМ случае выполняем код после finally (печатаем «8», так как исключение остановлено).
    }

    static class Option11 {
        //Вложенный try-catch-finally с исключением, которое НИКТО НЕ ПЕРЕХВАТИТ
        public static void main(String[] args) {
            try {
                System.err.print(" 0");
                try {
                    System.err.print(" 1");
                    if (true) {throw new Error();}
                    System.err.print(" 2");
                } catch (RuntimeException e) {
                    System.err.print(" 3"); // НЕ заходим - есть исключение, но НЕПОДХОДЯЩЕГО ТИПА
                } finally {
                    System.err.print(" 4"); // заходим всегда
                }
                System.err.print(" 5");     // НЕ заходим - выполнение НЕ в норме
            } catch (Exception e) {
                System.err.print(" 6");     // не заходим - есть исключение, но НЕПОДХОДЯЩЕГО ТИПА
            } finally {
                System.err.print(" 7");     // заходим всегда
            }
            System.err.print(" 8");         // не заходим - выполнение НЕ в норме
        }
        //>> 0 1 4 7
        //>> RUNTIME EXCEPTION: Exception in thread "main" java.lang.Error
        //Мы НЕ заходим в ОБЕ catch-секции (не печатаем «3» и «6»),
        //заходим в обе finally-секции (печатаем «4» и «7») и в обоих случаях НЕ выполняем код ПОСЛЕ finally
        //(не печатаем «5» и «8», так как исключение НЕ остановлено), выполнение метода прерывается по исключению.
    }

}

package ExeptionsPart1.TRY_FINALLY;

public class TryFinally {

    static class Option1 {
        //finally-секция получает управление, если try-блок завершился успешно
        public static void main(String[] args) {
            try {
                System.err.println("try");
            } finally {
                System.err.println("finally");
            }
        }
        //>> try
        //>> finally
    }

    static class Option2 {
        //finally-секция получает управление, даже если try-блок завершился исключением
        public static void main(String[] args) {
            try {
                throw new RuntimeException();
            } finally {
                System.err.println("finally");
            }
        }
        //>> finally
        //>> Exception in thread "main" java.lang.RuntimeException
    }

    static class Option3 {
        //finally-секция получает управление, даже если try-блок завершился директивой выхода из метода
        public static void main(String[] args) {
            try {
                return;
            } finally {
                System.err.println("finally");
            }
        }
        //>> finally
    }

    static class Option4 {
        //finally-секция НЕ вызывается только если мы «прибили» JVM
        public static void main(String[] args) {
            try {
                System.exit(42);
            } finally {
                System.err.println("finally");
            }
        }
        //>> Process finished with exit code 42
    }

    static class Option5 {
        //System.exit(42) и Runtime.getRuntime().exit(42) — это синонимы
        public static void main(String[] args) {
            try {
                Runtime.getRuntime().exit(42);
            } finally {
                System.err.println("finally");
            }
        }
        //>> Process finished with exit code 42
    }

    static class Option6 {
        //И при Runtime.getRuntime().halt(42) — тоже не успевает зайти в finally
        public static void main(String[] args) {
            try {
                Runtime.getRuntime().halt(42);
            } finally {
                System.err.println("finally");
            }
        }
        //>> Process finished with exit code 42
    }

    static class Option7 {
        //Однако finally-секция не может «починить» try-блок завершившийся исключением
        // (заметьте, «more» — не выводится в консоль)
        public static void main(String[] args) {
            try {
                System.err.println("try");
                if (true) {throw new RuntimeException();}
            } finally {
                System.err.println("finally");
            }
            System.err.println("more");
        }
        //>> try
        //>> finally
        //>> Exception in thread "main" java.lang.RuntimeException
    }

    static class Option8 {
        //И finally-секция не может «предотвратить» выход из метода,
        //если try-блок вызвал return («more» — не выводится в консоль)
        public static void main(String[] args) {
            try {
                System.err.println("try");
                if (true) {return;}
            } finally {
                System.err.println("finally");
            }
            System.err.println("more");
        }
        //>> try
        //>> finally
    }

    static class Option9 {
        //Однако finally-секция может «перебить» return при помощи другого throw/return
        public static void main(String[] args) {
            System.err.println(f());
        }
        public static int f() {
            try {
                return 0;
            } finally {
                return 1;
            }
        }
        //>> 1
    }

    static class Option10 {
        //Однако finally-секция может «перебить» throw при помощи другого throw/return
        public static void main(String[] args) {
            System.err.println(f());
        }
        public static int f() {
            try {
                throw new RuntimeException();
            } finally {
                return 1;
            }
        }
        //>> 1
    }

    static class Option11 {
        public static void main(String[] args) {
            System.err.println(f());
        }
        public static int f() {
            try {
                return 0;
            } finally {
                throw new RuntimeException();
            }
        }
        //>> Exception in thread "main" java.lang.RuntimeException
    }

    static class Option12 {
        public static void main(String[] args) {
            System.err.println(f());
        }
        public static int f() {
            try {
                throw new Error();
            } finally {
                throw new RuntimeException();
            }
        }
        //>> Exception in thread "main" java.lang.RuntimeException
    }

    static class Option13 {
        //Вообще говоря, в finally-секция нельзя стандартно узнать было ли исключение.
        //Конечно, можно постараться написать свой «велосипед»
        public static void main(String[] args) {
            System.err.println(f());
        }
        public static int f() {
            long rnd = System.currentTimeMillis();
            boolean finished = false;
            try {
                if (rnd % 3 == 0) {
                    throw new Error();
                } else if (rnd % 3 == 1) {
                    throw new RuntimeException();
                } else {
                    // nothing
                }
                finished = true;
            } finally {
                if (finished) {
                    // не было исключений
                } else {
                    // было исключение, но какое?
                }
            }
            return (int) rnd;
        }
    }

}

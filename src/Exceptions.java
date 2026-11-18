
public class Exceptions extends Exception {
    public static class InvalidTaskDataException extends Exception {//for alll illogical value that cames from the user
        public InvalidTaskDataException(String message) {
            super(message);
        }
    }
    public static class LineNotFoundException extends Exception{
        public LineNotFoundException(String message){
            super(message);
        }
    }
}
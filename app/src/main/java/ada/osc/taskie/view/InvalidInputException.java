package ada.osc.taskie.view;

public class InvalidInputException extends Exception {

    private String msg= "Invalid input!";

    public InvalidInputException() {
        super();
    }

    public InvalidInputException(String msg) {
        super(msg);
    }
}


 class EmptyFieldException extends InvalidInputException {

    public EmptyFieldException() {
    }

    public EmptyFieldException(String msg) {
        super(msg);
    }
}


class InvalidDateException extends InvalidInputException{

    public InvalidDateException() {
    }

    public InvalidDateException(String msg) {
        super(msg);
    }

}


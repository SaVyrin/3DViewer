package com.vsu.cgcourse.obj_writer;

public class ObjWriterException extends IndexOutOfBoundsException {
    public ObjWriterException(String errorMessage, int lineInd) {
        super("Error writing OBJ file on line: " + lineInd + ". " + errorMessage);
    }
}

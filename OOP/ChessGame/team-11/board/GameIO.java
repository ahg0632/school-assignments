package board;

import java.io.*;

public class GameIO {
    /**
     * Saves the current state of the board to a file.
     *
     * @param board the board to be saved
     * @param file the file to save the board to
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public void saveGame(Board board, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(board);
        }
    }

    /**
     * Loads the state of the board from a file.
     *
     * @param file the file to load the board from
     * @return the loaded board
     * @throws IOException if an I/O error occurs while reading from the file
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    public Board loadGame(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Board) ois.readObject();
        }
    }
}

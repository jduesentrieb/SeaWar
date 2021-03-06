package SeaWar2;

import org.junit.Test;
import seaWar2.Game;
import seaWar2.GameStatus;
import seaWar2.StatusException;
import seaWar2.board.Coordinate;
import seaWar2.board.LocalBoard;
import seaWar2.board.RemoteBoard;
import seaWar2.board.Ship;
import seaWar2.view.BoardViewConsole;

public class BoardViewConsoleImplTest {

    private Game game;

    private void createTestGame() {
        this.game = Game.createGame();
    }

    private LocalBoard getLocalBoard() {
        return this.game.getLocalBoard();
    }

    private RemoteBoard getRemoteBoard() {
        return this.game.getRemoteBoard();
    }

    private BoardViewConsole getBoardView() {
        return this.game.getBoardViewConsole();
    }

    @Test
    public void printBoards_GoodCase01() {
        this.createTestGame();
        LocalBoard localBoard = this.getLocalBoard();
        RemoteBoard remoteBoard = this.getRemoteBoard();
        BoardViewConsole view = this.getBoardView();

        view.printBoards(localBoard, remoteBoard);
    }

    @Test
    public void printPreparationCommands_GoodCase01() throws StatusException {
        this.createTestGame();
        LocalBoard localBoard = this.getLocalBoard();
        RemoteBoard remoteBoard = this.getRemoteBoard();
        BoardViewConsole view = this.getBoardView();

        view.printBoards(localBoard, remoteBoard);
        view.printPreparationCommands();
    }

    @Test
    public void printPlayCommands_ACTIVE() throws StatusException {
        this.createTestGame();
        LocalBoard localBoard = this.getLocalBoard();
        RemoteBoard remoteBoard = this.getRemoteBoard();
        BoardViewConsole view = this.getBoardView();

        this.game.setStatus(GameStatus.ACTIVE);

        view.printBoards(localBoard, remoteBoard);
        view.printPlayCommands();
    }

    @Test
    public void printPlayCommands_PASSIVE() throws StatusException {
        this.createTestGame();
        LocalBoard localBoard = this.getLocalBoard();
        RemoteBoard remoteBoard = this.getRemoteBoard();
        BoardViewConsole view = this.getBoardView();

        this.game.setStatus(GameStatus.PASSIVE);

        view.printBoards(localBoard, remoteBoard);
        view.printPlayCommands();
    }

    @Test
    public void printShipString_Test() throws StatusException {
        this.createTestGame();
        LocalBoard localBoard = this.getLocalBoard();
        BoardViewConsole view = this.getBoardView();

        Ship setShip = Ship.createShip(4);
        setShip.place(new Coordinate[]{Coordinate.createCoordinate(0, 0)});
        String[] shipFleet = view.getShipFleetString(localBoard.getShipFleet());

        for (String ships : shipFleet)
            System.out.print(ships);
    }


}
package seaWar2.view;

import seaWar2.ErrorMessages;
import seaWar2.Game;
import seaWar2.GameStatus;
import seaWar2.StatusException;
import seaWar2.board.*;
import seaWar2.communication.SWPEngine;
import seaWar2.communication.TCPChannel;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class BoardCommandsImpl implements BoardCommands {

    private final Game game;
    private final PrintStream consoleOutput;
    private final BufferedReader consoleInput;
    private final BoardViewConsole view;
    private final RemoteBoard remoteBoard;
    private final LocalBoard localBoard;
    private final SWPEngine swpEngine;
    private boolean determinesStart;

    public BoardCommandsImpl(Game game) {
        this(game, System.in, System.out);
    }

    public BoardCommandsImpl(Game game, InputStream in, OutputStream out) {
        this.game = game;

        this.consoleOutput = new PrintStream(out);
        this.consoleInput = new BufferedReader(new InputStreamReader(in));

        this.view = game.getBoardViewConsole();

        this.remoteBoard = game.getRemoteBoard();
        this.localBoard = game.getLocalBoard();

        this.swpEngine = game.getSWPEngine();
    }

    /*public void runGameX(){
        this.printTasks();
        boolean repeat = true;
        while (repeat) {
            runConnectionMode();
            runPreparationMode();
            runPlayingMode();
        }
    }*/

    @Override
    public void runGame() {
        this.printTasks();
        boolean repeat = true;
        while (repeat) {
            try {
                consoleOutput.print("\n-> ");
                String cmdLineString = consoleInput.readLine();

                if (cmdLineString == null) break;

                cmdLineString = cmdLineString.trim();

                int spaceIndex = cmdLineString.indexOf(' ');
                spaceIndex = spaceIndex != -1 ? spaceIndex : cmdLineString.length();

                String commandString = cmdLineString.substring(0, spaceIndex);

                String paramString = cmdLineString.substring(spaceIndex).trim();
                paramString = paramString.trim();

                switch (commandString) {
                    //only debug support
                    case "wait":
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            //
                        }
                        break;
                    case PRINT_BOARDS:
                        this.doPrintBoards();
                        break;
                    case SHOOT:
                        this.doShot(paramString);
                        break;
                    case SET_SHIP:
                        this.doSetShip(paramString);
                        break;
                    case REMOVE_SHIP:
                        this.doRemoveShip(paramString);
                        break;
                    case START_GAME:
                        this.doStartGame();
                        break;
                    case GIVE_UP:
                        this.doGiveUp();
                        break;
                    case TALK:
                        this.doTalk(paramString);
                        break;
                    case GET_STATUS:
                        this.doGetStatus();
                        break;
                    case SAVE:
                        this.doSave();
                        break;
                    case RESTORE:
                        this.doRestore();
                        break;
                    case CONNECT:
                        this.doConnect();
                        break;
                    case FILL:
                        this.doFill();
                        this.doPrintBoards();
                        break;
                    case "q!":
                    case EXIT:
                        this.swpEngine.sendCloseConnectionCmd();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("* Unknown command: " + cmdLineString + " *");
                        this.printTasks();
                        break;
                }
            } catch (OutOfBoardException oe) {
                consoleOutput.println("* The specified coordinates are out of the board! *");
            } catch (InvalidPositionException ip) {
                consoleOutput.println("* The specified coordinates are not valid! *");
            } catch (ShipNotAvailableException sna) {
                consoleOutput.println("* There is no ship left with the specified length! *");
            } catch (BoardException be) {
                System.out.println("* Failure on board operation: " + be.getMessage() + " *");
            } catch (StatusException se) {
                System.out.println("* Status Error: " + se.getMessage() + " *");
            } catch (NoSuchElementException | NumberFormatException nse) {
                consoleOutput.println("* Parameters are missing or the syntax is wrong! *");
            } catch (IOException e) {
                System.out.println("* Cannot read from console: " + e.getMessage() + " *");
            }
        }
    }

    private void runConnectionMode() {

    }

    private void runPreparationMode() {
        this.printTasks();
        boolean replay = true;
        while (replay) {
            try {
                consoleOutput.print("\n-> ");
                String cmdLineString = consoleInput.readLine();

                if (cmdLineString == null) break;

                cmdLineString = cmdLineString.trim();

                int spaceIndex = cmdLineString.indexOf(' ');
                spaceIndex = spaceIndex != -1 ? spaceIndex : cmdLineString.length();

                String commandString = cmdLineString.substring(0, spaceIndex);

                String paramString = cmdLineString.substring(spaceIndex).trim();
                paramString = paramString.trim();

                switch (commandString) {
                    case PRINT_BOARDS:
                        this.doPrintBoards();
                        break;
                    case SET_SHIP:
                        this.doSetShip(paramString);
                        break;
                    case REMOVE_SHIP:
                        this.doRemoveShip(paramString);
                        break;
                    case "q!":
                    case EXIT:
                        replay = false;
                        break;

                    default:
                        System.out.println("* Unknown command: " + cmdLineString + " *");
                        this.printTasks();
                        break;
                }
            } catch (OutOfBoardException oe) {
                consoleOutput.println("* The specified coordinates are out of the board! *");
            } catch (InvalidPositionException ip) {
                consoleOutput.println("* The specified coordinates are not valid! *");
            } catch (ShipNotAvailableException sna) {
                consoleOutput.println("* There is no ship left with the specified length! *");
            } catch (BoardException be) {
                System.out.println("* Failure on board operation: " + be.getMessage() + " *");
            } catch (StatusException se) {
                System.out.println("* Status Error: " + se.getMessage() + " *");
            } catch (NoSuchElementException | NumberFormatException nse) {
                consoleOutput.println("* Parameters are missing or the syntax is wrong! *");
            } catch (IOException e) {
                System.out.println("* Cannot read from console: " + e.getMessage() + " *");
            }
        }
    }

    private void runPlayingMode() {
        this.printTasks();
        boolean repeat = true;
        while (repeat) {
            try {
                consoleOutput.print("\n-> ");
                String cmdLineString = consoleInput.readLine();

                if (cmdLineString == null) break;

                cmdLineString = cmdLineString.trim();

                int spaceIndex = cmdLineString.indexOf(' ');
                spaceIndex = spaceIndex != -1 ? spaceIndex : cmdLineString.length();

                String commandString = cmdLineString.substring(0, spaceIndex);

                String paramString = cmdLineString.substring(spaceIndex).trim();
                paramString = paramString.trim();

                switch (commandString) {
                    case PRINT_BOARDS:
                        this.doPrintBoards();
                        break;
                    case SHOOT:
                        this.doShot(paramString);
                        break;
                    case GIVE_UP:
                        this.doGiveUp();
                        break;
                    case TALK:
                        this.doTalk(paramString);
                        break;
                    case GET_STATUS:
                        this.doGetStatus();
                        break;
                    case SAVE:
                        this.doSave();
                        break;
                    case RESTORE:
                        this.doRestore();
                        break;
                    case CONNECT:
                        this.doConnect();
                        break;
                    case FILL:
                        this.doFill();
                        this.doPrintBoards();
                        break;
                    case "q!":
                    case EXIT:
                        this.swpEngine.sendCloseConnectionCmd();
                        System.exit(0);
                    default:
                        System.out.println("* Unknown command: " + cmdLineString + " *");
                        this.printTasks();
                        break;
                }
            } catch (OutOfBoardException oe) {
                consoleOutput.println(ErrorMessages.OUT_OF_BOARD_ERR);
            } catch (StatusException se) {
                System.out.println("* Status Error: " + se.getMessage() + " *");
            } catch (NoSuchElementException | NumberFormatException nse) {
                consoleOutput.println(ErrorMessages.WRONG_SYNTAX_ERR);
            } catch (IOException e) {
                System.out.println("* Cannot read from console: " + e.getMessage() + " *");
            }
        }
    }

    private int parseRowCharToInt(char rowChar) throws OutOfBoardException {
        switch (rowChar) {
            case 'A':
                return 0;
            case 'B':
                return 1;
            case 'C':
                return 2;
            case 'D':
                return 3;
            case 'E':
                return 4;
            case 'F':
                return 5;
            case 'G':
                return 6;
            case 'H':
                return 7;
            case 'I':
                return 8;
            case 'J':
                return 9;
            default:
                throw new OutOfBoardException();
        }
    }

    private void printTasks() {
        consoleOutput.print("\n\n" +
                "TASKS\n" +
                "-----\n" +
                PRINT_BOARDS + "      - prints the boards.\n" +
                SET_SHIP + "        - places a ship on your board.         + [ship-length(1..5)][row][column][alignment(h/v)]\n" +
                REMOVE_SHIP + "     - removes ship from your board.        + [row][column]\n" +
                SHOOT + "      - shoots ship on your enemys board.    + [row][column]\n" +
                START_GAME + "      - starts the game.\n" +
                SAVE + "       - saves the game.\n" +
                EXIT + "       - exits the application.\n" +
                "-----\n");
    }

    private void doStartGame() throws IOException, StatusException {
        if (!this.swpEngine.isConnected()) {
            System.out.println(ErrorMessages.NOT_YET_CONNECTED_ERR);
        } else if (localBoard.isReady()) {
            this.game.setStatus(GameStatus.READY);
            this.swpEngine.sendReadyCmd();
        } else {
            consoleOutput.println(ErrorMessages.NOT_YET_READY_ERROR);
        }
    }

    private void doPrintBoards() {
        view.printBoards(localBoard, remoteBoard);
    }

    private void doShot(String paramString)
            throws NumberFormatException, StatusException, OutOfBoardException, IOException {
        StringTokenizer st = new StringTokenizer(paramString, " ", false);

        char rowChar = st.nextToken().toUpperCase().charAt(0);
        int row = this.parseRowCharToInt(rowChar);
        int column = Integer.parseInt(st.nextToken()) - 1;
        remoteBoard.shoot(row, column);
    }

    private void doSetShip(String paramString)
            throws IllegalArgumentException, InvalidPositionException, StatusException, ShipNotAvailableException,
            ShipAlreadySetException {

        StringTokenizer st = new StringTokenizer(paramString, " ", false);

        int shipLength = Integer.parseInt(st.nextToken());
        Ship ship = localBoard.getUnsetShip(shipLength);

        int row = this.parseRowCharToInt(st.nextToken().toUpperCase().charAt(0));
        int column = Integer.parseInt(st.nextToken()) - 1;

        String alignmentString = st.nextToken().toLowerCase();


        boolean horizontal;
        if (alignmentString.equals(Board.HORIZONTAL_STRING)) {
            horizontal = true;
        } else if (alignmentString.equals(Board.VERTICAL_STRING)) {
            horizontal = false;
        } else throw new IllegalArgumentException();

        localBoard.setShip(ship, row, column, horizontal);
        this.doPrintBoards();
    }

    private void doRemoveShip(String paramString) throws NumberFormatException, OutOfBoardException, StatusException {
        StringTokenizer st = new StringTokenizer(paramString, " ", false);

        int row = this.parseRowCharToInt(st.nextToken().toUpperCase().charAt(0));
        int column = Integer.parseInt(st.nextToken()) - 1;

        localBoard.unsetShip(row, column);
        this.doPrintBoards();
    }

    private void doGiveUp() throws IOException {
        //swpEngine.writeGiveUpPDU();
    }

    private void doTalk(String paramString) {

    }

    private void doGetStatus() {
        consoleOutput.println("Your actual status: " + game.getStatus().toString());
    }

    private void doSave() {

    }

    private void doRestore() {

    }

    private void doConnect() throws IOException {
        try {
            consoleOutput.println("Specify the channel name: ");
            String channelName = consoleInput.readLine().trim();
            consoleOutput.println("As server: ");
            boolean asServer = consoleInput.readLine().matches(".*y.*");
            consoleOutput.println("Specify the port: ");
            String portString = consoleInput.readLine().trim();
            int port = Integer.parseInt(portString);

            TCPChannel tcpChannel = TCPChannel.createTCPChannel(port, asServer, channelName);
            new Thread(tcpChannel).start();
            tcpChannel.waitForConnection();

            DataInputStream dis = new DataInputStream(tcpChannel.getInputStream());
            DataOutputStream dos = new DataOutputStream(tcpChannel.getOutputStream());

            SWPEngine swpEngine = this.game.getSWPEngine();
            swpEngine.handleConnection(dis, dos, asServer);
            this.determinesStart = asServer;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    private void doFill() {

    }


}